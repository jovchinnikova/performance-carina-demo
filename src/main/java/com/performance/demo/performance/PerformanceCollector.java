package com.performance.demo.performance;

import com.google.common.base.Stopwatch;
import com.performance.demo.performance.dao.*;
import com.performance.demo.performance.service.InfluxDbService;
import com.performance.demo.utils.parser.GfxParser;
import com.zebrunner.carina.utils.R;
import com.zebrunner.carina.webdriver.IDriverPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public abstract class PerformanceCollector implements IDriverPool {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final InfluxDbService dbService;

    private boolean epochSeconds = false;
    private long beginEpochMilli;
    protected long endEpochMilli;
    protected List<BaseMeasurement> allBenchmarks = new ArrayList<>();
    protected boolean isAllDataCollected;
    protected boolean collectLoginTime;
    protected boolean collectExecutionTime;

    private Stopwatch loginStopwatch;
    private Stopwatch executionStopwatch;
    private Stopwatch loadTimeStopwatch;

    private String actionName;
    private String elementName;

    protected String userName;
    protected int loadTimeQty = 0;

    protected PerformanceCollector() {
        this.dbService = new InfluxDbService();
    }

    public void collectLoadTime(String flowName) {
        Instant instant = Instant.now();
        loadTimeStopwatch.stop();
        Double loadTime = (double) loadTimeStopwatch.elapsed(TimeUnit.MILLISECONDS);
        LOGGER.info("LOAD TIME " + loadTime);
        allBenchmarks.add(new LoadTime(loadTime, flowName, instant, userName, actionName, elementName));
        loadTimeQty++;
    }

    public void collectSnapshotBenchmarks(String flowName, String actionName, String elementName) {
        Instant instant = Instant.now();
        Double cpuValue = collectCpuBenchmarks();
        Double memValue = collectMemoryBenchmarks();
        Network netValue = subtractNetData(instant, flowName, actionName, elementName);

        if (!epochSeconds) {
            long instantToEpoch = instant.toEpochMilli();
            beginEpochMilli = instantToEpoch - 3000;
            LOGGER.info("Instant in epoch ms from snapshots: {}", instantToEpoch);
            epochSeconds = true;
        }

        try {
            if (netValue != null) {
                allBenchmarks.add(netValue);
            } else {
                throw new NullPointerException("No runtime data for network was received.");
            }
            allBenchmarks.add(new Cpu(cpuValue, instant, flowName, userName, actionName, elementName));
            allBenchmarks.add(new Memory(memValue, instant, flowName, userName, actionName, elementName));
        } catch (Exception e) {
            LOGGER.warn("There was a problem in snapshot benchmarks:");
            LOGGER.warn(e.getMessage());
        }
    }

    public void collectLoginTime(String flowName) {
        Instant instant = Instant.now();
        LOGGER.info("[ PERFORMANCE INVESTIGATION ] Login took: {}", loginStopwatch);
        Double loginTime = (double) loginStopwatch.elapsed(TimeUnit.MILLISECONDS);
        allBenchmarks.add(new LoginTime(loginTime, instant, flowName, userName));
    }

    public void collectExecutionTime(String flowName) {
        Instant instant = Instant.now();
        executionStopwatch.stop();
        LOGGER.info("[ PERFORMANCE INVESTIGATION ] Test execution took: {}", executionStopwatch);
        Double executionTime = (double) executionStopwatch.elapsed(TimeUnit.MILLISECONDS);
        allBenchmarks.add(new ExecutionTime(executionTime, instant, flowName, userName));
    }

    public void collectAndWritePerformance(String flowName) {
        isAllDataCollected = collectAllBenchmarks(flowName);
        endEpochMilli = Instant.now().toEpochMilli() + 3000;
        if (Boolean.parseBoolean(R.TESTDATA.get("action_count_check"))) {
            if (isAllDataCollected)
                dbService.writeData(allBenchmarks);
            else
                LOGGER.warn("Skipped writing data to db, not all performance data were received during test execution");
        } else {
            dbService.writeData(allBenchmarks);
        }
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setActionElementNames(String actionName, String elementName) {
        this.actionName = actionName;
        this.elementName = elementName;
    }

    protected abstract Double collectCpuBenchmarks();

    protected abstract Double collectMemoryBenchmarks();

    protected abstract GfxParser.GfxRow collectGfxBenchmarks();

    protected abstract Network subtractNetData(Instant instant, String flowName, String actionName, String elementName);

    protected abstract void collectNetData();

    protected abstract boolean collectAllBenchmarks(String flowName);

    public Stopwatch getLoadTimeStopwatch() {
        return loadTimeStopwatch;
    }

    public void setLoadTimeStopwatch(Stopwatch loadTimeStopwatch) {
        this.loadTimeStopwatch = loadTimeStopwatch;
    }

    public Stopwatch getLoginStopwatch() {
        return loginStopwatch;
    }

    public void setLoginStopwatch(Stopwatch loginStopwatch) {
        this.loginStopwatch = loginStopwatch;
    }

    public Stopwatch getExecutionStopwatch() {
        return executionStopwatch;
    }

    public void setExecutionStopwatch(Stopwatch executionStopwatch) {
        this.executionStopwatch = executionStopwatch;
    }

    public boolean isAllDataCollected() {
        return isAllDataCollected;
    }

    public void setAllDataCollected(boolean allDataCollected) {
        isAllDataCollected = allDataCollected;
    }

    public long getBeginEpochMilli() {
        return beginEpochMilli;
    }

    public void setBeginEpochMilli(long beginEpochMilli) {
        this.beginEpochMilli = beginEpochMilli;
    }

    public long getEndEpochMilli() {
        return endEpochMilli;
    }

    public void setEndEpochMilli(long endEpochMilli) {
        this.endEpochMilli = endEpochMilli;
    }

    public boolean isCollectLoginTime() {
        return collectLoginTime;
    }

    public void setCollectLoginTime(boolean collectLoginTime) {
        this.collectLoginTime = collectLoginTime;
    }

    public boolean isCollectExecutionTime() {
        return collectExecutionTime;
    }

    public void setCollectExecutionTime(boolean collectExecutionTime) {
        this.collectExecutionTime = collectExecutionTime;
    }
}
