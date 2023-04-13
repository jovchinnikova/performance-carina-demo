package com.performance.demo.performance.android.dao;

import com.influxdb.annotations.Column;
import com.influxdb.annotations.Measurement;

import java.time.Instant;

@Measurement(name = "mem")
public class Memory extends BaseMeasurement {

    @Column(name = "proportional_set_size")
    private Double usedPercent;

    public Memory(Double usedPercent, Instant time, String flowName, String userName) {
        super(flowName, time, userName);
        this.usedPercent = usedPercent;
    }

    public Double getUsedPercent() {
        return usedPercent;
    }

    public void setUsedPercent(Double usedPercent) {
        this.usedPercent = usedPercent;
    }

}
