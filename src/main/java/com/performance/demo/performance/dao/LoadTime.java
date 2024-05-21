package com.performance.demo.performance.dao;

import com.influxdb.annotations.Column;
import com.influxdb.annotations.Measurement;

import java.time.Instant;

@Measurement(name = "load_time")
public class LoadTime extends BaseMeasurement{

    @Column(name = "value")
    private Double value;

    public LoadTime(Double value, String flowName, Instant time, String userName, String actionName, String elementName) {
        super(flowName, time, userName, actionName, elementName);
        this.value = value;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

}
