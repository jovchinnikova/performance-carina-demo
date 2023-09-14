package com.performance.demo.performance;

import com.google.common.base.Stopwatch;
import com.performance.demo.utils.parser.NetParser;
import com.zebrunner.carina.utils.commons.SpecialKeywords;
import com.zebrunner.carina.webdriver.config.WebDriverConfiguration;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.support.events.WebDriverListener;

public class PerformanceListener implements WebDriverListener {

    private static String flowName;
    private static PerformanceData performanceData;

    private static String loginMethodName;

    /**
     * This method should be used in the beginning of each performance test
     */
    public static void startPerformanceTracking(String flowName, String userName, boolean isCollectLoginTime, boolean isCollectExecutionTime) {
        if (!SpecialKeywords.IOS.equalsIgnoreCase(WebDriverConfiguration.getCapability(CapabilityType.PLATFORM_NAME).orElseThrow())) {
            performanceData = new PerformanceData();
            performanceData.setUserName(userName);
            setFlowName(flowName);
            performanceData.setCollectLoginTime(isCollectLoginTime);
            performanceData.setCollectExecutionTime(isCollectExecutionTime);
            startTracking();
        }
    }

    /**
     * This method is used in authService.loginByUsernameWithPerf
     */
    public static void collectLoginTime() {
        if (flowName != null && (performanceData.isCollectLoginTime() && performanceData.isCollectExecutionTime()))
            performanceData.collectLoginTime(flowName);
    }

    /**
     * This method should be used in the end of each performance test
     */
    public static void collectPerfBenchmarks() {
        if (flowName != null) {
            if (performanceData.isCollectLoginTime() && !performanceData.isCollectExecutionTime())
                performanceData.collectLoginTime(flowName);
            else if (performanceData.isCollectExecutionTime())
                performanceData.collectExecutionTime(flowName);

            performanceData.collectBenchmarks(flowName);
        }
    }

    @Override
    public void afterClick(WebElement element) {
        if (flowName != null)
            performanceData.collectSnapshotBenchmarks(flowName);
    }

    @Override
    public void afterSendKeys(WebElement element, CharSequence... keysToSend) {
        if (flowName != null)
            performanceData.collectSnapshotBenchmarks(flowName);
    }

    private static void startTracking() {
        if (flowName != null) {
            if (performanceData.isCollectLoginTime() && performanceData.isCollectExecutionTime()) {
                Stopwatch stopwatch = Stopwatch.createStarted();
                performanceData.setLoginStopwatch(stopwatch);
                performanceData.setExecutionStopWatch(stopwatch);
            } else if (performanceData.isCollectLoginTime()) {
                performanceData.setLoginStopwatch(Stopwatch.createStarted());
            } else if (performanceData.isCollectExecutionTime())
                performanceData.setExecutionStopWatch(Stopwatch.createStarted());

            NetParser.NetRow row = (NetParser.NetRow) performanceData.collectNetBenchmarks();
            performanceData.setRowStart(row);
        }
    }

    public static PerformanceData getPerformanceData() {
        return performanceData;
    }

    public static String getFlowName() {
        return flowName;
    }

    public static void setFlowName(String flowName) {
        PerformanceListener.flowName = flowName;
    }

    public static String getLoginMethodName() {
        return loginMethodName;
    }

    public static void setLoginMethodName(String loginMethodName) {
        PerformanceListener.loginMethodName = loginMethodName;
    }
}

