package com.performance.demo.performance.ios;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.performance.demo.performance.ios.pojo.Performance;

public class Parser {

    public static Performance createPerformanceObject(String json) {
        ObjectMapper objectMapper = new ObjectMapper();
        Performance performance = new Performance();
        try {
            performance = objectMapper.readValue(json, Performance.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return performance;
    }
}
