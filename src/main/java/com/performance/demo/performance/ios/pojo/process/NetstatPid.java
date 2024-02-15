package com.performance.demo.performance.ios.pojo.process;

import com.fasterxml.jackson.annotation.JsonProperty;

public class NetstatPid {

    @JsonProperty("net.packets.delta")
    private int netPacketsDelta;
    @JsonProperty("time")
    private double time;
    @JsonProperty("net.tx.bytes")
    private int netTxBytes;
    @JsonProperty("net.bytes.delta")
    private int netBytesDelta;
    @JsonProperty("net.rx.packets.delta")
    private int netRxPacketsDelta;
    @JsonProperty("net.tx.packets")
    private int netTxPackets;
    @JsonProperty("net.rx.bytes")
    private int netRxBytes;
    @JsonProperty("net.bytes")
    private int netBytes;
    @JsonProperty("net.tx.bytes.delta")
    private int netTxBytesDelta;
    @JsonProperty("net.rx.bytes.delta")
    private int netRxBytesDelta;
    @JsonProperty("net.rx.packets")
    private int netRxPackets;
    @JsonProperty("pid")
    private int pid;
    @JsonProperty("net.tx.packets.delta")
    private int netTxPacketsDelta;
    @JsonProperty("net.packets")
    private int netPackets;

}
