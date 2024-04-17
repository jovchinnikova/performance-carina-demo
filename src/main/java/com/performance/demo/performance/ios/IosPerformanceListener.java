package com.performance.demo.performance.ios;

import com.performance.demo.performance.ios.pojo.EventType;
import com.performance.demo.performance.ios.pojo.TestEvent;
import com.zebrunner.carina.webdriver.listener.DriverListener;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.events.WebDriverListener;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class IosPerformanceListener implements WebDriverListener {

    private static final IosPerformanceCollector performanceCollector = new IosPerformanceCollector();
    private static final List<TestEvent> testEvents = new ArrayList<>();
    private static String elementName;

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
    }

    @Override
    public void beforeSendKeys(WebElement element, CharSequence... keysToSend) {
        String driverMessage = DriverListener.getMessage(true);
        String elementName = StringUtils.substringBetween(driverMessage, "element '", " (");
        setElementName(elementName);
    }

    @Override
    public void afterSendKeys(WebElement element, CharSequence... keysToSend) {
        TestEvent testEvent = new TestEvent(EventType.SEND_KEYS, elementName, Instant.now());
        testEvents.add(testEvent);
    }

    public static List<TestEvent> getTestEvents() {
        return testEvents;
    }

    public void setElementName(String elementName) {
        this.elementName = elementName;
    }

    public static IosPerformanceCollector getPerformanceCollector() {
        return performanceCollector;
    }
}
