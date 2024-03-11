package com.performance.demo.performance;

import com.google.common.base.Stopwatch;
import com.performance.demo.performance.ios.DBService;
import com.performance.demo.performance.ios.IosPerformanceCollector;
import com.performance.demo.performance.ios.pojo.EventType;
import com.performance.demo.performance.ios.pojo.TestEvent;
import com.zebrunner.carina.utils.commons.SpecialKeywords;
import com.zebrunner.carina.webdriver.config.WebDriverConfiguration;
import com.zebrunner.carina.webdriver.listener.DriverListener;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.support.events.WebDriverListener;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class PerformanceListener implements WebDriverListener {

    private static PerformanceCollector performanceCollector;
    private static String flowName;
    private static List<TestEvent> testEvents = new ArrayList<>();
    private static String elementName;

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
    public static void stopPerformanceTracking() {
        if (flowName != null) {
            if (performanceCollector.isCollectLoginTime() && !performanceCollector.isCollectExecutionTime())
                performanceCollector.collectLoginTime(flowName);
            else if (performanceCollector.isCollectExecutionTime())
                performanceCollector.collectExecutionTime(flowName);

            performanceCollector.collectAndWritePerformance(flowName);
        }
    }

    @Override
    public void beforeClick(WebElement element) {
        String driverMessage = DriverListener.getMessage(true);
        String elementName = StringUtils.substringBetween(driverMessage, "element '", " (");
        setElementName(elementName);
    }

    @Override
    public void afterClick(WebElement element) {
        TestEvent testEvent = new TestEvent(EventType.CLICK, elementName, Instant.now());
        testEvents.add(testEvent);
        DBService.writeEvent(testEvent);
        if (flowName != null)
            performanceCollector.collectSnapshotBenchmarks(flowName);
    }

    @Override
    public void beforeSendKeys(WebElement element, CharSequence... keysToSend) {
        String driverMessage = DriverListener.getMessage(true);
        String elementName = StringUtils.substringBetween(driverMessage, "element '", " (");
        setElementName(elementName);
    }

    @Override
    public void afterSendKeys(WebElement element, CharSequence... keysToSend) {
        if (flowName != null)
            performanceCollector.collectSnapshotBenchmarks(flowName);
        TestEvent testEvent = new TestEvent(EventType.SEND_KEYS, elementName, Instant.now());
        testEvents.add(testEvent);
        DBService.writeEvent(testEvent);
    }

    @Override
    public void beforeGet(WebDriver driver, String url) {
        IosPerformanceCollector.startCollecting();
    }

    private static void startTracking() {
        if (flowName != null) {
            if (performanceCollector.isCollectLoginTime() && performanceCollector.isCollectExecutionTime()) {
                Stopwatch stopwatch = Stopwatch.createStarted();
                performanceCollector.setLoginStopwatch(stopwatch);
                performanceCollector.setExecutionStopWatch(stopwatch);
            } else if (performanceCollector.isCollectLoginTime())
                performanceCollector.setLoginStopwatch(Stopwatch.createStarted());
            else if (performanceCollector.isCollectExecutionTime())
                performanceCollector.setExecutionStopWatch(Stopwatch.createStarted());

            performanceCollector.collectNetBenchmarks();
        }
    }

    public static PerformanceCollector getPerformanceCollector() {
        return performanceCollector;
    }

    public static void setPerformanceCollector(PerformanceCollector performanceCollector) {
        PerformanceListener.performanceCollector = performanceCollector;
    }

    public static String getFlowName() {
        return flowName;
    }

    public static void setFlowName(String flowName) {
        PerformanceListener.flowName = flowName;
    }

    public static List<TestEvent> getTestEvents() {
        return testEvents;
    }

    public static void setElementName(String elementName) {
        PerformanceListener.elementName = elementName;
    }
}
