package com.performance.demo.performance.dao;

import com.influxdb.annotations.Column;
import com.influxdb.annotations.Measurement;
import com.performance.demo.performance.ios.pojo.EventType;

import java.time.Instant;

@Measurement(name = "load_time")
public class LoadTime extends BaseMeasurement{

    @Column(name = "value")
    private Double value;

    public LoadTime(Double value, String flowName, Instant time, String userName, EventType eventType, String elementName) {
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
