package com.performance.demo;

import com.performance.demo.annotations.PerformanceTest;
import com.performance.demo.performance.PerformanceListener;
import com.performance.demo.utils.AOPUtil;
import com.performance.demo.utils.GrafanaUtil;
import com.zebrunner.carina.core.IAbstractTest;
import com.zebrunner.carina.utils.R;
import org.testng.ITestResult;
import org.testng.annotations.*;

import java.lang.reflect.Method;

public interface IPerformanceTest extends IAbstractTest {

    void setConfig(Object service);

    @BeforeSuite
    default void setPerformanceListener() {
        R.CONFIG.put("driver_event_listeners", "com.performance.demo.performance.PerformanceListener");
    }

    @BeforeMethod
    default void startTrackingPerformance(Method method) {
        if (method.isAnnotationPresent(PerformanceTest.class)) {
            PerformanceTest annotation = method.getAnnotation(PerformanceTest.class);
            if (annotation.collectLoginTime() && annotation.collectExecutionTime()) {
                if ("".equals(annotation.loginMethodName()))
                    throw new RuntimeException("LoginMethodName should be added to @PerformanceTest to collect login time");
                PerformanceListener.setLoginMethodName(annotation.loginMethodName());
                Object loginService = AOPUtil.setAopConfiguration(PerformanceListener.getLoginMethodName());
                setConfig(loginService);
            }
            PerformanceListener.startPerformanceTracking(annotation.flowName(), annotation.userName(),
                    annotation.collectLoginTime(), annotation.collectExecutionTime());
        } else {
            throw new RuntimeException("Performance test should have annotation @PerformanceTest");
        }
    }

    @AfterMethod
    default void collectPerformance(ITestResult iTestResult, Method method) {
        if (iTestResult.getStatus() == 1 && method.isAnnotationPresent(PerformanceTest.class)) {
            PerformanceListener.collectPerfBenchmarks();
            new GrafanaUtil().attachPerformanceLinkToTest();
        }
    }

    @AfterSuite
    default void attachPerformanceLinkToTestRun() {
        new GrafanaUtil().attachPerformanceLinkToTestRun();
    }
}
