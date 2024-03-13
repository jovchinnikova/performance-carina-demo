package com.performance.demo.performance.ios.pojo.process;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.influxdb.annotations.Column;
import com.influxdb.annotations.Measurement;
import com.performance.demo.performance.dao.BaseMeasurement;

import java.time.Instant;

@Measurement(name = "Netstat_pid")
public class NetstatPid extends BaseMeasurement {

    @Column(tag = true, name = "metric_type")
    private final String metricType = "Process";

    @Column(name = "net.packets.delta")
    @JsonProperty("net.packets.delta")
    private int netPacketsDelta;

    @Column(name = "tb")
    @JsonProperty("net.tx.bytes")
    private int netTxBytes;

    @Column(name = "net.bytes.delta")
    @JsonProperty("net.bytes.delta")
    private int netBytesDelta;

    @Column(name = "net.rx.packets.delta")
    @JsonProperty("net.rx.packets.delta")
    private int netRxPacketsDelta;

    @Column(name = "tp")
    @JsonProperty("net.tx.packets")
    private int netTxPackets;

    @Column(name = "rb")
    @JsonProperty("net.rx.bytes")
    private int netRxBytes;

    @Column(name = "total_bytes")
    @JsonProperty("net.bytes")
    private int netBytes;

    @Column(name = "net.tx.bytes.delta")
    @JsonProperty("net.tx.bytes.delta")
    private int netTxBytesDelta;

    @Column(name = "net.rx.bytes.delta")
    @JsonProperty("net.rx.bytes.delta")
    private int netRxBytesDelta;

    @Column(name = "rp")
    @JsonProperty("net.rx.packets")
    private int netRxPackets;

    @Column(name = "pid")
    @JsonProperty("pid")
    private int pid;

    @Column(name = "net.tx.packets.delta")
    @JsonProperty("net.tx.packets.delta")
    private int netTxPacketsDelta;

    @Column(name = "total_packets")
    @JsonProperty("net.packets")
    private int netPackets;

    @JsonProperty("time")
    private long jsonTime;

    public void convertTime() {
        this.time = Instant.ofEpochSecond(jsonTime);
    }
}
