package com.performance.demo.performance.ios.pojo.system;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Graphics {

    @JsonProperty("Device Utilization %")
    private int deviceUtilization;
    @JsonProperty("Renderer Utilization %")
    private int rendererUtilization;
    @JsonProperty("recoveryCount")
    private int recoveryCount;
    @JsonProperty("In use system memory (driver)")
    private int inUseSystemMemoryDriver;
    @JsonProperty("SplitSceneCount")
    private int splitSceneCount;
    @JsonProperty("Tiler Utilization %")
    private int tilerUtilization;
    @JsonProperty("Alloc system memory")
    private int allocSystemMemory;
    @JsonProperty("Allocated PB Size")
    private int allocatedPBSize;
    @JsonProperty("XRVideoCardRunTimeStamp")
    private int XRVideoCardRunTimeStamp;
    @JsonProperty("In use system memory")
    private int inUseSystemMemory;
    @JsonProperty("TiledSceneBytes")
    private int tiledSceneBytes;
    @JsonProperty("IOGLBundleName")
    private String IOGLBundleName;
    @JsonProperty("CoreAnimationFramesPerSecond")
    private int coreAnimationFramesPerSecond;

}
