package com.performance.demo.performance.ios;

import com.performance.demo.performance.ios.pojo.Performance;

import java.util.concurrent.TimeUnit;

import static com.performance.demo.performance.ios.IosPerformanceCollector.startCollecting;
import static com.performance.demo.performance.ios.IosPerformanceCollector.stopCollecting;

public class Main {

    public static void main(String[] args) {
        startCollecting();
        try {
            TimeUnit.SECONDS.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String result = stopCollecting();
        Performance performance = Parser.createPerformanceObject(result);
        DBService.writeData(performance);
    }

}