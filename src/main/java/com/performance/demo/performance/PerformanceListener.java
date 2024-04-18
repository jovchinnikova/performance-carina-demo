package com.performance.demo.performance;

import com.google.common.base.Stopwatch;
import com.performance.demo.performance.ios.pojo.EventType;
import com.zebrunner.carina.utils.commons.SpecialKeywords;
import com.zebrunner.carina.webdriver.config.WebDriverConfiguration;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.support.events.WebDriverListener;
import com.zebrunner.carina.webdriver.listener.DriverListener;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.Sequence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;

public class PerformanceListener implements WebDriverListener {

    private static PerformanceCollector performanceCollector;
    private static String flowName;
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private static String elementName;

    /**
     * This method should be used in the beginning of each performance test
     */
    public static void startPerformanceTracking(String flowName, String userName, boolean isCollectLoginTime, boolean isCollectExecutionTime) {
        if (!SpecialKeywords.IOS.equalsIgnoreCase(WebDriverConfiguration.getCapability(CapabilityType.PLATFORM_NAME).get())) {
            try {
                performanceCollector = new AdbPerformanceCollector();
                performanceCollector.setUserName(userName);
                setFlowName(flowName);
                performanceCollector.setLoadTimeStopwatch(Stopwatch.createUnstarted());
                performanceCollector.setCollectLoginTime(isCollectLoginTime);
                performanceCollector.setCollectExecutionTime(isCollectExecutionTime);
                startTracking();
            } catch (Exception e) {
                LOGGER.warn("There was an error during initialization of PerformanceCollector: {}", e.getMessage());
            }
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
        if (performanceCollector.getLoadTimeStopwatch().isRunning()) {
            performanceCollector.collectLoadTime(flowName);
        }
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
        elementName = StringUtils.substringBetween(driverMessage, "element '", " (");
    }

    @Override
    public void afterClick(WebElement element) {
        EventType click = EventType.CLICK;
        String elementName = getElementName();
        if (flowName != null) {
            performanceCollector.collectSnapshotBenchmarks(flowName, click, elementName);
            performanceCollector.collectNetBenchmarks(flowName, click, elementName);
        }
        if (!performanceCollector.getLoadTimeStopwatch().isRunning()) {
            performanceCollector.setLoadTimeStopwatch(Stopwatch.createStarted());
            performanceCollector.setEventElementNames(click, elementName);
        }
    }

    @Override
    public void beforeSendKeys(WebElement element, CharSequence... keysToSend) {
        String driverMessage = DriverListener.getMessage(true);
        elementName = StringUtils.substringBetween(driverMessage, "element '", " (");
    }

    @Override
    public void afterSendKeys(WebElement element, CharSequence... keysToSend) {
        EventType sendKeys = EventType.SEND_KEYS;
        String elementName = getElementName();
        if (flowName != null)
            performanceCollector.collectSnapshotBenchmarks(flowName, sendKeys, elementName);
        if (!performanceCollector.getLoadTimeStopwatch().isRunning()) {
            performanceCollector.setLoadTimeStopwatch(Stopwatch.createStarted());
            performanceCollector.setEventElementNames(sendKeys, elementName);
        }
    }

    // TODO: 03.04.2024 Find a way to get action and element name here:
    @Override
    public void afterPerform(WebDriver driver, Collection<Sequence> actions) {
        String element = "Element";
        String action = "";
        try {
            StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
            action = Arrays.stream(stackTrace)
                    .filter(e -> e.getClassName().contains("IMobileUtils"))
                    .reduce((first, second) -> second)
                    .get().getMethodName();
        } catch (Exception e) {
            LOGGER.warn("There was an error during retrieving action name: {}", e.getMessage());
        }

        LOGGER.info("METHOD NAME: " + action);

        EventType eventType;

        switch (action) {
            case "longTap":
                eventType = EventType.LONG_TAP;
                break;
            case "swipe":
                eventType = EventType.SWIPE;
                break;
            default:
                eventType = EventType.NONE;
                break;
        }

        if (flowName != null) {
            performanceCollector.collectSnapshotBenchmarks(flowName, eventType, element);
            performanceCollector.collectNetBenchmarks(flowName, eventType, element);
        }
    }


    @Override
    public void beforeAnyWebElementCall(WebElement element, Method method, Object[] args) {
        if (performanceCollector.getLoadTimeStopwatch().isRunning()) {
            performanceCollector.collectLoadTime(flowName);
        }
    }

    private static void startTracking() {
        if (flowName != null) {
            if (performanceCollector.isCollectLoginTime() && performanceCollector.isCollectExecutionTime()) {
                Stopwatch stopwatch = Stopwatch.createStarted();
                performanceCollector.setLoginStopwatch(stopwatch);
                performanceCollector.setExecutionStopwatch(stopwatch);
            } else if (performanceCollector.isCollectLoginTime())
                performanceCollector.setLoginStopwatch(Stopwatch.createStarted());
            else if (performanceCollector.isCollectExecutionTime())
                performanceCollector.setExecutionStopwatch(Stopwatch.createStarted());

            performanceCollector.collectNetData();
        }
    }

    public static String getElementName() {
        String element = elementName;
        elementName = null;
        return element;
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
}
