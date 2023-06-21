package com.performance.demo.utils;

import com.performance.demo.annotations.LoginMethod;
import org.springframework.beans.factory.support.*;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.Objects;

public class AOPUtil {

    public static Object setAopConfiguration(String loginMethodName) {
        int index = loginMethodName.lastIndexOf(".");
        int lastIndex = loginMethodName.indexOf("(");
        String className = loginMethodName.substring(0, index);
        String methodName = loginMethodName.substring(index + 1, lastIndex);
        ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("spring.xml");
        DefaultListableBeanFactory context = (DefaultListableBeanFactory) ctx.getBeanFactory();
        GenericBeanDefinition beanDefinition = new GenericBeanDefinition();
        Class<?> serviceClass = null;
        try {
            serviceClass = Class.forName(className);
            if (!serviceClass.getDeclaredMethod(methodName).isAnnotationPresent(LoginMethod.class))
                throw new RuntimeException("Please add @LoginMethod annotation to login method");
            beanDefinition.setBeanClass(serviceClass);
        } catch (ClassNotFoundException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        context.registerBeanDefinition("loginService", beanDefinition);
        return context.getBean("loginService", Objects.requireNonNull(serviceClass));
    }
}
