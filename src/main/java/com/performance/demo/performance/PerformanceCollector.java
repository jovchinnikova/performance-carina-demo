package com.performance.demo.performance;

import java.lang.invoke.MethodHandles;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.TimeUnit;

import com.performance.demo.performance.dao.*;
import com.performance.demo.performance.service.InfluxDbService;
import com.performance.demo.utils.parser.GfxParser;
import com.performance.demo.utils.parser.MemParser;
import com.performance.demo.utils.parser.NetParser;
import com.zebrunner.carina.webdriver.IDriverPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Stopwatch;

public abstract class PerformanceCollector implements IDriverPool {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final InfluxDbService dbService;

    private boolean epochSeconds = false;
    private long beginEpochMilli;
    private long endEpochMilli;
    protected List<BaseMeasurement> allBenchmarks = new ArrayList<>();
    protected int cpuQuantity = 0;
    protected int memQuantity = 0;
    protected boolean cpuNotNull;
    private boolean isMatchCount;
    private boolean collectLoginTime;
    private boolean collectExecutionTime;

    private Stopwatch loginStopwatch;
    private Stopwatch executionStopWatch;

    protected NetParser.NetRow rowStart;

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
                    userName));
        } catch (Exception e) {
            LOGGER.warn("No data was received for gfx");
        }

        NetParser.NetRow row = collectNetBenchmarks();

        subtractNetData(row, instant, flowName);

        endEpochMilli = instant.toEpochMilli() + 3000;

        isMatchCount = dbService.writeData(allBenchmarks, cpuQuantity, memQuantity, collectLoginTime, collectExecutionTime);

        if (!isMatchCount)
            LOGGER.warn("Skipped writing data to db, not all performance data were received during test execution");
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

    protected abstract Double collectCpuBenchmarks();

    protected abstract MemParser.MemRow collectMemoryBenchmarks();

    protected abstract GfxParser.GfxRow collectGfxBenchmarks();

    public abstract NetParser.NetRow collectNetBenchmarks();

    protected abstract void subtractNetData(NetParser.NetRow rowEnd, Instant instant, String flowName);

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

    public boolean isMatchCount() {
        return isMatchCount;
    }

    public void setMatchCount(boolean matchCount) {
        isMatchCount = matchCount;
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

    public NetParser.NetRow getRowStart() {
        return rowStart;
    }

    public void setRowStart(NetParser.NetRow rowStart) {
        this.rowStart = rowStart;
    }
}
