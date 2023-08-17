package com.performance.demo.utils;

import com.performance.demo.performance.dao.BaseMeasurement;
import com.zebrunner.carina.utils.R;

public enum GrafanaLinkParameter {

    HOST("host", R.TESTDATA.get("grafana_host")), START_TIME("start_time", 0.0),
    END_TIME("end_time", 0.0), APP_VERSION("app_version", BaseMeasurement.cutAppVersionIfNecessary()),
    PLATFORM("platform", R.CONFIG.get("capabilities.platformName").toUpperCase()), ENV("environment", R.CONFIG.get("env")),
    DEVICE_NAME("device_name", ""), OS_VERSION("os_version", ""), FLOW("flow", ""),
    USER("user", ""), ORG_ID("org", R.TESTDATA.get("grafana_org_id")), TEST_ID("test_id", 0),
    RUN_ID("run_id", 0);

    private String name;
    private Object value;

    GrafanaLinkParameter(String name, Object value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
}
