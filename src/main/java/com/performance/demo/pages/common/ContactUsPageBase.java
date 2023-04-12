package com.performance.demo.pages.common;

import com.zebrunner.carina.utils.mobile.IMobileUtils;
import com.zebrunner.carina.webdriver.gui.AbstractPage;
import org.openqa.selenium.WebDriver;

public abstract class ContactUsPageBase extends AbstractPage implements IMobileUtils {

    public ContactUsPageBase(WebDriver driver) {
        super(driver);
    }

    public abstract void typeName(String name);

    public abstract void typeEmail(String email);

    public abstract void typeQuestion(String question);

    public abstract void submit();

    public abstract boolean isErrorMessagePresent();

    public abstract boolean isRecaptchaPresent();

    public abstract boolean isSuccessMessagePresent();

}
