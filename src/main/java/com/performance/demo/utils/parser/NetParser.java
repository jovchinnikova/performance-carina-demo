package com.performance.demo.utils.parser;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class NetParser {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private static final Pattern VALUES = Pattern.compile("^\\s*st=(\\d*) rb=(\\d*) rp=(\\d*) tb=(\\d*) tp=(\\d*) op=(\\d*)");
    private static final String START_DELIMITER = "\\s*ident=\\[\\{type=(.*), subType=0, networkId=\\\".*\\\", metered=false, defaultNetwork=true(, .*\\}\\]|\\}\\]) uid=%s set=FOREGROUND tag=0x0";
    private static final Pattern STOP_DELIMITER = Pattern.compile("\\s*ident=\\[\\{type=.*, subType=0, networkId=\"(.*)\", metered=false, defaultNetwork=\\w*.*}].*");
    private static final Pattern TAGSTATS_STOP_DEL = Pattern.compile("UID tag stats:");

    private NetRow row;

    public NetRow parse(List<String> lines, String uid) {

        Pattern patternDelimiter = Pattern.compile(String.format(START_DELIMITER, uid));

        boolean matches = false;
        boolean foundRb = false;
        int st;
        long rb;
        long rp;
        long tb;
        long tp;

        List<NetRow> rows = new ArrayList<>();

        for (String line : lines) {
            Matcher matcher = patternDelimiter.matcher(line);

            if (matcher.matches()) {
                LOGGER.info("Found uid");
                matches = true;
            }
            Matcher stMatcher = VALUES.matcher(line);
            if (stMatcher.matches() && matches) {
                LOGGER.info("Matcher " + stMatcher);
                try {
                    st = Integer.parseInt(stMatcher.group(1));
                    rb = Long.parseLong(stMatcher.group(2));
                    rp = Long.parseLong(stMatcher.group(3));
                    tb = Long.parseLong(stMatcher.group(4));
                    tp = Long.parseLong(stMatcher.group(5));
                    NetRow tempRow = new NetRow(st, rb, rp, tp, tb);
                    rows.add(tempRow);
                    foundRb = true;
                } catch (Exception e) {
                    LOGGER.warn("There was an error during converting values to long");
                }
            }
            matcher = STOP_DELIMITER.matcher(line);
            Matcher matcher1 = TAGSTATS_STOP_DEL.matcher(line);
            if ((matcher.matches() || matcher1.matches()) && foundRb) {
                LOGGER.info("Found stop delimiter");
                List<Integer> stValues = rows.stream().map(NetRow::getSt).collect(Collectors.toList());
                Integer stMax = Collections.max(stValues);
                for (NetRow netRow : rows) {
                    if (stMax == netRow.getSt()) {
                        row = netRow;
                    }
                }
                break;
            }
        }
        return row;
    }

    public static class NetRow implements Row {

        private final int st;
        private final long rb;
        private final long rp;
        private final long tp;
        private final long tb;

        public NetRow(int st, long rb, long rp, long tb, long tp) {
            this.st = st;
            this.rb = rb;
            this.rp = rp;
            this.tb = tb;
            this.tp = tp;
        }

        @Override
        public String toString() {
            return "Row{" +
                    "st=" + st +
                    ", rb=" + rb +
                    ", rp=" + rp +
                    ", tb=" + tb +
                    ", tp=" + tp +
                    '}';
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            NetRow other = (NetRow) obj;
            long sumThis = this.rb + this.rp + this.tb + this.tp;
            long sumOther = other.rb + other.rp + other.tb + other.tp;
            return sumThis == sumOther;
        }

        public int getSt() {
            return st;
        }

        public long getRb() {
            return rb;
        }

        public long getRp() {
            return rp;
        }

        public long getTp() {
            return tp;
        }

        public long getTb() {
            return tb;
        }
    }

}
