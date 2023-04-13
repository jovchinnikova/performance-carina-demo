package com.performance.demo.utils.parser;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MemParser {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private static final Pattern MEM_INFO_DELIMITER = Pattern.compile("\\**\\s*MEMINFO in pid \\d+ \\[com\\.solvd\\.carinademoapplication]\\s*\\**");
    private static final Pattern TOTAL_VALUES = Pattern.compile("^\\s*TOTAL[^:]\\s*[^A-Z]\\s(\\d*)\\s*(.*)");

    public MemRow parse(List<String> lines) {
        boolean foundDel = false;
        MemRow memRow = new MemRow();

        for (String line : lines) {
            Matcher m = MEM_INFO_DELIMITER.matcher(line);
            if (m.matches()) {
                LOGGER.info("Found delimiter");
                foundDel = true;
            }

            Matcher m2 = TOTAL_VALUES.matcher(line);
            if (m2.matches() && foundDel) {
                memRow.totalPss = Integer.parseInt(m2.group(1));
            }
        }
        return memRow;
    }

    public static class MemRow implements Row {

        protected Integer totalPss;

        public Integer getTotalPss() {
            return totalPss;
        }

        public void setTotalPss(Integer totalPss) {
            this.totalPss = totalPss;
        }
    }

}

