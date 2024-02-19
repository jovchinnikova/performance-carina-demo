package com.performance.demo.performance.ios.pojo.system;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.influxdb.annotations.Column;
import com.influxdb.annotations.Measurement;

import java.time.Instant;

@Measurement(name = "Graphics")
public class Graphics {

    @Column(tag = true, name = "metricType")
    private final String metricType = "System";

    @Column(name = "Device_Utilization_%")
    @JsonProperty("Device Utilization %")
    private int deviceUtilization;

    @Column(name = "Renderer_Utilization_%")
    @JsonProperty("Renderer Utilization %")
    private int rendererUtilization;

    @Column(name = "recoveryCount")
    @JsonProperty("recoveryCount")
    private int recoveryCount;

    @Column(name = "In_use_system_memory_(driver)")
    @JsonProperty("In use system memory (driver)")
    private int inUseSystemMemoryDriver;

    @Column(name = "In_use_system_memory_(driver)")
    @JsonProperty("SplitSceneCount")
    private int splitSceneCount;

    @Column(name = "Tiler_Utilization_%")
    @JsonProperty("Tiler Utilization %")
    private int tilerUtilization;

    @Column(name = "Alloc_system_memory")
    @JsonProperty("Alloc system memory")
    private int allocSystemMemory;

    @Column(name = "Allocated_PB_Size")
    @JsonProperty("Allocated PB Size")
    private int allocatedPBSize;

    @Column(name = "XRVideoCardRunTimeStamp")
    @JsonProperty("XRVideoCardRunTimeStamp")
    private int XRVideoCardRunTimeStamp;

    @Column(name = "In_use_system_memory")
    @JsonProperty("In use system memory")
    private int inUseSystemMemory;

    @Column(name = "TiledSceneBytes")
    @JsonProperty("TiledSceneBytes")
    private int tiledSceneBytes;

    @Column(name = "IOGLBundleName")
    @JsonProperty("IOGLBundleName")
    private String IOGLBundleName;

    @Column(name = "CoreAnimationFramesPerSecond")
    @JsonProperty("CoreAnimationFramesPerSecond")
    private int coreAnimationFramesPerSecond;

    @JsonProperty("time")
    private long time;

    @Column(timestamp = true)
    private Instant instantTime;

    public void convertTime() {
        this.instantTime = Instant.ofEpochSecond(time);
    }

}
