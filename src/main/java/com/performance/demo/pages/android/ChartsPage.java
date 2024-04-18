package com.performance.demo.pages.android;

import com.performance.demo.pages.common.ChartsPageBase;
import com.zebrunner.carina.utils.factory.DeviceType;
import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

@DeviceType(pageType = DeviceType.Type.ANDROID_PHONE, parentClass = ChartsPageBase.class)
public class ChartsPage extends ChartsPageBase {

    public ChartsPage(WebDriver driver) {
        super(driver);
    }

    @FindBy(xpath = "(//android.view.View[@resource-id='ac_chart_2'])[2]")
    private ExtendedWebElement element;

    @FindBy(xpath = "(//android.view.View[@content-desc='AnyChart Trial Version'])[2]")
    private ExtendedWebElement element2;

    @FindBy(className = "android.widget.ImageButton")
    private ExtendedWebElement leftMenuButton;

    @Override
    public ExtendedWebElement getElement() {
        return element;
    }

    @Override
    public ExtendedWebElement getElement2() {
        return element2;
    }

    @Override
    public ExtendedWebElement getLeftMenuButton(){
        return leftMenuButton;
    }
}
