package com.performance.demo.performance.ios.pojo.system;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.influxdb.annotations.Column;
import com.influxdb.annotations.Measurement;

import java.time.Instant;

@Measurement(name = "Netstat")
@JsonRootName("InterfaceDetectionEvent")
public class InterfaceDetectionEvent implements Event {

    @Column(tag = true, name = "metricType")
    String metricType = "System";

    @Column(tag = true, name = "eventType")
    String eventType = "InterfaceDetectionEvent";

    @Column(name = "interface_index")
    @JsonProperty("interface_index")
    private int interface_index;

    @Column(name = "name")
    @JsonProperty("name")
    private String name;

    @JsonProperty("time")
    private long time;

    @Column(timestamp = true)
    private Instant instantTime;

    @Override
    public void convertTime() {
        this.instantTime = Instant.ofEpochSecond(time);
    }

}
