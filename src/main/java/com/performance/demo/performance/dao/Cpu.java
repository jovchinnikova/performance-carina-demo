package com.performance.demo.performance.dao;

import com.influxdb.annotations.Column;
import com.influxdb.annotations.Measurement;
import com.performance.demo.performance.ios.pojo.EventType;

import java.time.Instant;

@Measurement(name = "cpu")
public class Cpu extends BaseMeasurement {

    @Column(name = "value")
    private Double value;

    public Cpu(Double value, Instant time, String flowName, String userName, EventType eventType, String elementName) {
        super(flowName, time, userName, eventType, elementName);
        this.value = value;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }
}
