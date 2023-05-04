package com.performance.demo.performance.dao;

public enum Flow {

    LOGIN_FLOW("login_flow"), SIGN_UP_FLOW("sign_up_flow"), UI_ELEMENTS("ui_elements_flow");

    private final String name;

    Flow(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
