package com.performance.demo.performance.android;

import com.google.common.base.Stopwatch;
import com.performance.demo.performance.android.dao.Flow;
import com.performance.demo.utils.parser.NetParser;
import com.zebrunner.agent.core.registrar.Artifact;
import com.zebrunner.agent.core.registrar.CurrentTest;
import com.zebrunner.agent.core.registrar.CurrentTestRun;
import com.zebrunner.carina.utils.Configuration;
import com.zebrunner.carina.utils.R;
import com.zebrunner.carina.utils.commons.SpecialKeywords;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.events.WebDriverListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;

public class PerformanceListener implements WebDriverListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private static Flow flow;
    private static PerformanceData performanceData;

    private static final boolean ATTACH_LINKS = Boolean.parseBoolean(R.TESTDATA.get("attach_grafana_links"));
    private static final String RUN_URL = "%s/d/eIzJir4Vk/multiple_flows?orgId=2&var-run_id=%s";
    private static final String GRAFANA_TOKEN = R.TESTDATA.getDecrypted("grafana_token");
    private static final String GRAFANA_HOST = R.TESTDATA.get("grafana_host");
    private static final String TEST_URL = "%s/d/Fg_sTfYnk/all_flows?orgId=2&from=%s&to=%s&var-app_version=%s&var-os_version=%s" +
            "&var-platform_name=%s&var-env=%s&var-device_name=%s&var-flow_id=%s&var-user=%s";

    private static Long runId;
    private static Long testId;

    /**
     * This method should be used in the beginning of each performance test
     */
    public static void startPerformanceTracking(Flow flow, String userName) {
        if (!SpecialKeywords.IOS.equalsIgnoreCase(Configuration.getPlatform())) {
            performanceData = new PerformanceData();
            performanceData.setUserName(userName);
            setFlow(flow);
            runId = CurrentTestRun.getId().orElse(0L);
            testId = CurrentTest.getId().orElse(0L);
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
            attachPerformanceLinkToTest();
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

    private static void attachPerformanceLinkToTest() {
        if(ATTACH_LINKS && performanceData.isMatchCount()) {
            String dashboardUrl = generateDashboardUrl();
            LOGGER.info("DASHBOARD URL: {}", dashboardUrl);
            if (runId != 0 && testId != 0)
                Artifact.attachReferenceToTest("Performance dashboard", dashboardUrl);
        } else {
            LOGGER.warn("Not all performance data were received during test execution");
        }
    }

    public static void attachPerformanceLinkToTestRun() {
        if (ATTACH_LINKS)
        Artifact.attachReferenceToTestRun("Performance dashboard", String.format(RUN_URL, GRAFANA_HOST, runId));
    }

    private static String generateDashboardUrl() {
     long beginEpochMilli = performanceData.getBeginEpochMilli();
     long endEpochMilli = performanceData.getEndEpochMilli();
     String appVersion = R.CONFIG.get("app_version");
     String deviceVersion = performanceData.getDevice().getOsVersion();
     String platformName = R.CONFIG.get("capabilities.platformName").toUpperCase();
     String env = R.CONFIG.get("env");
     String deviceName = performanceData.getDevice().getName();
     String userName = performanceData.getUserName();
        return String.format(TEST_URL, GRAFANA_HOST, beginEpochMilli, endEpochMilli, appVersion, deviceVersion,
                platformName, env, deviceName, flow.getName(), userName);
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

