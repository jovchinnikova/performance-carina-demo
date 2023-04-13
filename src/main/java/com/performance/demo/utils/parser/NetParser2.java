package com.performance.demo.utils.parser;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NetParser2 {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private NetParser.NetRow row;

    private static final Pattern VALUES = Pattern.compile("\\swlan0: *(\\d*) *(\\d*) * *(\\d*) *(\\d*) *(\\d*) *(\\d*) *(\\d*) *(\\d*) *(\\d*) *(\\d*) *(\\d*) *(\\d*) *(\\d*) *(\\d*) *(\\d*) *(\\d*)");

    public NetParser.NetRow parse(List<String> lines) {
        int st = 0;
        long rb;
        long rp;
        long tb;
        long tp;

        for (String line : lines) {
            Matcher matcher = VALUES.matcher(line);
            if (matcher.matches()) {
                LOGGER.info("Matcher " + matcher);
                rb = Long.parseLong(matcher.group(1));
                rp = Long.parseLong(matcher.group(2));
                tb = Long.parseLong(matcher.group(9));
                tp = Long.parseLong(matcher.group(10));
                row = new NetParser.NetRow(st, rb, rp, tp, tb);
            }
        }
        return row;
    }
}
