package com.performance.demo.performance.dao;

import com.influxdb.annotations.Column;
import com.influxdb.annotations.Measurement;

import java.time.Instant;

@Measurement(name = "load_time")
public class LoadTime extends BaseMeasurement{

    @Column(tag = true, name = "_actionName")
    private String actionName;

    @Column(name = "value")
    private Double loadTime;

    public LoadTime(Double loadTime, String flowName, Instant time, String userName, String actionName) {
        super(flowName, time, userName);
        this.actionName = actionName;
        this.loadTime = loadTime;
    }

    public Double getLoadTime() {
        return loadTime;
    }

    public void setLoadTime(Double loadTime) {
        this.loadTime = loadTime;
    }

}
