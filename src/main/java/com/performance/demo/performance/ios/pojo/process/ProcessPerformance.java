package com.performance.demo.performance.ios.pojo.process;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class ProcessPerformance {

    @JsonProperty("sysmon_monitor_pid")
    private List<SysmonMonitorPid> sysmonMonitorPidMetrics;

    @JsonProperty("energy_pid")
    private List<Energy> energyMetrics;

    @JsonProperty("netstat_pid")
    private List<NetstatPid> netstatPidMetrics;

    public List<SysmonMonitorPid> getSysmonMonitorPidMetrics() {
        return sysmonMonitorPidMetrics;
    }

    public List<Energy> getEnergyMetrics() {
        return energyMetrics;
    }

    public List<NetstatPid> getNetstatPidMetrics() {
        return netstatPidMetrics;
    }
}
