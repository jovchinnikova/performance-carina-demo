package com.amazon.pages.common;

import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import com.zebrunner.carina.webdriver.gui.AbstractPage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

public abstract class HomePageBase extends AbstractPage {

    @FindBy(xpath = "//span[contains(text(),'Browsing History')]")
    private ExtendedWebElement browsingHistoryLink;

    public HomePageBase(WebDriver driver) {
        super(driver);
    }

    @Override
    public boolean isPageOpened() {
        return browsingHistoryLink.isPresent();
    }
}
