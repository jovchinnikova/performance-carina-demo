package com.performance.demo.performance;

import java.lang.invoke.MethodHandles;
import java.time.Instant;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.performance.demo.performance.dao.Gfx;
import com.performance.demo.performance.dao.Network;
import com.performance.demo.utils.parser.CoreParser;
import com.performance.demo.utils.parser.GeneralParser;
import com.performance.demo.utils.parser.GfxParser;
import com.performance.demo.utils.parser.MemParser;
import com.performance.demo.utils.parser.NetParser;
import org.openqa.selenium.HasCapabilities;
import org.openqa.selenium.JavascriptExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.ImmutableMap;
import com.zebrunner.carina.webdriver.IDriverPool;

import io.appium.java_client.android.HasSupportedPerformanceDataType;

public class AdbPerformanceCollector extends PerformanceCollector implements IDriverPool {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final String bundleId = getAppPackage();
    private final GeneralParser generalParser;
    private final String cpuCommand = generateCpuCommand();
    private final String errorOutput = String.format("No process found for: %s%n", bundleId);
    private final String pidCommand = String.format(PerformanceTypes.PID.cmdArgs, bundleId);

    private NetParser.NetRow netRowStart;
    private NetParser.NetRow netRowEnd;

    private int cpuQuantity = 0;
    private int memQuantity = 0;

    public AdbPerformanceCollector() {
        super();
        this.generalParser = new GeneralParser(bundleId);
    }

    public enum PerformanceTypes {
        CPU("top -n 1 | grep -E \"%s|%s\""),
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

    @Override
    protected Double collectCpuBenchmarks() {
        String cpuOutput = "";

        try {
            for (int x = 0; x <= 7; x++) {
                cpuOutput = executeMobileShellCommand(cpuCommand);
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

        String coreOutput = executeMobileShellCommand(PerformanceTypes.CORE.cmdArgs);

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

    @Override
    protected MemParser.MemRow collectMemoryBenchmarks() {
        String memOutput = "";
        String memCommand = String.format(PerformanceTypes.MEM.cmdArgs, bundleId);

        for (int x = 0; x <= 7; x++) {
            memOutput = executeMobileShellCommand(memCommand);
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

    @Override
    protected GfxParser.GfxRow collectGfxBenchmarks() {
        String output = "";
        String gfxCommand = String.format(PerformanceTypes.GFX.cmdArgs, bundleId);

        for (int x = 0; x <= 7; x++) {
            output = executeMobileShellCommand(gfxCommand);
            if (!output.equals(errorOutput)) {
                break;
            }
            LOGGER.info("# Attempts: {}", (x + 1));
        }

        return (GfxParser.GfxRow) generalParser.parse(Arrays.asList(output.split("\\n")), PerformanceTypes.GFX);
    }

    @Override
    public void collectNetBenchmarks() {
        String pid;
        String netData = "";

        pid = executeMobileShellCommand(pidCommand).replaceAll("\\s+", "");
        LOGGER.info("PID: {} ", pid);

        String netCommand = String.format(PerformanceTypes.NET.cmdArgs, pid);

        for (int x = 0; x <= 7; x++) {
            netData = executeMobileShellCommand(netCommand);
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

        if (netRowStart == null) {
            netRowStart = netRow;
        } else {
            netRowEnd = netRow;
        }
    }

    private void subtractNetData(Instant instant, String flowName) {
        try {
            int rbResult = (int) (netRowEnd.getRb() - netRowStart.getRb());
            int rpResult = (int) (netRowEnd.getRp() - netRowStart.getRp());
            int tbResult = (int) (netRowEnd.getTb() - netRowStart.getTb());
            int tpResult = (int) (netRowEnd.getTp() - netRowStart.getTp());

            if (netRowStart.getSt() == netRowEnd.getSt()) {
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

    @Override
    public boolean collectAllBenchmarks(String flowName) {
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
                    userName));
        } catch (Exception e) {
            LOGGER.warn("No data was received for gfx");
        }

        collectNetBenchmarks();
        subtractNetData(instant, flowName);

        boolean isAllDataCollected = false;

        int actionCount;
        if (collectLoginTime && collectExecutionTime) {
            actionCount = cpuQuantity + memQuantity + 4;
        } else if (collectLoginTime || collectExecutionTime) {
            actionCount = cpuQuantity + memQuantity + 3;
        } else {
            actionCount = cpuQuantity + memQuantity + 2;
            LOGGER.warn("No time duration was collected during test execution");
        }

        int benchmarkCount = allBenchmarks.size();

        LOGGER.info(String.format("Action count: %s benchmark count: %s", actionCount, benchmarkCount));
        if (actionCount == benchmarkCount)
            isAllDataCollected = true;

        return isAllDataCollected;
    }

    /**
     * Old way of getting net data, works only for Android 11 and 12, depends on the bucket start
     */
    protected NetParser.NetRow collectNetBenchmarksOld() {
        String id;
        String userId = "";
        String netData = "";

        if ("11".equals(getDevice().getOsVersion()) || "12".equals(getDevice().getOsVersion())) {

            Pattern userIdPattern = Pattern.compile("\\s*userId=(\\d*)");
            String userIdCommand = String.format(PerformanceTypes.USERID.cmdArgs, bundleId);

            id = executeMobileShellCommand(userIdCommand).replaceAll("\\s+", "");
            LOGGER.info("UID output: {}", id);
            Matcher m = userIdPattern.matcher(id);
            if (m.matches()) {
                userId = m.group(1);
                LOGGER.info("UserId: {}", userId);
            }

            for (int x = 0; x <= 7; x++) {
                netData = executeMobileShellCommand("dumpsys netstats detail");
                if (netData.contains("uid=" + userId)) {
                    break;
                }
                LOGGER.info("# Attempts: {} ", (x + 1));
            }
        }

        String[] netOutput = netData.split("\\n");

        NetParser.NetRow netRow = (NetParser.NetRow) generalParser.parseNet(List.of(netOutput), getDevice(), userId);

        try {
            LOGGER.info("Net Row: {}", netRow);
        } catch (Exception e) {
            LOGGER.warn("There was an error during parsing of netdata");
        }
        return netRow;
    }

    private String generateCpuCommand() {
        String processName1 = bundleId.substring(0, 14).concat("+");
        String processName2 = bundleId.substring(0, 15).concat("+");
        return String.format(PerformanceTypes.CPU.cmdArgs, processName1, processName2);
    }

    private String getAppPackage() {
        return ((HasCapabilities) getDriver()).getCapabilities().getCapability("appium:appPackage").toString();
    }

    private String executeMobileShellCommand(String command) {
        return (String) ((JavascriptExecutor) getDriver()).executeScript("mobile: shell",
                ImmutableMap.of("command", "", "args", Collections.singletonList(command)));
    }

    private HashMap<String, Double> getPerfDataFromAppium(PerformanceTypes performanceType) {
        return parsePerfData(((HasSupportedPerformanceDataType) getDriver()).getPerformanceData(
                bundleId, performanceType.cmdArgs, 2));
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

    public GeneralParser getGeneralParser() {
        return generalParser;
    }

    public String getCpuCommand() {
        return cpuCommand;
    }

    public String getBundleId() {
        return bundleId;
    }

    public String getErrorOutput() {
        return errorOutput;
    }

    public String getPidCommand() {
        return pidCommand;
    }

}
