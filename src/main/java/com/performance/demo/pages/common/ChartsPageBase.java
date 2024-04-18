package com.performance.demo.pages.common;

import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import com.zebrunner.carina.webdriver.gui.AbstractPage;
import org.openqa.selenium.WebDriver;

public abstract class ChartsPageBase extends AbstractPage {

    public ChartsPageBase(WebDriver driver) {
        super(driver);
    }

    public abstract ExtendedWebElement getElement();

    public abstract ExtendedWebElement getElement2();

    public abstract ExtendedWebElement getLeftMenuButton();
}
