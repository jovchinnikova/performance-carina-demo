package com.performance.demo.utils;

import com.performance.demo.performance.PerformanceCollector;
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

    private final PerformanceCollector performanceCollector = PerformanceListener.getPerformanceCollector();

    private String generateDashboardUrl(DashboardType dashboardType) {
        String urlTemplate = dashboardType.getUrlTemplate();
        Map<String, Object> params = new HashMap<>();
        if (DashboardType.TEST_DASHBOARD.equals(dashboardType)) {
            GrafanaLinkParameter.START_TIME.setValue(performanceCollector.getBeginEpochMilli());
            GrafanaLinkParameter.END_TIME.setValue(performanceCollector.getEndEpochMilli());
        }
        GrafanaLinkParameter.OS_VERSION.setValue(performanceCollector.getDevice().getOsVersion());
        GrafanaLinkParameter.DEVICE_NAME.setValue(performanceCollector.getDevice().getName());
        GrafanaLinkParameter.USER.setValue(performanceCollector.getUserName());
        GrafanaLinkParameter.TEST_ID.setValue(CurrentTest.getId().orElse(0L));
        GrafanaLinkParameter.RUN_ID.setValue(CurrentTestRun.getId().orElse(0L));
        GrafanaLinkParameter.FLOW.setValue(PerformanceListener.getFlowName());
        for (GrafanaLinkParameter parameter : GrafanaLinkParameter.values()) {
            if (!parameter.getValue().equals(0.0) && !parameter.getValue().equals(""))
                params.put(parameter.getName(), parameter.getValue());
        }
        return StringSubstitutor.replace(urlTemplate, params, "${", "}");

    }

    public void attachPerformanceLinkToTest() {
        if (ATTACH_LINKS && performanceCollector.isMatchCount()) {
            String testDashboardUrl = generateDashboardUrl(DashboardType.TEST_DASHBOARD);
            LOGGER.info("TEST DASHBOARD URL: {}", testDashboardUrl);
            Artifact.attachReferenceToTest(PERFORMANCE_DASHBOARD, testDashboardUrl);
        }
    }

    public void attachPerformanceLinkToTestRun() {
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
