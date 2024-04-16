package com.performance.demo.utils.parser;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NetParser2 {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private static final Pattern VALUES = Pattern.compile(
            "\\s(wlan[01]): *(\\d*) *(\\d*) * *(\\d*) *(\\d*) *(\\d*) *(\\d*) *(\\d*) *(\\d*) *(\\d*) *(\\d*) *(\\d*) *(\\d*) *(\\d*) *(\\d*) *(\\d*) *(\\d*)");

    public NetParser.NetRow parse(List<String> lines) {
        String expectedType;
        Map<String, NetParser.NetRow> netData = parseForTwoTypes(lines);
        if (netData.size() == 2)
            expectedType = "wlan1";
        else
            expectedType = "wlan0";

        LOGGER.info("Net data type: {}", expectedType);
        return netData.get(expectedType);
    }

    public Map<String, NetParser.NetRow> parseForTwoTypes(List<String> lines) {
        int st = 0;
        Map<String, NetParser.NetRow> netData = new HashMap<>();

        lines.forEach(line -> {
            Matcher matcher = VALUES.matcher(line);
            if (matcher.matches()) {
                String type = matcher.group(1);
                long rb = Long.parseLong(matcher.group(2));
                long rp = Long.parseLong(matcher.group(3));
                long tb = Long.parseLong(matcher.group(10));
                long tp = Long.parseLong(matcher.group(11));
                if (rb != 0 && rp != 0 && tb != 0 && tp != 0)
                    netData.put(type, new NetParser.NetRow(st, rb, rp, tp, tb));
            }
        });

        return netData;
    }
}
