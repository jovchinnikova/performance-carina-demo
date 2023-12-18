package com.performance.demo.performance;

import java.lang.invoke.MethodHandles;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.performance.demo.performance.dao.BaseMeasurement;
import com.performance.demo.performance.dao.Cpu;
import com.performance.demo.performance.dao.ExecutionTime;
import com.performance.demo.performance.dao.LoginTime;
import com.performance.demo.performance.dao.Memory;
import com.performance.demo.performance.service.InfluxDbService;
import com.performance.demo.utils.parser.GfxParser;
import com.performance.demo.utils.parser.MemParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Stopwatch;
import com.zebrunner.carina.webdriver.IDriverPool;

public abstract class PerformanceCollector implements IDriverPool {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final InfluxDbService dbService;

    private boolean epochSeconds = false;
    private long beginEpochMilli;
    protected long endEpochMilli;
    protected List<BaseMeasurement> allBenchmarks = new ArrayList<>();
    protected boolean cpuNotNull;
    protected boolean isAllDataCollected;
    protected boolean collectLoginTime;
    protected boolean collectExecutionTime;

    private Stopwatch loginStopwatch;
    private Stopwatch executionStopWatch;

    protected String userName;

    protected PerformanceCollector() {
        this.dbService = new InfluxDbService();
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

    public void collectLoginTime(String flowName) {
        Instant instant = Instant.now();
        LOGGER.info("[ PERFORMANCE INVESTIGATION ] Login took: {}", loginStopwatch);
        Double loginTime = (double) loginStopwatch.elapsed(TimeUnit.MILLISECONDS);
        allBenchmarks.add(new LoginTime(loginTime, instant, flowName,
                userName));
    }

    public void collectExecutionTime(String flowName) {
        Instant instant = Instant.now();
        executionStopWatch.stop();
        LOGGER.info("[ PERFORMANCE INVESTIGATION ] Test execution took: {}", executionStopWatch);
        Double executionTime = (double) executionStopWatch.elapsed(TimeUnit.MILLISECONDS);
        allBenchmarks.add(new ExecutionTime(executionTime, instant, flowName,
                userName));
    }

    public void collectAndWritePerformance(String flowName) {
        isAllDataCollected = collectAllBenchmarks(flowName);
        endEpochMilli = Instant.now().toEpochMilli() + 3000;
        if (isAllDataCollected)
            dbService.writeData(allBenchmarks);
        else
            LOGGER.warn("Skipped writing data to db, not all performance data were received during test execution");
    }

    protected abstract Double collectCpuBenchmarks();

    protected abstract MemParser.MemRow collectMemoryBenchmarks();

    protected abstract GfxParser.GfxRow collectGfxBenchmarks();

    public abstract void collectNetBenchmarks();

    protected abstract boolean collectAllBenchmarks(String flowName);

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public List<BaseMeasurement> getAllBenchmarks() {
        return allBenchmarks;
    }

    public void setAllBenchmarks(List<BaseMeasurement> allBenchmarks) {
        this.allBenchmarks = allBenchmarks;
    }

    public boolean isCpuNotNull() {
        return cpuNotNull;
    }

    public Stopwatch getLoginStopwatch() {
        return loginStopwatch;
    }

    public void setLoginStopwatch(Stopwatch loginStopwatch) {
        this.loginStopwatch = loginStopwatch;
    }

    public Stopwatch getExecutionStopWatch() {
        return executionStopWatch;
    }

    public void setExecutionStopWatch(Stopwatch executionStopWatch) {
        this.executionStopWatch = executionStopWatch;
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
