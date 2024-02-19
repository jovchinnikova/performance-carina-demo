package com.performance.demo.performance.ios.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.performance.demo.performance.ios.pojo.process.ProcessPerformance;
import com.performance.demo.performance.ios.pojo.system.SystemPerformance;

public class Performance {

    @JsonProperty("system_performance")
    private SystemPerformance systemPerformance;

    @JsonProperty("process_performance")
    private ProcessPerformance processPerformance;

    public SystemPerformance getSystemPerformance() {
        return systemPerformance;
    }

    public ProcessPerformance getProcessPerformance() {
        return processPerformance;
    }
}
