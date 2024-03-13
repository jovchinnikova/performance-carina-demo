package com.performance.demo;

import com.performance.demo.performance.ios.IosPerformanceCollector;
import com.performance.demo.performance.ios.IosPerformanceListener;
import com.performance.demo.performance.ios.pojo.Performance;
import com.zebrunner.carina.core.IAbstractTest;
import com.zebrunner.carina.utils.R;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

import java.lang.reflect.Method;

public interface IosPerformanceTest extends IAbstractTest {

    IosPerformanceCollector performanceCollector = IosPerformanceListener.getPerformanceCollector();

    @BeforeSuite
    default void setPerformanceListener() {
        R.CONFIG.put("driver_event_listeners", "com.performance.demo.performance.ios.IosPerformanceListener");
    }

    @BeforeMethod
    default void startCollecting() {
        getDriver();
        performanceCollector.startCollecting();
    }

    @AfterMethod
    default void collectMetrics(Method method) {
        String response = performanceCollector.stopCollecting();
        Performance performance = performanceCollector.createPerformanceObject(response);
        performance.addTestEvents(IosPerformanceListener.getTestEvents(), method.getName());
        performanceCollector.writePerformanceToDB();
    }
}
