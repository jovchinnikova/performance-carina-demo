package com.performance.demo.performance.ios.pojo.system;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName("ConnectionDetectionEvent")
public class ConnectionDetectionEvent implements Event {

    @JsonProperty("interface_index")
    private int interface_index;
    @JsonProperty("pid")
    private int pid;
    @JsonProperty("recv_buffer_size")
    private int recv_buffer_size;
    @JsonProperty("recv_buffer_used")
    private int recv_buffer_used;
    @JsonProperty("serial_number")
    private int serial_number;
    @JsonProperty("kind")
    private int kind;

}
