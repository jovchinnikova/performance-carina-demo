package com.performance.demo.performance.aop;

import com.performance.demo.annotations.LoginMethod;
import com.performance.demo.pages.common.CarinaDescriptionPageBase;
import com.performance.demo.pages.common.LoginPageBase;
import com.performance.demo.pages.common.WelcomePageBase;
import com.zebrunner.carina.utils.factory.ICustomTypePageFactory;

public class LoginService implements ICustomTypePageFactory {

    @LoginMethod
    public CarinaDescriptionPageBase testLogin(){
        WelcomePageBase welcomePage = initPage(getDriver(), WelcomePageBase.class);
        LoginPageBase loginPage = welcomePage.clickNextBtn();
        return loginPage.login();
    }

}
