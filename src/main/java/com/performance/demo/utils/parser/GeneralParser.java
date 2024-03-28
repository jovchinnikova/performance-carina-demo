package com.performance.demo.utils.parser;

import com.performance.demo.performance.AdbPerformanceCollector;

import java.util.List;
import java.util.Map;

public class GeneralParser {

    private final String bundleId;

    public GeneralParser(String bundleId) {
        this.bundleId = bundleId;
    }

    public Row parse(List<String> lines, AdbPerformanceCollector.PerformanceTypes performanceTypes) {
        switch (performanceTypes) {
            case MEM2: {
                if (lines.contains(MemParser2.PSS_DELIMITER.toString())) {
                    return new MemParser2().parse(lines, bundleId);
                } else {
                    return new MemParser().parse(lines, bundleId);
                }
            }
            case GFX: {
                return new GfxParser().parse(lines);
            }
            default:
                return new Row() {
                };
        }
    }

    public Map<String, NetParser.NetRow> parseNet(List<String> lines) {
        return new NetParser2().parseForTwoTypes(lines);
    }

    public String getBundleId() {
        return bundleId;
    }

}