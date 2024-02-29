package com.performance.demo.performance.ios.pojo.process;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.influxdb.annotations.Column;
import com.influxdb.annotations.Measurement;
import com.performance.demo.performance.ios.pojo.system.SysmonMonitor;

import java.time.Instant;

@Measurement(name = "SysmonMonitor_pid")
public class SysmonMonitorPid extends SysmonMonitor {

    @Column(tag = true, name = "metric_type")
    private final String metricType = "Process";

    @JsonProperty("time")
    private long jsonTime;

    public void convertTime() {
        this.time = Instant.ofEpochSecond(jsonTime);
    }

}
