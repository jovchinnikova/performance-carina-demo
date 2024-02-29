package com.performance.demo.performance.ios.pojo.system;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.influxdb.annotations.Column;
import com.influxdb.annotations.Measurement;
import com.performance.demo.performance.dao.BaseMeasurement;

import java.time.Instant;

@Measurement(name = "Netstat")
@JsonRootName("ConnectionDetectionEvent")
public class ConnectionDetectionEvent extends BaseMeasurement implements Event {

    @Column(tag = true, name = "metric_type")
    String metricType = "System";

    @Column(tag = true, name = "event_type")
    String eventType = "ConnectionDetectionEvent";

    @Column(name = "interface_index")
    @JsonProperty("interface_index")
    private int interface_index;

    @Column(name = "pid")
    @JsonProperty("pid")
    private int pid;

    @Column(name = "recv_buffer_size")
    @JsonProperty("recv_buffer_size")
    private int recv_buffer_size;

    @Column(name = "recv_buffer_used")
    @JsonProperty("recv_buffer_used")
    private int recv_buffer_used;

    @Column(name = "serial_number")
    @JsonProperty("serial_number")
    private int serial_number;

    @Column(name = "kind")
    @JsonProperty("kind")
    private int kind;

    @JsonProperty("time")
    private long jsonTime;

    public void convertTime() {
        this.time = Instant.ofEpochSecond(jsonTime);
    }

}
