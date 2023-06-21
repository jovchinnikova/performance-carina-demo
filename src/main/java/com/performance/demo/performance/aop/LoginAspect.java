package com.performance.demo.performance.aop;
import com.performance.demo.performance.PerformanceListener;

public class LoginAspect {

    public void collectLogin() {
        PerformanceListener.collectLoginTime();
    }

}
