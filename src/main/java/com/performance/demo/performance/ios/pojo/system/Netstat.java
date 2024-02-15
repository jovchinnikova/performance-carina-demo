package com.performance.demo.performance.ios.pojo.system;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class Netstat {

    @JsonProperty("events")
    private List<Event> events;
}
