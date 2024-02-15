package com.performance.demo.performance.ios.pojo.process;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Energy {

    @JsonProperty("energy.overhead")
    private int overhead;
    @JsonProperty("kIDEGaugeSecondsSinceInitialQueryKey")
    private int kIDEGaugeSecondsSinceInitialQueryKey;
    @JsonProperty("energy.version")
    private int version;
    @JsonProperty("energy.networkning.overhead")
    private int networkingOverhead;
    @JsonProperty("energy.appstate.cost")
    private int appStateCost;
    @JsonProperty("energy.location.overhead")
    private int locationOverhead;
    @JsonProperty("energy.thermalstate.cost")
    private int thermalStateCost;
    @JsonProperty("energy.networking.cost")
    private int networkingCost;
    @JsonProperty("energy.cost")
    private int cost;
    @JsonProperty("energy.cpu.overhead")
    private int cpuOverhead;
    @JsonProperty("energy.appstate.overhead")
    private int appStateOverhead;
    @JsonProperty("energy.gpu.overhead")
    private int gpuOverhead;
    @JsonProperty("energy.inducedthermalstate.cost")
    private int inducedThermalStateCost;
    @JsonProperty("energy.display.overhead")
    private int displayOverhead;
    @JsonProperty("energy.gpu.cost")
    private int energyGpuCost;
    @JsonProperty("energy.cpu.cost")
    private int energyCpuCost;
    @JsonProperty("energy.display.cost")
    private int energyDisplayCost;
    @JsonProperty("energy.location.cost")
    private int energyLocationCost;

}
