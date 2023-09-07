package com.performance.demo.utils;

import com.performance.demo.performance.PerformanceData;
import com.performance.demo.performance.PerformanceListener;
import com.zebrunner.agent.core.registrar.Artifact;
import com.zebrunner.agent.core.registrar.CurrentTest;
import com.zebrunner.agent.core.registrar.CurrentTestRun;
import com.zebrunner.carina.utils.R;
import org.apache.commons.text.StringSubstitutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.util.HashMap;
import java.util.Map;

public class GrafanaUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private static final String PERFORMANCE_DASHBOARD = "Performance dashboard";
    private static final boolean ATTACH_LINKS = Boolean.parseBoolean(R.TESTDATA.get("attach_grafana_links"));
    private static final String RUN_URL = R.TESTDATA.get("grafana_run_url");
    private static final String TEST_URL = R.TESTDATA.get("grafana_test_url");

    private static final PerformanceData performanceData = PerformanceListener.getPerformanceData();
    private static final String flowName = PerformanceListener.getFlowName();
    private static final long runId = CurrentTestRun.getId().orElse(0L);
    private static final long testId = CurrentTest.getId().orElse(0L);

    private static String generateDashboardUrl(DashboardType dashboardType) {
        String urlTemplate = dashboardType.getUrlTemplate();
        Map<String, Object> params = new HashMap<>();
        if (DashboardType.TEST_DASHBOARD.equals(dashboardType)) {
            GrafanaLinkParameter.START_TIME.setValue(performanceData.getBeginEpochMilli());
            GrafanaLinkParameter.END_TIME.setValue(performanceData.getEndEpochMilli());
        }
        GrafanaLinkParameter.OS_VERSION.setValue(performanceData.getDevice().getOsVersion());
        GrafanaLinkParameter.DEVICE_NAME.setValue(performanceData.getDevice().getName());
        GrafanaLinkParameter.USER.setValue(performanceData.getUserName());
        GrafanaLinkParameter.TEST_ID.setValue(testId);
        GrafanaLinkParameter.RUN_ID.setValue(runId);
        GrafanaLinkParameter.FLOW.setValue(flowName);
        for (GrafanaLinkParameter parameter: GrafanaLinkParameter.values()) {
            if (!parameter.getValue().equals(0.0) && !parameter.getValue().equals(""))
                params.put(parameter.getName(), parameter.getValue());
        }
        return StringSubstitutor.replace(urlTemplate, params, "${", "}");

    }

    public static void attachPerformanceLinkToTest() {
        if (ATTACH_LINKS && performanceData.isMatchCount()) {
            String testDashboardUrl = generateDashboardUrl(DashboardType.TEST_DASHBOARD);
            LOGGER.info("TEST DASHBOARD URL: {}", testDashboardUrl);
            Artifact.attachReferenceToTest(PERFORMANCE_DASHBOARD, testDashboardUrl);
        }
    }

    public static void attachPerformanceLinkToTestRun() {
        if (ATTACH_LINKS) {
            String runDashboardUrl = generateDashboardUrl(DashboardType.RUN_DASHBOARD);
            Artifact.attachReferenceToTestRun(PERFORMANCE_DASHBOARD, runDashboardUrl);
        }
    }

    private enum DashboardType {

        TEST_DASHBOARD("test", TEST_URL), RUN_DASHBOARD("run", RUN_URL);

        private final String type;
        private final String urlTemplate;

        DashboardType(String type, String template) {
            this.type = type;
            this.urlTemplate = template;
        }

        public String getType() {
            return type;
        }

        public String getUrlTemplate() {
            return urlTemplate;
        }
    }

}
