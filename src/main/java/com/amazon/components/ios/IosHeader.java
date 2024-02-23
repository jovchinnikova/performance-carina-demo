package com.amazon.components.ios;

import com.amazon.components.android.AndroidHeader;
import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

public class IosHeader extends AndroidHeader {

    @FindBy(id = "icp-touch-link-country")
    private ExtendedWebElement chooseCountryLink;

    @FindBy(id = "icp-dropdown")
    private ExtendedWebElement chooseCountrySelector;

    @FindBy(xpath = "//a[text()='Canada']")
    private ExtendedWebElement canadaOption;

    @FindBy(id = "icp-save-button-announce")
    private ExtendedWebElement goWebsiteButton;

    @FindBy(xpath = "//div[@id='hmenu-canvas']")
    private IosMenu menu;

    public IosHeader(WebDriver driver, SearchContext searchContext) {
        super(driver, searchContext);
    }

    @Override
    public void chooseUSLocation(String zipCode){
        chooseCountryLink.click();
        chooseCountrySelector.click();
        canadaOption.click();
        goWebsiteButton.click();
    }

    @Override
    public IosMenu getMenu() {
        navHamburger.click();
        return menu;
    }
}
