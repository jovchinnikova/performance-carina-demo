package com.performance.demo.performance;

import com.google.common.base.Stopwatch;
import com.performance.demo.utils.parser.NetParser;
import com.zebrunner.agent.core.registrar.Artifact;
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

    private static final String PERFORMANCE_DASHBOARD = "Performance dashboard";
    private static final boolean ATTACH_LINKS = Boolean.parseBoolean(R.TESTDATA.get("attach_grafana_links"));
    private static final String RUN_URL = R.TESTDATA.get("grafana_run_url");
    private static final String GRAFANA_TOKEN = R.TESTDATA.getDecrypted("grafana_token");
    private static final String GRAFANA_HOST = R.TESTDATA.get("grafana_host");
    private static final String TEST_URL = R.TESTDATA.get("grafana_test_url");

    private static Long runId;

    private static String flowName;
    private static PerformanceData performanceData;

    /**
     * This method should be used in the beginning of each performance test
     */
    public static void startPerformanceTracking(String flowName, String userName, boolean isCollectLogin, boolean isCollectExecution) {
        if (!SpecialKeywords.IOS.equalsIgnoreCase(Configuration.getPlatform())) {
            performanceData = new PerformanceData();
            performanceData.setUserName(userName);
            setFlowName(flowName);
            runId = CurrentTestRun.getId().orElse(0L);
            performanceData.setCollectLogin(isCollectLogin);
            performanceData.setCollectExecution(isCollectExecution);
            startTracking();
        }
    }

    /**
     * This method is used in authService.loginByUsernameWithPerf
     */
    public static void collectLoginTime() {
        if (flowName != null && (performanceData.isCollectLogin() && performanceData.isCollectExecution()))
            performanceData.collectLoginTime(flowName);
    }

    /**
     * This method should be used in the end of each performance test
     */
    public static void collectPerfBenchmarks() {
        if (flowName != null) {
            if (performanceData.isCollectLogin() && !performanceData.isCollectExecution()) {
                performanceData.collectLoginTime(flowName);
                performanceData.collectBenchmarks(flowName);
            } else if (performanceData.isCollectExecution()) {
                performanceData.collectExecutionTime(flowName);
                performanceData.collectBenchmarks(flowName);
            }
            attachPerformanceLinkToTest();
        }
    }

    @Override
    public void afterClick(WebElement element) {
        if (flowName != null)
            performanceData.collectSnapshotBenchmarks(flowName);
    }

    @Override
    public void afterSendKeys(WebElement element, CharSequence... keysToSend) {
        if (flowName != null)
            performanceData.collectSnapshotBenchmarks(flowName);
    }

    private static void startTracking() {
        if (flowName != null) {
            if (performanceData.isCollectLogin() && !performanceData.isCollectExecution())
                performanceData.setLoginStopwatch(Stopwatch.createStarted());
            else if (!performanceData.isCollectLogin() && performanceData.isCollectExecution())
                performanceData.setExecutionStopWatch(Stopwatch.createStarted());
            else if (performanceData.isCollectLogin() && performanceData.isCollectExecution()){
                Stopwatch stopwatch = Stopwatch.createStarted();
                performanceData.setLoginStopwatch(stopwatch);
                performanceData.setExecutionStopWatch(stopwatch);
            }

            NetParser.NetRow row = (NetParser.NetRow) performanceData.collectNetBenchmarks();
            performanceData.setRowStart(row);
        }
    }

    private static void attachPerformanceLinkToTest() {
        if (ATTACH_LINKS && performanceData.isMatchCount()) {
            String dashboardUrl = generateDashboardUrl();
            LOGGER.info("DASHBOARD URL: {}", dashboardUrl);
            Artifact.attachReferenceToTest(PERFORMANCE_DASHBOARD, dashboardUrl);
        }
    }

    public static void attachPerformanceLinkToTestRun() {
        if (ATTACH_LINKS)
            Artifact.attachReferenceToTestRun(PERFORMANCE_DASHBOARD, String.format(RUN_URL, GRAFANA_HOST, runId));
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
                platformName, env, deviceName, flowName, userName);
    }

    public PerformanceData getPerformanceData() {
        return performanceData;
    }

    public static String getFlowName() {
        return flowName;
    }

    public static void setFlowName(String flowName) {
        PerformanceListener.flowName = flowName;
    }
}

