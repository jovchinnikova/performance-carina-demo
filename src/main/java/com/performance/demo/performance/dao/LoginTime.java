package com.performance.demo.performance.dao;

import com.influxdb.annotations.Column;
import com.influxdb.annotations.Measurement;

import java.time.Instant;

@Measurement(name = "login_time")
public class LoginTime extends BaseMeasurement {

    @Column(name = "value")
    private Double value;

    public LoginTime(Double value, Instant time, String flowName, String userName) {
        super(flowName, time, userName);
        this.value = value;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

}


