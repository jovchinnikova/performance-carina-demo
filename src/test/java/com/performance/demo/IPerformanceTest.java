package com.performance.demo;

import com.performance.demo.annotations.PerformanceTest;
import com.performance.demo.performance.PerformanceListener;
import com.zebrunner.carina.core.IAbstractTest;
import com.zebrunner.carina.utils.R;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

import java.lang.reflect.Method;

public interface IPerformanceTest extends IAbstractTest {

    @BeforeSuite
    default void setPerformanceListener() {
        R.CONFIG.put("driver_event_listeners", "com.performance.demo.performance.PerformanceListener");
    }

    @BeforeMethod
    default void startTrackingPerformance(Method method) {
        if (method.isAnnotationPresent(PerformanceTest.class)) {
            PerformanceTest annotation = method.getAnnotation(PerformanceTest.class);
            PerformanceListener.startPerformanceTracking(annotation.flowName(), annotation.userName(),
                    annotation.collectLoginTime(), annotation.collectExecutionTime());
        } else {
            throw new RuntimeException("Performance test should have annotation @PerformanceTest");
        }
    }

    @AfterMethod
    default void collectPerformance(ITestResult iTestResult) {
        if (iTestResult.getStatus() == 1)
            PerformanceListener.collectPerfBenchmarks();
    }

    @AfterSuite
    default void attachPerformanceLinkToTestRun() {
        PerformanceListener.attachPerformanceLinkToTestRun();
    }
}
