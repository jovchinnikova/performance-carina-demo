package com.performance.demo.performance.dao;

import com.influxdb.annotations.Column;
import com.influxdb.annotations.Measurement;

import java.time.Instant;

@Measurement(name = "cpu")
public class Cpu extends BaseMeasurement {

    @Column(name = "value")
    private Double value;

    public Cpu(Double value, Instant time, String flowName, String userName, String actionName, String elementName) {
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
