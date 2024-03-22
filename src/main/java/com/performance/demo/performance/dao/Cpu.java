package com.performance.demo.performance.dao;

import com.influxdb.annotations.Column;
import com.influxdb.annotations.Measurement;

import java.time.Instant;

@Measurement(name = "cpu")
public class Cpu extends BaseMeasurement {

    @Column(name = "value")
    private Double value;

    @Column(tag = true, name = "_actionName")
    private String actionName;

    public Cpu(Double value, Instant time, String flowName, String userName, String actionName) {
        super(flowName, time, userName);
        this.value = value;
        this.actionName = actionName;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }
}
