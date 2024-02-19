package com.performance.demo.performance.ios.pojo.process;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.influxdb.annotations.Column;
import com.influxdb.annotations.Measurement;

import java.time.Instant;

@Measurement(name = "Energy")
public class Energy {

    @Column(tag = true, name = "metric_type")
    private String metricType = "Process";

    @Column(name = "energy.overhead")
    @JsonProperty("energy.overhead")
    private int overhead;

    @Column(name = "kIDEGaugeSecondsSinceInitialQueryKey")
    @JsonProperty("kIDEGaugeSecondsSinceInitialQueryKey")
    private int kIDEGaugeSecondsSinceInitialQueryKey;

    @Column(name = "energy.version")
    @JsonProperty("energy.version")
    private int version;

    @Column(name = "energy.networkning.overhead")
    @JsonProperty("energy.networkning.overhead")
    private int networkingOverhead;

    @Column(name = "energy.appstate.cost")
    @JsonProperty("energy.appstate.cost")
    private int appStateCost;

    @Column(name = "energy.location.overhead")
    @JsonProperty("energy.location.overhead")
    private int locationOverhead;

    @Column(name = "energy.thermalstate.cost")
    @JsonProperty("energy.thermalstate.cost")
    private int thermalStateCost;

    @Column(name = "energy.networking.cost")
    @JsonProperty("energy.networking.cost")
    private int networkingCost;

    @Column(name = "energy.cost")
    @JsonProperty("energy.cost")
    private int cost;

    @Column(name = "energy.cpu.overhead")
    @JsonProperty("energy.cpu.overhead")
    private int cpuOverhead;

    @Column(name = "energy.appstate.overhead")
    @JsonProperty("energy.appstate.overhead")
    private int appStateOverhead;

    @Column(name = "energy.gpu.overhead")
    @JsonProperty("energy.gpu.overhead")
    private int gpuOverhead;

    @Column(name = "energy.inducedthermalstate.cost")
    @JsonProperty("energy.inducedthermalstate.cost")
    private int inducedThermalStateCost;

    @Column(name = "energy.display.overhead")
    @JsonProperty("energy.display.overhead")
    private int displayOverhead;

    @Column(name = "energy.gpu.cost")
    @JsonProperty("energy.gpu.cost")
    private int energyGpuCost;

    @Column(name = "energy.cpu.cost")
    @JsonProperty("energy.cpu.cost")
    private int energyCpuCost;

    @Column(name = "energy.display.cost")
    @JsonProperty("energy.display.cost")
    private int energyDisplayCost;

    @Column(name = "energy.location.cost")
    @JsonProperty("energy.location.cost")
    private int energyLocationCost;

    @JsonProperty("time")
    private long time;

    @Column(timestamp = true)
    private Instant instantTime;

    public void convertTime() {
        this.instantTime = Instant.ofEpochSecond(time);
    }
}
