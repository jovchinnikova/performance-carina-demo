package com.performance.demo.performance;

import com.google.common.collect.ImmutableMap;
import com.performance.demo.performance.dao.Gfx;
import com.performance.demo.performance.dao.Network;
import com.performance.demo.utils.parser.GeneralParser;
import com.performance.demo.utils.parser.GfxParser;
import com.performance.demo.utils.parser.NetParser;
import com.zebrunner.carina.webdriver.IDriverPool;
import org.openqa.selenium.HasCapabilities;
import org.openqa.selenium.JavascriptExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.time.Instant;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class AdbPerformanceCollector extends PerformanceCollector implements IDriverPool {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private static final String WLAN0 = "wlan0";
    private static final String WLAN1 = "wlan1";

    private final String bundleId = getAppPackage();
    private final GeneralParser generalParser;
    private final String pidCommand = String.format(PerformanceTypes.PID.cmdArgs, bundleId);
    private String cpuCommand;
    private String memCommand;
    private String netCommand;
    private String gfxCommand;

    private Map<String, NetParser.NetRow> netRowStart;
    private Map<String, NetParser.NetRow> netRowEnd;

    private int cpuQuantity = 0;
    private int memQuantity = 0;

    public AdbPerformanceCollector() {
        super();
        this.generalParser = new GeneralParser(bundleId);
        generateCommands();
    }

    public enum PerformanceTypes {
        CPU("ps -p %s -o %%cpu="),
        MEM("dumpsys meminfo %s | awk '/TOTAL PSS:/ {print $3} /TOTAL:/ {print $2}'"),
        NET("cat proc/%s/net/dev"),
        PID("pgrep -f %s"),
        GFX("dumpsys gfxinfo %s framestats");

        private final String cmdArgs;

        PerformanceTypes(String shellCmd) {
            this.cmdArgs = shellCmd;
        }
    }

    /**
     * Collects performance output data by executing a given adb shell command.
     * Attempts to collect data up to 3 times if no data is initially returned.
     *
     * @param command adb shell command to be executed.
     * @return String benchmark output, or null if data collection fails.
     */
    private String collectBenchmark(String command) {
        String output = "";
        try {
            for (int attempt = 1; attempt <= 3; attempt++) {
                output = executeMobileShellCommand(command).trim();
                if (!output.isEmpty()) {
                    break;
                }
                LOGGER.info("# Attempts for '{}' command: {}", command, attempt);
            }
        } catch (Exception e) {
            LOGGER.warn("There was an error during executing adb shell command");
        }
        return output;
    }

    /**
     * Collects CPU benchmark data by executing a shell command and processing the output.
     *
     * @return The calculated CPU benchmark value, or null if data collection fails.
     */
    @Override
    protected Double collectCpuBenchmarks() {
        Double cpuValue = null;
        String cpuOutput = collectBenchmark(cpuCommand);

        if (!cpuOutput.isEmpty()) {
            try {
                LOGGER.info("% cpu: {}", cpuOutput);
                cpuValue = Double.parseDouble(cpuOutput);
            } catch (Exception e) {
                LOGGER.warn("There was an error during parsing cpu command output");
            }
            cpuQuantity++;
        }

        return cpuValue;
    }

    /**
     * Collects Memory benchmark data by executing a shell command and processing the output.
     *
     * @return The calculated MEM benchmark value, or null if data collection fails.
     */
    protected Double collectMemoryBenchmarks() {
        Double memValue = null;
        String memOutput = collectBenchmark(memCommand);

        if (!memOutput.isEmpty()) {
            try {
                LOGGER.info("total pss: {}", memOutput);
                memValue = Double.parseDouble(memOutput);
            } catch (Exception e) {
                LOGGER.warn("There was an error during parsing MEM command output");
            }
            memQuantity++;
        }

        return memValue;
    }

    /**
     * Collects GFX benchmark data by executing a GFX command and parsing the output.
     *
     * @return GfxRow containing the parsed GFX benchmark results.
     */
    @Override
    protected GfxParser.GfxRow collectGfxBenchmarks() {
        String output = collectBenchmark(gfxCommand);

        return (GfxParser.GfxRow) generalParser.parse(Arrays.asList(output.split("\\n")), PerformanceTypes.GFX);
    }

    /**
     * Collects network benchmarks by executing a network command and parsing the output.
     */
    @Override
    protected void collectNetBenchmarks() {
        String netData = collectBenchmark(netCommand);
        String pid = executeMobileShellCommand(pidCommand).trim();
        LOGGER.info("PID: {} ", pid);

        String[] netOutput = netData.split("\\n");

        Map<String, NetParser.NetRow> netRow = generalParser.parseNet(List.of(netOutput));

        try {
            netRow.forEach((type, row) -> LOGGER.info("Net data type: {}, Net Row: {}", type, row));
        } catch (Exception e) {
            LOGGER.warn("There was an error during parsing of netdata");
        }

        if (netRowStart == null) {
            netRowStart = netRow;
        } else {
            netRowEnd = netRow;
        }

    }

    /**
     * Calculates the difference between two {@link NetParser.NetRow} instances and creates a {@link Network} instance with the results.
     *
     * @param rowStart The starting {@link NetParser.NetRow} instance.
     * @param rowEnd   The ending {@link NetParser.NetRow} instance.
     * @param instant  The timestamp for when the network data is recorded.
     * @param flowName The name of the network flow.
     * @return A {@link Network} instance containing the calculated results and additional information.
     */
    private Network makeSubtraction(NetParser.NetRow rowStart, NetParser.NetRow rowEnd, Instant instant, String flowName) {
        int rbResult = (int) (rowEnd.getRb() - rowStart.getRb());
        int rpResult = (int) (rowEnd.getRp() - rowStart.getRp());
        int tbResult = (int) (rowEnd.getTb() - rowStart.getTb());
        int tpResult = (int) (rowEnd.getTp() - rowStart.getTp());

        return new Network(rbResult, rpResult, tbResult, tpResult, instant, flowName, userName);
    }

    private void subtractNetData(Instant instant, String flowName) {
        try {
            Network resultRow;
            if (netRowStart.size() > 1 && netRowEnd.size() > 1) {
                Network netWlan0 = makeSubtraction(netRowStart.get(WLAN0), netRowEnd.get(WLAN0), instant, flowName);
                Network netWlan1 = makeSubtraction(netRowStart.get(WLAN1), netRowEnd.get(WLAN1), instant, flowName);
                if (netWlan0.getBytesReceived() != 0 && netWlan0.getTransferredBytes() != 0 && netWlan0.getReceivedPackets() != 0
                        && netWlan0.getTransferredPackets() != 0) {
                    resultRow = netWlan0;
                } else {
                    resultRow = netWlan1;
                }
            } else {
                String expectedType;
                if (netRowStart.containsKey(WLAN0) && netRowEnd.containsKey(WLAN0)) {
                    expectedType = WLAN0;
                } else {
                    expectedType = WLAN1;
                }
                resultRow = makeSubtraction(netRowStart.get(expectedType), netRowEnd.get(expectedType), instant, flowName);
            }

            if (resultRow.getBytesReceived() != 0 && resultRow.getTransferredBytes() != 0 && resultRow.getReceivedPackets() != 0
                    && resultRow.getTransferredPackets() != 0) {
                allBenchmarks.add(resultRow);
            } else {
                LOGGER.info("Skipping writing net data to influx because new bucket didn't start");
            }
        } catch (Exception e) {
            LOGGER.warn("No network data was received for the start or the end of the test");
        }
    }

    /**
     * Collects all benchmarks and evaluates if all expected data points have been successfully collected.
     *
     * @param flowName The name of the flow for which benchmarks are being collected.
     * @return true if all expected benchmarks have been collected, false otherwise.
     */
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
            actionCount = loadTimeQty + cpuQuantity + memQuantity + 4;
        } else if (collectLoginTime || collectExecutionTime) {
            actionCount = loadTimeQty + cpuQuantity + memQuantity + 3;
        } else {
            actionCount = loadTimeQty + cpuQuantity + memQuantity + 2;
            LOGGER.warn("No time duration was collected during test execution");
        }
        LOGGER.info("cpuQuantity: " + cpuQuantity + " , memQuantity: " + memQuantity);
        int benchmarkCount = allBenchmarks.size();

        LOGGER.info("Action count: {} benchmark count: {}", actionCount, benchmarkCount);
        if (actionCount == benchmarkCount)
            isAllDataCollected = true;

        return isAllDataCollected;
    }

    private void generateCommands() {
        String pid = executeMobileShellCommand(pidCommand).trim();
        cpuCommand = String.format(PerformanceTypes.CPU.cmdArgs, pid);
        netCommand = String.format(PerformanceTypes.NET.cmdArgs, pid);
        memCommand = String.format(PerformanceTypes.MEM.cmdArgs, bundleId);
        gfxCommand = String.format(PerformanceTypes.GFX.cmdArgs, bundleId);
    }

    private String getAppPackage() {
        return ((HasCapabilities) getDriver()).getCapabilities().getCapability("appium:appPackage").toString();
    }

    private String executeMobileShellCommand(String command) {
        return (String) ((JavascriptExecutor) getDriver()).executeScript("mobile: shell",
                ImmutableMap.of("command", "", "args", Collections.singletonList(command)));
    }

}
