package com.performance.demo.performance.android;

import com.google.common.base.Stopwatch;
import com.performance.demo.performance.android.dao.Flow;
import com.performance.demo.utils.parser.NetParser;
import com.zebrunner.carina.utils.Configuration;
import com.zebrunner.carina.utils.commons.SpecialKeywords;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.events.WebDriverListener;

public class PerformanceListener implements WebDriverListener {

    private static Flow flow;
    private static PerformanceData performanceData;

    /**
     * This method should be used in the beginning of each performance test
     */
    public static void startPerformanceTracking(Flow flow, String userName) {
        if (!SpecialKeywords.IOS.equalsIgnoreCase(Configuration.getPlatform())) {
            performanceData = new PerformanceData();
            performanceData.setUserName(userName);
            setFlow(flow);
            startTracking();
        }
    }

    /**
     * This method is used in authService.loginByUsernameWithPerf
     */
    public static void collectLoginTime() {
        if (flow != null && !Flow.LOGIN_FLOW.equals(flow))
            performanceData.collectLoginTime(flow.getName());
    }

    /**
     * This method should be used in the end of each performance test
     */
    public static void collectPerfBenchmarks() {
        if (flow != null) {
            if (Flow.LOGIN_FLOW.equals(flow)) {
                performanceData.collectLoginTime(flow.getName());
                performanceData.collectBenchmarks(flow.getName());
            } else {
                performanceData.collectExecutionTime(flow.getName());
                performanceData.collectBenchmarks(flow.getName());
            }
        }
    }

    @Override
    public void afterClick(WebElement element) {
        if (flow != null)
            performanceData.collectSnapshotBenchmarks(flow.getName());
    }

    @Override
    public void afterSendKeys(WebElement element, CharSequence... keysToSend) {
        if (flow != null)
            performanceData.collectSnapshotBenchmarks(flow.getName());
    }

    private static void startTracking() {
        if (flow != null) {
            if (Flow.LOGIN_FLOW.equals(flow))
                performanceData.setLoginStopwatch(Stopwatch.createStarted());
            else if (Flow.SIGN_UP_FLOW.equals(flow))
                performanceData.setExecutionStopWatch(Stopwatch.createStarted());
            else {
                Stopwatch stopwatch = Stopwatch.createStarted();
                performanceData.setLoginStopwatch(stopwatch);
                performanceData.setExecutionStopWatch(stopwatch);
            }

            NetParser.NetRow row = (NetParser.NetRow) performanceData.collectNetBenchmarks();
            performanceData.setRowStart(row);
        }
    }

    public PerformanceData getPerformanceData() {
        return performanceData;
    }

    public static Flow getFlow() {
        return flow;
    }

    public static void setFlow(Flow flow) {
        PerformanceListener.flow = flow;
    }
}

