package com.performance.demo.performance.ios.pojo.system;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class SystemPerformance {

    @JsonProperty("sysmon_monitor")
    private List<SysmonMonitor> sysmonMonitorMetrics;
    @JsonProperty("graphics")
    private List<Graphics> graphicsMetrics;
    @JsonProperty("netstat")
    private Netstat netstatMetrics;

}
