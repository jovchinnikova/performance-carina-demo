package com.performance.demo.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface PerformanceTest {

    String flowName() default "";

    String userName() default "";

    boolean collectLoginTime() default false;

    boolean collectExecutionTime() default false;

}
