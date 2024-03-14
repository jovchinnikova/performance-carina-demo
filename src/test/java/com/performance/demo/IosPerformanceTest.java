package com.performance.demo;

import com.performance.demo.performance.ios.IosPerformanceCollector;
import com.performance.demo.performance.ios.IosPerformanceListener;
import com.performance.demo.performance.ios.pojo.Performance;
import com.zebrunner.carina.core.IAbstractTest;
import com.zebrunner.carina.utils.R;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestContext;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

import java.lang.invoke.MethodHandles;
import java.lang.reflect.Method;
import java.time.Instant;

public interface IosPerformanceTest extends IAbstractTest {

    Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    IosPerformanceCollector performanceCollector = IosPerformanceListener.getPerformanceCollector();
    String UNFORMATTED_LINK = "%s/d/%s/%s?orgId=2&from=%s&to=%s&var-platform_name=%s&var-os_version=%s&var-device_name=%s&var-flow_id=%s";

    @BeforeSuite
    default void setPerformanceListener() {
        R.CONFIG.put("driver_event_listeners", "com.performance.demo.performance.ios.IosPerformanceListener");
    }

    @BeforeMethod
    default void startCollecting(ITestContext context) {
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

    @AfterMethod
    default void printLink(ITestContext context, Method method) {
        for (IosMetric iosMetric : IosMetric.values()) {
            LOGGER.info(iosMetric.getMetricName() + " dashboard");
            LOGGER.info(iosMetric.formatLink(context, method));
        }
    }

    enum IosMetric {
        ENERGY("energy", "bb7bda5e-bf6d-432e-a9bd-5f0d92ecf149"),
        NETSTAT("netstat", "d3462317-c850-4866-8ff5-b114cfa9000a"),
        SYSMON("sysmon-monitor", "f2ccbbc2-899f-4ea2-972b-2da4f6d4488b"),
        GRAPHICS("graphics", "e489b067-4c4c-489d-85e3-d047e320942a");

        private String metricName;
        private String dash_id;

        IosMetric(String metricName, String dash_id) {
            this.metricName = metricName;
            this.dash_id = dash_id;
        }

        public String getMetricName() {
            return metricName;
        }

        public String getDash_id() {
            return dash_id;
        }

        public String formatLink(ITestContext context, Method method) {
            String startTime = String.valueOf(context.getStartDate().getTime());
            String endTime = String.valueOf(Instant.now().toEpochMilli());
            String platformName = R.CONFIG.get("capabilities.platformName").toUpperCase();
            String osVersion = R.CONFIG.get("capabilities.platformVersion");
            String deviceName = R.CONFIG.get("capabilities.deviceName");
            String host = R.TESTDATA.get("grafana_host");
            String flowId = method.getName();
            return String.format(UNFORMATTED_LINK, host, dash_id, metricName, startTime, endTime, platformName, osVersion, deviceName, flowId);
        }
    }
}
