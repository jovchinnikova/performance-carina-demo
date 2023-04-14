package com.performance.demo.performance.android;

import com.google.common.base.Stopwatch;
import com.google.common.collect.ImmutableMap;
import com.performance.demo.performance.android.dao.*;
import com.performance.demo.performance.android.service.InfluxDbService;
import com.performance.demo.utils.parser.*;
import com.zebrunner.agent.core.registrar.Artifact;
import com.zebrunner.agent.core.registrar.CurrentTest;
import com.zebrunner.agent.core.registrar.CurrentTestRun;
import com.zebrunner.carina.utils.R;
import com.zebrunner.carina.webdriver.IDriverPool;
import io.appium.java_client.android.HasSupportedPerformanceDataType;
import org.openqa.selenium.HasCapabilities;
import org.openqa.selenium.JavascriptExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PerformanceData implements IDriverPool {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private static final String TEST_URL = "%s/d/Fg_sTfYnk/all_flows?orgId=2&from=%s&to=%s&var-app_version=%s" +
            "&var-os_version=%s&var-platform_name=%s&var-env=%s&var-device_name=%s&var-flow_id=%s&var-user=%s";
    public static final String RUN_URL = "%s/d/eIzJir4Vk/multiple_flows?orgId=2&var-run_id=%s";
    public static final String GRAFANA_TOKEN = R.TESTDATA.getDecrypted("grafana_token");

    private final InfluxDbService dbService;
    private final GeneralParser generalParser;
    private boolean epochSeconds = false;
    private long beginEpochMilli;
    private long endEpochMilli;
    private List<BaseMeasurement> allBenchmarks = new ArrayList<>();
    private int cpuQuantity = 0;
    private int memQuantity = 0;
    private boolean cpuNotNull;

    private static Stopwatch loginStopwatch;
    private static Stopwatch executionStopWatch;

    private String userName;

    private final Long runId = CurrentTestRun.getId().orElse(0L);
    private final Long testId = CurrentTest.getId().orElse(0L);
    private final String errorOutput = String.format("No process found for: %s\n", bundleId);

    private static String bundleId;

    private static NetParser.NetRow rowStart;

    public PerformanceData() {
        this.dbService = new InfluxDbService();
        this.generalParser = new GeneralParser();
        bundleId = getAppPackage();
    }

    public enum PerformanceTypes {
        CPU("top -n 1 | grep -E \"%s\""),
        MEM("dumpsys meminfo %s"),
        NET("cat proc/%s/net/dev"),
        PID("pidof -s %s"),
        CORE("cat /proc/stat"),
        USERID("dumpsys package %s | grep userId"),
        GFX("dumpsys gfxinfo %s framestats");

        private final String cmdArgs;

        PerformanceTypes(String shellCmd) {
            this.cmdArgs = shellCmd;
        }
    }

    public void collectSnapshotBenchmarks(String flowName) {

        Double cpuValue = collectCpuBenchmarks();
        MemParser.MemRow memRow = collectMemoryBenchmarks();

        Instant instant = Instant.now();

        if (!epochSeconds) {
            long instantToEpoch = instant.toEpochMilli();
            beginEpochMilli = instantToEpoch - 3000;
            LOGGER.info("Instant in epoch ms from snapshots: {}", instantToEpoch);
            epochSeconds = true;
        }

        try {
            if (cpuNotNull) {
                allBenchmarks.add(new Cpu(cpuValue, instant, flowName, userName));
            }
            allBenchmarks.add(new Memory(memRow.getTotalPss().doubleValue(), instant, flowName,
                    userName));
        } catch (Exception e) {
            LOGGER.warn("No data was received for memory or cpu");
        }
    }

    public void collectBenchmarks(String flowName) {
        Instant instant = Instant.now();

        GfxParser.GfxRow r = collectGfxBenchmarks();

        try {
            LOGGER.info("GFX Row: {}", r);
            allBenchmarks.add(new Gfx(
                    r.getTotalFrames(),
                    r.getJankyFrames(),
                    r.getPercentile90(),
                    r.getPercentile95(),
                    r.getPercentile99(),
                    instant,
                    flowName,
                    userName
            ));
        } catch (Exception e) {
            LOGGER.warn("No data was received for gfx");
        }

        NetParser.NetRow row = (NetParser.NetRow) collectNetBenchmarks();

        subtractNetData(getRowStart(), row, instant, flowName);

        endEpochMilli = instant.toEpochMilli() + 3000;

        dbService.writeData(allBenchmarks, cpuQuantity, memQuantity, flowName);
        if (dbService.isMatchCount()) {
            String dashboardUrl = generateDashboardUrl(flowName, R.CONFIG.get("capabilities.platformName").toUpperCase(), R.CONFIG.get("env"));
            LOGGER.info("DASHBOARD URL: {}", dashboardUrl);
            if (runId != 0 && testId != 0)
                Artifact.attachReferenceToTest("Performance dashboard", dashboardUrl);
        } else {
            LOGGER.warn("Not all performance data were received during test execution");
        }
    }

    /**
     * Old way of getting net data, works only for Android 11 and 12, depends on the bucket start
     */
    public Row collectNetBenchmarksOld() {
        String id;
        String userId = "";
        String netData = "";

        if ("11".equals(getDevice().getOsVersion()) || "12".equals(getDevice().getOsVersion())) {

            Pattern userIdPattern = Pattern.compile("\\s*userId=(\\d*)");

            String userIdCommand = String.format(PerformanceTypes.USERID.cmdArgs, bundleId);

            id = ((String) ((JavascriptExecutor) getDriver()).executeScript("mobile: shell",
                    ImmutableMap.of("command", "", "args", Collections.singletonList(userIdCommand)))).
                    replaceAll("\\s+", "");
            LOGGER.info("UID output: {}", id);
            Matcher m = userIdPattern.matcher(id);
            if (m.matches()) {
                userId = m.group(1);
                LOGGER.info("UserId: {}", userId);
            }

            for (int x = 0; x <= 7; x++) {
                netData = (String) ((JavascriptExecutor) getDriver()).executeScript("mobile: shell",
                        ImmutableMap.of("command", "", "args", Collections.singletonList("dumpsys netstats detail")));
                if (netData.contains("uid=" + userId)) {
                    break;
                }
                LOGGER.info("# Attempts: {} ", (x + 1));
            }
        }

        String[] netOutput = netData.split("\\n");

        return generalParser.parseNet(List.of(netOutput), getDevice(), userId);
    }

    public Row collectNetBenchmarks() {
        String pid;
        String netData = "";

        String pidCommand = String.format(PerformanceTypes.PID.cmdArgs, bundleId);

        pid = ((String) ((JavascriptExecutor) getDriver()).executeScript("mobile: shell",
                ImmutableMap.of("command", "", "args", Collections.singletonList(pidCommand)))).
                replaceAll("\\s+", "");
        LOGGER.info("PID: {} ", pid);

        String netCommand = String.format(PerformanceTypes.NET.cmdArgs, pid);

        for (int x = 0; x <= 7; x++) {
            netData = (String) ((JavascriptExecutor) getDriver()).executeScript("mobile: shell",
                    ImmutableMap.of("command", "", "args", Collections.singletonList(netCommand)));
            if (!netData.isEmpty()) {
                break;
            }
            LOGGER.info("# Attempts: {}", (x + 1));
        }

        String[] netOutput = netData.split("\\n");

        NetParser.NetRow netRow = (NetParser.NetRow) generalParser.parse(List.of(netOutput), PerformanceTypes.NET);

        try {
            LOGGER.info("Net Row: {}", netRow);
        } catch (Exception e) {
            LOGGER.warn("There was an error during parsing of netdata");
        }
        return netRow;
    }

    private void subtractNetData(NetParser.NetRow rowStart, NetParser.NetRow rowEnd, Instant instant, String flowName) {
        try {
            int rbResult = (int) (rowEnd.getRb() - rowStart.getRb());
            int rpResult = (int) (rowEnd.getRp() - rowStart.getRp());
            int tbResult = (int) (rowEnd.getTb() - rowStart.getTb());
            int tpResult = (int) (rowEnd.getTp() - rowStart.getTp());

            if (rowStart.getSt() == rowEnd.getSt()) {
                if (rbResult != 0 && rpResult != 0 && tbResult != 0 && tpResult != 0) {
                    allBenchmarks.add(new Network(
                            rbResult, rpResult, tbResult, tpResult,
                            instant, flowName, userName));
                } else {
                    LOGGER.info("Skipping writing net data to influx because new bucket didn't start");
                }
            } else {
                LOGGER.info("Skipping writing net data to influx due to different st values");
            }
        } catch (Exception e) {
            LOGGER.warn("No network data was received for the start or the end of the test");
        }
    }

    private Double collectCpuBenchmarks() {
        String pid;

        String pidCommand = String.format(PerformanceTypes.PID.cmdArgs, bundleId);

        pid = ((String) ((JavascriptExecutor) getDriver()).executeScript("mobile: shell",
                ImmutableMap.of("command", "", "args", Collections.singletonList(pidCommand)))).
                replaceAll("\\s+", "");
        LOGGER.info("PID: {} ", pid);

        String cpuOutput = "";
        String cpuCommand = String.format(PerformanceTypes.CPU.cmdArgs, pid);

        try {
            for (int x = 0; x <= 7; x++) {
                cpuOutput = (String) ((JavascriptExecutor) getDriver()).executeScript("mobile: shell",
                        ImmutableMap.of("command", "", "args", Collections.singletonList(cpuCommand)));
                if (!cpuOutput.isEmpty()) {
                    cpuQuantity++;
                    break;
                }
                LOGGER.info("# Attempts for cpu: {}", (x + 1));
            }
        } catch (Exception e) {
            LOGGER.warn("There was an error during executing adb shell command");
        }

        cpuNotNull = !cpuOutput.isEmpty();

        LOGGER.info(cpuOutput);
        String[] cpuData = cpuOutput.trim().split("\\s+");
        LOGGER.info("cpuArray: {}", Arrays.toString(cpuData));
        LOGGER.info("cpuArray size: {}", cpuData.length);
        int cpuIndex = 8;
        if (cpuData.length > 12) {
            cpuIndex++;
        }

        String coreOutput = (String) ((JavascriptExecutor) getDriver()).executeScript("mobile: shell",
                ImmutableMap.of("command", "", "args", Collections.singletonList(PerformanceTypes.CORE.cmdArgs)));

        CoreParser.CoreRow coreRow = (CoreParser.CoreRow) generalParser.parse(Arrays.asList(coreOutput.split("\\n")),
                PerformanceTypes.CORE);
        int coreQuantity = coreRow.getCoreQuantity();
        double cpuValue = 0.0;
        try {
            cpuValue = Double.parseDouble(cpuData[cpuIndex]) / coreQuantity;
            LOGGER.info("CPU value: {}", cpuValue);
        } catch (Exception e) {
            LOGGER.warn("No data was received for cpu");
        }

        return cpuValue;
    }

    private MemParser.MemRow collectMemoryBenchmarks() {
        String memOutput = "";

        String memCommand = String.format(PerformanceTypes.MEM.cmdArgs, bundleId);

        for (int x = 0; x <= 7; x++) {
            memOutput = (String) ((JavascriptExecutor) getDriver()).executeScript("mobile: shell",
                    ImmutableMap.of("command", "", "args", Collections.singletonList(memCommand)));
            if (!memOutput.isEmpty() && !errorOutput.equals(memOutput)) {
                memQuantity++;
                break;
            }
            LOGGER.info("# Attempts for pss: {}", (x + 1));
        }

        MemParser.MemRow row = (MemParser.MemRow) generalParser.parse(Arrays.asList(memOutput.split("\\n")), PerformanceTypes.MEM);
        try {
            LOGGER.info("total pss: {}", row.getTotalPss());
        } catch (Exception e) {
            LOGGER.warn("No data was received for memory during parsing");
        }
        return row;
    }

    private GfxParser.GfxRow collectGfxBenchmarks() {
        String output = "";

        String gfxCommand = String.format(PerformanceTypes.GFX.cmdArgs, bundleId);

        for (int x = 0; x <= 7; x++) {
            output = (String) ((JavascriptExecutor) getDriver()).executeScript("mobile: shell",
                    ImmutableMap.of("command", "", "args", Collections.singletonList(gfxCommand)));
            if (!output.equals(errorOutput)) {
                break;
            }
            LOGGER.info("# Attempts: {}", (x + 1));
        }

        return (GfxParser.GfxRow) generalParser.parse(Arrays.asList(output.split("\\n")), PerformanceTypes.GFX);
    }

    public void collectLoginTime(Stopwatch stopwatch, Instant instant, String flowName) {
        LOGGER.info("[ PERFORMANCE INVESTIGATION ] Login took: {}", stopwatch);
        Double loginTime = (double) stopwatch.elapsed(TimeUnit.MILLISECONDS);
        allBenchmarks.add(new LoginTime(loginTime, instant, flowName,
                userName));
    }

    public void collectExecutionTime(Stopwatch stopwatch, Instant instant, String flowName) {
        stopwatch.stop();
        LOGGER.info("[ PERFORMANCE INVESTIGATION ] Test execution took: {}", stopwatch);
        Double executionTime = (double) stopwatch.elapsed(TimeUnit.MILLISECONDS);
        allBenchmarks.add(new ExecutionTime(executionTime, instant, flowName,
                userName));
    }

    private String generateDashboardUrl(String flowName, String platformName, String env) {
        return String.format(TEST_URL, R.TESTDATA.get("grafana_host"), beginEpochMilli, endEpochMilli,
                R.CONFIG.get("app_version"), getDevice().getOsVersion(), platformName, env, getDevice().getName(),
                flowName, userName);
    }

    private HashMap<String, Double> getPerfDataFromAppium(PerformanceTypes performanceType) {
        return parsePerfData(((HasSupportedPerformanceDataType) getDriver()).getPerformanceData(
                bundleId, performanceType.cmdArgs,
                2));
    }

    private static HashMap<String, Double> parsePerfData(List<List<Object>> data) {
        HashMap<String, Double> readableData = new HashMap<>();
        for (int i = 0; i < data.get(0).size(); i++) {
            double val;
            if (data.get(1).get(i) == null) {
                val = 0;
            } else {
                val = Double.parseDouble((String) data.get(1).get(i));
            }
            readableData.put((String) data.get(0).get(i), val);
        }
        return readableData;
    }

    private String getAppPackage() {
        return ((HasCapabilities) getDriver()).getCapabilities().
                getCapability("appium:appPackage").toString();
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public static NetParser.NetRow getRowStart() {
        return rowStart;
    }

    public static void setRowStart(NetParser.NetRow rowStart) {
        PerformanceData.rowStart = rowStart;
    }

    public List<BaseMeasurement> getAllBenchmarks() {
        return allBenchmarks;
    }

    public void setAllBenchmarks(List<BaseMeasurement> allBenchmarks) {
        this.allBenchmarks = allBenchmarks;
    }

    public int getCpuQuantity() {
        return cpuQuantity;
    }

    public void setCpuQuantity(int cpuQuantity) {
        this.cpuQuantity = cpuQuantity;
    }

    public int getMemQuantity() {
        return memQuantity;
    }

    public void setMemQuantity(int memQuantity) {
        this.memQuantity = memQuantity;
    }

    public boolean isCpuNotNull() {
        return cpuNotNull;
    }

    public Long getRunId() {
        return runId;
    }

    public Long getTestId() {
        return testId;
    }

    public static Stopwatch getLoginStopwatch() {
        return loginStopwatch;
    }

    public static void setLoginStopwatch(Stopwatch loginStopwatch) {
        PerformanceData.loginStopwatch = loginStopwatch;
    }

    public static Stopwatch getExecutionStopWatch() {
        return executionStopWatch;
    }

    public static void setExecutionStopWatch(Stopwatch executionStopWatch) {
        PerformanceData.executionStopWatch = executionStopWatch;
    }

    public static String getBundleId() {
        return bundleId;
    }
}
