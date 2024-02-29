package com.performance.demo.performance.ios.pojo.system;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.influxdb.annotations.Column;
import com.influxdb.annotations.Measurement;
import com.performance.demo.performance.dao.BaseMeasurement;

import java.time.Instant;

@Measurement(name = "Netstat")
@JsonRootName("InterfaceDetectionEvent")
public class InterfaceDetectionEvent extends BaseMeasurement implements Event {

    @Column(tag = true, name = "metric_type")
    String metricType = "System";

    @Column(tag = true, name = "event_type")
    String eventType = "InterfaceDetectionEvent";

    @Column(name = "interface_index")
    @JsonProperty("interface_index")
    private int interface_index;

    @Column(name = "name")
    @JsonProperty("name")
    private String name;

    @JsonProperty("time")
    private long jsonTime;

    public void convertTime() {
        this.time = Instant.ofEpochSecond(jsonTime);
    }

}
