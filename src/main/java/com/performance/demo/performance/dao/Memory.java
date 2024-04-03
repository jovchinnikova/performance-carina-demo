package com.performance.demo.performance.dao;

import com.influxdb.annotations.Column;
import com.influxdb.annotations.Measurement;

import java.time.Instant;

@Measurement(name = "mem")
public class Memory extends BaseMeasurement {

    @Column(name = "proportional_set_size")
    private Double usedPercent;

    @Column(tag = true, name = "action_name")
    private String actionName;

    @Column(tag = true, name = "element_name")
    private String elementName;

    public Memory(Double usedPercent, Instant time, String flowName, String userName, String actionName, String elementName) {
        super(flowName, time, userName);
        this.usedPercent = usedPercent;
        this.actionName = actionName;
        this.elementName = elementName;
    }

    public Double getUsedPercent() {
        return usedPercent;
    }

    public void setUsedPercent(Double usedPercent) {
        this.usedPercent = usedPercent;
    }

}
