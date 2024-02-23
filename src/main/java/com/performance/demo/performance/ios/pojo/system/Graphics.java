package com.performance.demo.performance.ios.pojo.system;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.influxdb.annotations.Column;
import com.influxdb.annotations.Measurement;
import com.performance.demo.performance.dao.BaseMeasurement;

import java.time.Instant;

@Measurement(name = "Graphics")
public class Graphics extends BaseMeasurement {

    @Column(tag = true, name = "metric_type")
    private final String metricType = "System";

    @Column(name = "device_utilization")
    @JsonProperty("Device Utilization %")
    private int deviceUtilization;

    @Column(name = "renderer_utilization")
    @JsonProperty("Renderer Utilization %")
    private int rendererUtilization;

    @Column(name = "recovery_count")
    @JsonProperty("recoveryCount")
    private int recoveryCount;

    @Column(name = "In_use_system_memory_driver")
    @JsonProperty("In use system memory (driver)")
    private int inUseSystemMemoryDriver;

    @Column(name = "split_scene_count")
    @JsonProperty("SplitSceneCount")
    private int splitSceneCount;

    @Column(name = "tiler_utilization")
    @JsonProperty("Tiler Utilization %")
    private int tilerUtilization;

    @Column(name = "alloc_system_memory")
    @JsonProperty("Alloc system memory")
    private int allocSystemMemory;

    @Column(name = "allocated_PB_size")
    @JsonProperty("Allocated PB Size")
    private int allocatedPBSize;

    @Column(name = "XRVideoCardRunTimeStamp")
    @JsonProperty("XRVideoCardRunTimeStamp")
    private int XRVideoCardRunTimeStamp;

    @Column(name = "in_use_system_memory")
    @JsonProperty("In use system memory")
    private int inUseSystemMemory;

    @Column(name = "tiled_scene_bytes")
    @JsonProperty("TiledSceneBytes")
    private int tiledSceneBytes;

    @Column(name = "IOGLBundleName")
    @JsonProperty("IOGLBundleName")
    private String IOGLBundleName;

    @Column(name = "FPS")
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
