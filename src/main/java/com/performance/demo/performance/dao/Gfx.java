package com.performance.demo.performance.dao;

import com.influxdb.annotations.Column;
import com.influxdb.annotations.Measurement;

import java.time.Instant;

@Measurement(name = "gfx")
public class Gfx extends BaseMeasurement {

    @Column(tag = true, name = "activityName")
    private String activityName;

    @Column(name = "total_frames")
    private int totalFrames;

    @Column(name = "janky_frames")
    private int jankyFrames;

    @Column(name = "90_percentile")
    private int percentile90;

    @Column(name = "95_percentile")
    private int percentile95;

    @Column(name = "99_percentile")
    private int percentile99;

    public Gfx(int totalFrames, int jankyFrames, int percentile90, int percentile95, int percentile99, Instant time,
               String flowName, String userName) {
        super(flowName, time, userName);
        this.totalFrames = totalFrames;
        this.jankyFrames = jankyFrames;
        this.percentile90 = percentile90;
        this.percentile95 = percentile95;
        this.percentile99 = percentile99;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public int getTotalFrames() {
        return totalFrames;
    }

    public void setTotalFrames(int totalFrames) {
        this.totalFrames = totalFrames;
    }

    public int getJankyFrames() {
        return jankyFrames;
    }

    public void setJankyFrames(int jankyFrames) {
        this.jankyFrames = jankyFrames;
    }

    public int getPercentile90() {
        return percentile90;
    }

    public void setPercentile90(int percentile90) {
        this.percentile90 = percentile90;
    }

    public int getPercentile95() {
        return percentile95;
    }

    public void setPercentile95(int percentile95) {
        this.percentile95 = percentile95;
    }

    public int getPercentile99() {
        return percentile99;
    }

    public void setPercentile99(int percentile99) {
        this.percentile99 = percentile99;
    }

}
