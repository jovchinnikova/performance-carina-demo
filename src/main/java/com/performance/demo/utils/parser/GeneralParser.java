package com.performance.demo.utils.parser;

import java.util.List;

import com.performance.demo.performance.PerformanceData;
import com.zebrunner.carina.webdriver.device.Device;

public class GeneralParser {

    private String bundleId;

    public GeneralParser(String bundleId) {
        this.bundleId = bundleId;
    }

    public Row parse(List<String> lines, PerformanceData.PerformanceTypes performanceTypes) {
        switch (performanceTypes) {
            case MEM: {
                if (lines.contains(MemParser2.PSS_DELIMITER.toString())) {
                    return new MemParser2().parse(lines, bundleId);
                } else {
                    return new MemParser().parse(lines, bundleId);
                }
            }
            case GFX: {
                return new GfxParser().parse(lines);
            }
            case NET: {
                return new NetParser2().parse(lines);
            }
            case CORE: {
                return new CoreParser().parse(lines);
            }
            default:
                return new Row() {
                };
        }
    }

    /**
     * Old method for parsing net data received as a result of the method
     * collectNetBenchmarksOld() from PerformanceData
     * Works only for Android 11 and 12
     */
    public Row parseNet(List<String> lines, Device device, String userId) {
        if ("11".equals(device.getOsVersion()) || "12".equals(device.getOsVersion())) {
            return new NetParser().parse(lines, userId);
        } else
            return new Row() {
            };
    }

    public String getBundleId() {
        return bundleId;
    }

    public void setBundleId(String bundleId) {
        this.bundleId = bundleId;
    }
}