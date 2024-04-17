package com.performance.demo;

import com.performance.demo.annotations.PerformanceTest;
import com.performance.demo.performance.PerformanceListener;
import com.performance.demo.performance.ios.IosPerformanceCollector;
import com.performance.demo.performance.ios.IosPerformanceListener;
import com.performance.demo.utils.GrafanaLinkParameter;
import com.performance.demo.utils.GrafanaUtil;
import com.zebrunner.carina.core.IAbstractTest;
import com.zebrunner.carina.utils.R;
import org.apache.commons.text.StringSubstitutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

import java.lang.invoke.MethodHandles;
import java.lang.reflect.Method;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

public interface IPerformanceTest extends IAbstractTest {

    Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    IosPerformanceCollector performanceCollector = IosPerformanceListener.getPerformanceCollector();
    boolean isAndroid = "android".equalsIgnoreCase(R.CONFIG.get("capabilities.platformName"));

    @BeforeSuite
    default void setPerformanceListener() {
        if (isAndroid) {
            R.CONFIG.put("driver_event_listeners", PerformanceListener.class.getName());
        } else {
            R.CONFIG.put("driver_event_listeners", IosPerformanceListener.class.getName());
        }
    }

    @BeforeMethod
    default void startTrackingPerformance(Method method) {
        if (isAndroid) {
            startTrackingAndroid(method);
        } else {
            startTrackingIos(method);
        }
    }

    @AfterMethod
    default void collectPerformance(ITestContext context, ITestResult iTestResult, Method method) {
        if (!method.isAnnotationPresent(PerformanceTest.class)) {
            throw new RuntimeException("Performance test should have annotation @PerformanceTest");
        }
        if (isAndroid) {
            collectPerformanceAndroid(iTestResult, method);
        } else {
            collectPerformanceIos(context);
        }
    }

    @AfterSuite
    default void attachPerformanceLinkToTestRun() {
        if (isAndroid)
            new GrafanaUtil().attachPerformanceLinkToTestRun();
    }

    private void startTrackingAndroid(Method method) {
        PerformanceTest annotation = method.getAnnotation(PerformanceTest.class);
        String flowName;
        if (annotation.flowName().isEmpty()) {
            flowName = method.getName();
        } else {
            flowName = annotation.flowName();
        }
        PerformanceListener.startPerformanceTracking(flowName, annotation.userName(),
                annotation.collectLoginTime(), annotation.collectExecutionTime());
    }

    private void startTrackingIos(Method method) {
        PerformanceTest annotation = method.getAnnotation(PerformanceTest.class);
        String flowName;
        if (annotation.flowName().isEmpty()) {
            flowName = method.getName();
        } else {
            flowName = annotation.flowName();
        }
        getDriver();
        performanceCollector.startCollecting(flowName, annotation.userName());
    }

    private void collectPerformanceAndroid(ITestResult iTestResult, Method method) {
        if (iTestResult.getStatus() == 1 && method.isAnnotationPresent(PerformanceTest.class)) {
            PerformanceListener.stopPerformanceTracking();
            new GrafanaUtil().attachPerformanceLinkToTest();
        }
    }

    private void collectPerformanceIos(ITestContext context) {
        String response = performanceCollector.stopCollecting();
        performanceCollector.createPerformanceObject(response);
        performanceCollector.writePerformanceToDB();
        printLink(context);
    }

    private void printLink(ITestContext context) {
        for (IosMetric iosMetric : IosMetric.values()) {
            LOGGER.info(iosMetric.getMetricName() + " dashboard");
            LOGGER.info(iosMetric.formatLink(context));
        }
    }

    enum IosMetric {
        ENERGY("energy"),
        NETSTAT("netstat"),
        SYSMON("sysmon-monitor"),
        GRAPHICS("graphics");

        private final String metricName;

        IosMetric(String metricName) {
            this.metricName = metricName;
        }

        public String getMetricName() {
            return metricName;
        }

        public String formatLink(ITestContext context) {
            String unformattedLink = R.TESTDATA.get("grafana_ios_url");
            Map<String, Object> params = new HashMap<>();
            GrafanaLinkParameter.START_TIME.setValue(context.getStartDate().getTime());
            GrafanaLinkParameter.END_TIME.setValue(Instant.now().toEpochMilli());
            GrafanaLinkParameter.OS_VERSION.setValue(performanceCollector.getDevice().getOsVersion());
            GrafanaLinkParameter.DEVICE_NAME.setValue(performanceCollector.getDevice().getName());
            GrafanaLinkParameter.METRIC_NAME.setValue(metricName);
            GrafanaLinkParameter.USER.setValue(performanceCollector.getUserName());
            GrafanaLinkParameter.FLOW.setValue(performanceCollector.getFlowName());
            for (GrafanaLinkParameter parameter : GrafanaLinkParameter.values()) {
                if (!parameter.getValue().equals(0.0) && !parameter.getValue().equals(""))
                    params.put(parameter.getName(), parameter.getValue());
            }
            return StringSubstitutor.replace(unformattedLink, params, "${", "}");
        }
    }
}
