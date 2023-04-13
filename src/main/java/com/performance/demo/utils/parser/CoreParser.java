package com.performance.demo.utils.parser;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CoreParser {

    private static final Pattern CPU = Pattern.compile("cpu(\\d+) .*");

    public CoreRow parse(List<String> lines) {

        int coreQuantity = 0;

        for (String line : lines) {
            Matcher matcher = CPU.matcher(line);
            if (matcher.matches()) {
                coreQuantity++;
            }
        }
        return new CoreRow(coreQuantity);
    }

    public static class CoreRow implements Row {

        private int coreQuantity;

        CoreRow(int coreQuantity) {
            this.coreQuantity = coreQuantity;
        }

        public int getCoreQuantity() {
            return coreQuantity;
        }

        public void setCoreQuantity(int coreQuantity) {
            this.coreQuantity = coreQuantity;
        }
    }
}
