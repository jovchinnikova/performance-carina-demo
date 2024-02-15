package com.performance.demo.performance.ios.pojo.system;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName("InterfaceDetectionEvent")
public class InterfaceDetectionEvent implements Event {

    @JsonProperty("interface_index")
    private int interface_index;
    @JsonProperty("name")
    private String name;

}
