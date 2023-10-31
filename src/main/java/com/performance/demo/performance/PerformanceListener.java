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
    private static PerformanceCollector performanceCollector;

    private static String loginMethodName;

    /**
     * This method should be used in the beginning of each performance test
     */
    public static void startPerformanceTracking(String flowName, String userName, boolean isCollectLoginTime, boolean isCollectExecutionTime) {
        if (!SpecialKeywords.IOS.equalsIgnoreCase(WebDriverConfiguration.getCapability(CapabilityType.PLATFORM_NAME).orElseThrow())) {
            performanceCollector = new AdbPerformanceCollector();
            performanceCollector.setUserName(userName);
            setFlowName(flowName);
            performanceCollector.setCollectLoginTime(isCollectLoginTime);
            performanceCollector.setCollectExecutionTime(isCollectExecutionTime);
            startTracking();
        }
    }

    /**
     * This method is used in authService.loginByUsernameWithPerf
     */
    public static void collectLoginTime() {
        if (flowName != null && (performanceCollector.isCollectLoginTime() && performanceCollector.isCollectExecutionTime()))
            performanceCollector.collectLoginTime(flowName);
    }

    /**
     * This method should be used in the end of each performance test
     */
    public static void collectPerfBenchmarks() {
        if (flowName != null) {
            if (performanceCollector.isCollectLoginTime() && !performanceCollector.isCollectExecutionTime())
                performanceCollector.collectLoginTime(flowName);
            else if (performanceCollector.isCollectExecutionTime())
                performanceCollector.collectExecutionTime(flowName);

            performanceCollector.collectBenchmarks(flowName);
        }
    }

    @Override
    public void afterClick(WebElement element) {
        if (flowName != null)
            performanceCollector.collectSnapshotBenchmarks(flowName);
    }

    @Override
    public void afterSendKeys(WebElement element, CharSequence... keysToSend) {
        if (flowName != null)
            performanceCollector.collectSnapshotBenchmarks(flowName);
    }

    private static void startTracking() {
        if (flowName != null) {
            if (performanceCollector.isCollectLoginTime() && performanceCollector.isCollectExecutionTime()) {
                Stopwatch stopwatch = Stopwatch.createStarted();
                performanceCollector.setLoginStopwatch(stopwatch);
                performanceCollector.setExecutionStopWatch(stopwatch);
            } else if (performanceCollector.isCollectLoginTime()) {
                performanceCollector.setLoginStopwatch(Stopwatch.createStarted());
            } else if (performanceCollector.isCollectExecutionTime())
                performanceCollector.setExecutionStopWatch(Stopwatch.createStarted());

            NetParser.NetRow row = performanceCollector.collectNetBenchmarks();
            performanceCollector.setRowStart(row);
        }
    }

    public static PerformanceCollector getPerformanceCollector() {
        return performanceCollector;
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

