package com.performance.demo.utils.parser;

import java.lang.invoke.MethodHandles;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MemParser2 {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    public static final Pattern PSS_DELIMITER = Pattern.compile("Total PSS by process:");

    private static final String VALUE = "^\\s*(.*)K: %s .*";

    private MemParser.MemRow memRow;

    public MemParser.MemRow parse(List<String> lines, String bundleId) {
        Pattern valuePattern = Pattern.compile(String.format(VALUE, bundleId));
        boolean foundDel = false;

        for (String line : lines) {
            Matcher matcher = PSS_DELIMITER.matcher(line);
            if (matcher.matches()) {
                LOGGER.info("Found delimiter");
                foundDel = true;
            }
            Matcher matcher2 = valuePattern.matcher(line);
            if (matcher2.matches() && foundDel) {
                String val = matcher2.group(1).replace(",", "");
                Integer totalPss = Integer.parseInt(val);
                memRow = new MemParser.MemRow(totalPss);
                break;
            }
        }
        return memRow;
    }

}
