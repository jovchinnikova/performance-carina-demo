package com.performance.demo.performance.ios.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.performance.demo.performance.ios.pojo.process.Energy;
import com.performance.demo.performance.ios.pojo.process.NetstatPid;
import com.performance.demo.performance.ios.pojo.process.ProcessPerformance;
import com.performance.demo.performance.ios.pojo.process.SysmonMonitorPid;
import com.performance.demo.performance.ios.pojo.system.Event;
import com.performance.demo.performance.ios.pojo.system.Graphics;
import com.performance.demo.performance.ios.pojo.system.SysmonMonitor;
import com.performance.demo.performance.ios.pojo.system.SystemPerformance;

import java.util.List;

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

    public void addTestEvents(List<TestEvent> testEvents) {
        for (TestEvent testEvent : testEvents){
            List<Energy> energyMetrics = getProcessPerformance().getEnergyMetrics();
            for (Energy energy : energyMetrics) {
                energy.convertTime();
                if (testEvent.getTime().getEpochSecond() == energy.getTime().getEpochSecond()) {
                    energy.setEventType(testEvent);
                }
            }
            List<NetstatPid> netstatPidMetrics = getProcessPerformance().getNetstatPidMetrics();
            for (NetstatPid netstatPid : netstatPidMetrics) {
                netstatPid.convertTime();
                if (testEvent.getTime().getEpochSecond() == netstatPid.getTime().getEpochSecond()) {
                    netstatPid.setEventType(testEvent);
                }
            }
            List<SysmonMonitorPid> sysmonMonitorPidMetrics = getProcessPerformance().getSysmonMonitorPidMetrics();
            for (SysmonMonitorPid sysmonMonitorPid : sysmonMonitorPidMetrics) {
                sysmonMonitorPid.convertTime();
                if (testEvent.getTime().getEpochSecond() == sysmonMonitorPid.getTime().getEpochSecond()) {
                    sysmonMonitorPid.setEventType(testEvent);
                }
            }
            List<Graphics> graphicMetrics = getSystemPerformance().getGraphicsMetrics();
            for (Graphics graphics : graphicMetrics) {
                graphics.convertTime();
                if (testEvent.getTime().getEpochSecond() == graphics.getTime().getEpochSecond()) {
                    graphics.setEventType(testEvent);
                }
            }
            List<SysmonMonitor> sysmonMonitorMetrics = getSystemPerformance().getSysmonMonitorMetrics();
            for (SysmonMonitor sysmonMonitor : sysmonMonitorMetrics) {
                sysmonMonitor.convertTime();
                if (testEvent.getTime().getEpochSecond() == sysmonMonitor.getTime().getEpochSecond()) {
                    sysmonMonitor.setEventType(testEvent);
                }
            }
            List<Event> netstatMetrics = getSystemPerformance().getNetstatMetrics();
            for (Event event : netstatMetrics) {
                event.convertTime();
                if (testEvent.getTime().getEpochSecond() == event.getTime().getEpochSecond()) {
                    event.setEventType(testEvent);
                }
            }
        }
    }
}
