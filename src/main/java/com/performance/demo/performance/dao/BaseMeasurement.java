package com.performance.demo.performance.dao;

import com.influxdb.annotations.Column;
import com.zebrunner.agent.core.registrar.CurrentTest;
import com.zebrunner.agent.core.registrar.CurrentTestRun;
import com.zebrunner.carina.utils.R;
import com.zebrunner.carina.webdriver.IDriverPool;

import java.time.Instant;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// class for avoiding writeMeasurement() method duplication
// for each type of measurement we want to persist
public class BaseMeasurement implements IDriverPool {

    @Column(tag = true, name = "os_version")
    private String osVersion;

    @Column(tag = true, name = "app_version")
    private String appVersion;

    @Column(tag = true, name = "device_name")
    private String deviceName;

    @Column(tag = true, name = "platform_name")
    private String platformName;

    @Column(tag = true, name = "flow_id")
    private String flowName;

    @Column(tag = true, name = "env")
    private String env;

    @Column(timestamp = true)
    private Instant time;

    @Column(tag = true, name = "username")
    private String userName;

    @Column(tag = true, name = "run_id")
    private Long runId;

    @Column(tag = true, name = "test_id")
    private Long testId;


    public BaseMeasurement(String flowName, Instant time, String userName) {
        this.osVersion = getDevice().getOsVersion();
        this.appVersion = cutAppVersionIfNecessary();
        this.deviceName = getDevice().getName();
        this.platformName = R.CONFIG.get("capabilities.platformName").toUpperCase();
        this.flowName = flowName;
        this.env = R.CONFIG.get("env");
        this.time = time;
        this.userName = userName;
        this.runId = CurrentTestRun.getId().orElse(0L);
        this.testId = CurrentTest.getId().orElse(0L);
    }

    public static String cutAppVersionIfNecessary() {
        String appVersionRegex = R.TESTDATA.get("app_version_regex");
        String version = R.CONFIG.get("app_version");
        String result = version;
        if (!appVersionRegex.isEmpty()) {
            Pattern appVersionPattern = Pattern.compile(appVersionRegex);
            Matcher matcher = appVersionPattern.matcher(version);
            if (matcher.matches()) {
                result = matcher.group(1);
            }
        }
        return result;
    }

    public String getOsVersion() {
        return osVersion;
    }

    public void setOsVersion(String osVersion) {
        this.osVersion = osVersion;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getPlatformName() {
        return platformName;
    }

    public void setPlatformName(String platformName) {
        this.platformName = platformName;
    }

    public String getFlowName() {
        return flowName;
    }

    public void setFlowName(String flowName) {
        this.flowName = flowName;
    }

    public String getEnv() {
        return env;
    }

    public void setEnv(String env) {
        this.env = env;
    }

    public Instant getTime() {
        return time;
    }

    public void setTime(Instant time) {
        this.time = time;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Long getRunId() {
        return runId;
    }

    public void setRunId(Long runId) {
        this.runId = runId;
    }

    public Long getTestId() {
        return testId;
    }

    public void setTestId(Long testId) {
        this.testId = testId;
    }

}
