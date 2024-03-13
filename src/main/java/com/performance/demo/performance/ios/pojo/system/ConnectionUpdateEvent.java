package com.performance.demo.performance.ios.pojo.system;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.influxdb.annotations.Column;
import com.influxdb.annotations.Measurement;
import com.performance.demo.performance.dao.BaseMeasurement;

import java.math.BigInteger;
import java.time.Instant;

@Measurement(name = "Netstat")
@JsonRootName("ConnectionUpdateEvent")
public class ConnectionUpdateEvent extends BaseMeasurement implements Event {

    @Column(tag = true, name = "type")
    String eventType = "ConnectionUpdateEvent";

    @Column(tag = true, name = "metric_type")
    String metricType = "System";

    @Column(name = "rx_packets")
    @JsonProperty("rx_packets")
    private int rx_packets;

    @Column(name = "rx_bytes")
    @JsonProperty("rx_bytes")
    private int rx_bytes;

    @Column(name = "tx_bytes")
    @JsonProperty("tx_bytes")
    private int tx_bytes;

    @Column(name = "rx_dups")
    @JsonProperty("rx_dups")
    private BigInteger rx_dups;

    @Column(name = "rx000")
    @JsonProperty("rx000")
    private int rx000;

    @Column(name = "tx_retx")
    @JsonProperty("tx_retx")
    private int tx_retx;

    @Column(name = "min_rtt")
    @JsonProperty("min_rtt")
    private int min_rtt;

    @Column(name = "avg_rtt")
    @JsonProperty("avg_rtt")
    private int avg_rtt;

    @Column(name = "connection_serial")
    @JsonProperty("connection_serial")
    private int connection_serial;

    @Column(name = "unknown0")
    @JsonProperty("unknown0")
    private int unknown0;

    @Column(name = "unknown1")
    @JsonProperty("unknown1")
    private int unknown1;

    @JsonProperty("json_time")
    private long jsonTime;

    public void convertTime() {
        this.time = Instant.ofEpochSecond(jsonTime);
    }
}
