package com.performance.demo.utils.parser;

import java.lang.invoke.MethodHandles;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MemParser {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private static final String MEM_INFO_DELIMITER = "\\**\\s*MEMINFO in pid \\d+ \\[%s]\\s*\\**";
    private static final Pattern TOTAL_VALUES = Pattern.compile("^\\s*TOTAL[^:]\\s*[^A-Z]\\s(\\d*)\\s*(.*)");

    private MemRow memRow;

    public MemRow parse(List<String> lines, String bundleId) {
        Pattern delimiterPattern = Pattern.compile(String.format(MEM_INFO_DELIMITER, bundleId));
        boolean foundDel = false;

        for (String line : lines) {
            Matcher m = delimiterPattern.matcher(line);
            if (m.matches()) {
                LOGGER.info("Found delimiter");
                foundDel = true;
            }

            Matcher m2 = TOTAL_VALUES.matcher(line);
            if (m2.matches() && foundDel) {
                Integer totalPss = Integer.parseInt(m2.group(1));
                memRow = new MemRow(totalPss);
                break;
            }
        }
        return memRow;
    }

    public static class MemRow implements Row {
        private Integer totalPss;

        public MemRow(Integer totalPss) {
            this.totalPss = totalPss;
        }

        public Integer getTotalPss() {
            return totalPss;
        }

        public void setTotalPss(Integer totalPss) {
            this.totalPss = totalPss;
        }
    }

}
