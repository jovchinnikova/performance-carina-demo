package com.performance.demo.performance.ios.pojo.system;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName("ConnectionUpdateEvent")
public class ConnectionUpdateEvent implements Event {

    @JsonProperty("rx_packets")
    private int rx_packets;
    @JsonProperty("rx_bytes")
    private int rx_bytes;
    @JsonProperty("tx_bytes")
    private int tx_bytes;
    @JsonProperty("rx_dups")
    private int rx_dups;
    @JsonProperty("rx000")
    private String rx000;
    @JsonProperty("tx_retx")
    private String tx_retx;
    @JsonProperty("min_rtt")
    private String min_rtt;
    @JsonProperty("avg_rtt")
    private String avg_rtt;
    @JsonProperty("connection_serial")
    private String connection_serial;
    @JsonProperty("unknown0")
    private int unknown0;
    @JsonProperty("unknown1")
    private int unknown1;

}
