package com.amazon.pages.android;

import com.amazon.components.android.AndroidHeader;
import com.amazon.pages.common.HomePageBase;
import com.zebrunner.carina.utils.factory.DeviceType;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

@DeviceType(pageType = DeviceType.Type.ANDROID_PHONE, parentClass = HomePageBase.class)
public class HomePage extends HomePageBase {

    @FindBy(id = "navbar")
    private AndroidHeader header;

    public HomePage(WebDriver driver) {
        super(driver);
    }

    @Override
    public AndroidHeader getHeader() {
        return header;
    }
}
