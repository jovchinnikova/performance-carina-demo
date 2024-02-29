package com.performance.demo.performance.ios.pojo.process;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.influxdb.annotations.Column;
import com.influxdb.annotations.Measurement;
import com.performance.demo.performance.dao.BaseMeasurement;

import java.time.Instant;

@Measurement(name = "Energy")
public class Energy extends BaseMeasurement {

    @Column(tag = true, name = "metric_type")
    private String metricType = "Process";

    @Column(name = "total_overhead")
    @JsonProperty("energy.overhead")
    private int totalOverhead;

    @Column(name = "kIDEGaugeSecondsSinceInitialQueryKey")
    @JsonProperty("kIDEGaugeSecondsSinceInitialQueryKey")
    private int kIDEGaugeSecondsSinceInitialQueryKey;

    @Column(name = "version")
    @JsonProperty("energy.version")
    private int version;

    @Column(name = "networking_overhead")
    @JsonProperty("energy.networkning.overhead")
    private int networkingOverhead;

    @Column(name = "appState_cost")
    @JsonProperty("energy.appstate.cost")
    private int appStateCost;

    @Column(name = "location_overhead")
    @JsonProperty("energy.location.overhead")
    private int locationOverhead;

    @Column(name = "thermalState_cost")
    @JsonProperty("energy.thermalstate.cost")
    private int thermalStateCost;

    @Column(name = "networking_cost")
    @JsonProperty("energy.networking.cost")
    private int networkingCost;

    @Column(name = "total_cost")
    @JsonProperty("energy.cost")
    private int totalCost;

    @Column(name = "cpu_overhead")
    @JsonProperty("energy.cpu.overhead")
    private int cpuOverhead;

    @Column(name = "appState_overhead")
    @JsonProperty("energy.appstate.overhead")
    private int appStateOverhead;

    @Column(name = "gpu_overhead")
    @JsonProperty("energy.gpu.overhead")
    private int gpuOverhead;

    @Column(name = "inducedThermalState_cost")
    @JsonProperty("energy.inducedthermalstate.cost")
    private int inducedThermalStateCost;

    @Column(name = "display_overhead")
    @JsonProperty("energy.display.overhead")
    private int displayOverhead;

    @Column(name = "gpu_cost")
    @JsonProperty("energy.gpu.cost")
    private int gpuCost;

    @Column(name = "cpu_cost")
    @JsonProperty("energy.cpu.cost")
    private int cpuCost;

    @Column(name = "display_cost")
    @JsonProperty("energy.display.cost")
    private int displayCost;

    @Column(name = "location_cost")
    @JsonProperty("energy.location.cost")
    private int locationCost;

    @JsonProperty("time")
    private long jsonTime;

    public void convertTime() {
        this.time = Instant.ofEpochSecond(jsonTime);
    }
}
