package com.performance.demo.performance.dao;

import com.influxdb.annotations.Column;
import com.influxdb.annotations.Measurement;

import java.time.Instant;

@Measurement(name = "cpu")
public class Cpu extends BaseMeasurement {

    @Column(name = "value")
    private Double value;

    @Column(tag = true, name = "actionName")
    private String actionName;

    @Column(tag = true, name = "elementName")
    private String elementName;

    public Cpu(Double value, Instant time, String flowName, String userName, String actionName, String elementName) {
        super(flowName, time, userName);
        this.value = value;
        this.actionName = actionName;
        this.elementName = elementName;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }
}
