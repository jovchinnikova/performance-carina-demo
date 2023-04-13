package com.performance.demo.utils.parser;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MemParser2 {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    public static final Pattern PSS_DELIMITER = Pattern.compile("Total PSS by process:");

    private static final Pattern VALUE = Pattern.compile("^\\s*(.*)K: com\\.solvd\\.carinademoapplication .*");

    public MemParser.MemRow parse(List<String> lines){
        boolean foundDel = false;

        MemParser.MemRow memRow = new MemParser.MemRow();

        for(String line : lines) {
            Matcher matcher = PSS_DELIMITER.matcher(line);
            if(matcher.matches()){
                LOGGER.info("Found delimiter");
                foundDel = true;
            }
            Matcher matcher2 = VALUE.matcher(line);
            if(matcher2.matches() && foundDel){
                String val = matcher2.group(1).replace(",","");
                memRow.totalPss = Integer.parseInt(val);
                break;
            }
        }
        return memRow;
    }

}
