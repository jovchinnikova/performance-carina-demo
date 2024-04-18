package com.performance.demo.utils.parser;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GfxParser {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private static final Pattern TOTAL_FRAMES_PREFIX = Pattern.compile("Total frames rendered: (\\d+)");
    private static final Pattern JANKY_FRAMES_PREFIX = Pattern.compile("Janky frames: (\\d+) \\(.+%\\)");
    private static final Pattern PERCENTILE_90_PREFIX = Pattern.compile("90th percentile: (\\d+)ms");
    private static final Pattern PERCENTILE_95_PREFIX = Pattern.compile("95th percentile: (\\d+)ms");
    private static final Pattern PERCENTILE_99_PREFIX = Pattern.compile("99th percentile: (\\d+)ms");
    private static final Pattern GENERAL_INFO_DELIMITER = Pattern.compile("Stats since");

    private GfxRow gfxRow;

    public GfxRow parse(List<String> lines) {
        Integer totalFrames = null;
        Integer jankyFrames = null;
        Integer percentile90 = null;
        Integer percentile95 = null;
        Integer percentile99 = null;
        boolean foundGfxDel = false;

        for (String line : lines) {
            Matcher matcher = GENERAL_INFO_DELIMITER.matcher(line);
            if (matcher.find()) {
                LOGGER.info("Found gfx delimiter");
                foundGfxDel = true;
            }

            matcher = TOTAL_FRAMES_PREFIX.matcher(line);
            if (matcher.matches() && foundGfxDel) {
                totalFrames = Integer.parseInt(matcher.group(1));
                LOGGER.info("total_frames: " + totalFrames);
            }

            matcher = JANKY_FRAMES_PREFIX.matcher(line);
            if (matcher.matches() && foundGfxDel) {
                jankyFrames = Integer.parseInt(matcher.group(1));
                LOGGER.info("janky_frames: " + jankyFrames);
            }

            matcher = PERCENTILE_90_PREFIX.matcher(line);
            if (matcher.matches() && foundGfxDel) {
                percentile90 = Integer.parseInt(matcher.group(1));
                LOGGER.info("90_p: " + percentile90);
            }

            matcher = PERCENTILE_95_PREFIX.matcher(line);
            if (matcher.matches() && foundGfxDel) {
                percentile95 = Integer.parseInt(matcher.group(1));
                LOGGER.info("95_p: " + percentile95);
            }

            matcher = PERCENTILE_99_PREFIX.matcher(line);
            if (matcher.matches() && foundGfxDel) {
                percentile99 = Integer.parseInt(matcher.group(1));
                LOGGER.info("99_p: " + percentile99);
            }

            if (totalFrames != null && jankyFrames != null && percentile90 != null && percentile95 != null && percentile99 != null) {
                gfxRow = new GfxRow(totalFrames, jankyFrames, percentile90, percentile95, percentile99);
                break;
            }
        }

        return gfxRow;
    }

    public static class GfxRow implements Row {

        private int totalFrames;
        private int jankyFrames;
        private int percentile90;
        private int percentile95;
        private int percentile99;

        public GfxRow(int totalFrames, int jankyFrames, int percentile90, int percentile95, int percentile99) {
            this.totalFrames = totalFrames;
            this.jankyFrames = jankyFrames;
            this.percentile90 = percentile90;
            this.percentile95 = percentile95;
            this.percentile99 = percentile99;
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

        @Override
        public String toString() {
            return "Row{" +
                    "totalFrames=" + totalFrames +
                    ", jankyFrames=" + jankyFrames +
                    ", percentile90=" + percentile90 +
                    ", percentile95=" + percentile95 +
                    ", percentile99=" + percentile99 +
                    '}';
        }
    }

    public GfxRow getRow() {
        return gfxRow;
    }
}