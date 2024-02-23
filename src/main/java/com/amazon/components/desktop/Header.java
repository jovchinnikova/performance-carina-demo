package com.amazon.components.desktop;

import com.amazon.components.Menu;
import com.amazon.pages.common.SearchResultsPageBase;
import com.zebrunner.carina.utils.factory.ICustomTypePageFactory;
import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import com.zebrunner.carina.webdriver.gui.AbstractUIObject;
import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;
import java.util.Random;

public class Header extends AbstractUIObject implements ICustomTypePageFactory {

    @FindBy(id = "nav-logo-sprites")
    private ExtendedWebElement logo;

    @FindBy(id = "nav-global-location-popover-link")
    private ExtendedWebElement deliveryLocation;

    @FindBy(xpath = "//input[contains(@aria-label,'zip code')]")
    protected ExtendedWebElement zipCodeInput;

    @FindBy(xpath = "//input[@class='a-button-input'][./following-sibling::span[text()='Apply']]")
    protected ExtendedWebElement applyButton;

    @FindBy(xpath = "//div[@class='a-popover-footer']//input")
    private ExtendedWebElement continueButton;

    @FindBy(id = "twotabsearchtextbox")
    private ExtendedWebElement searchField;

    @FindBy(id = "nav-search-submit-button")
    private ExtendedWebElement searchButton;

    @FindBy(xpath = ".//div[@class='two-pane-results-container']//span")
    private List<ExtendedWebElement> searchSuggestions;

    @FindBy(id = "nav-cart")
    private ExtendedWebElement cartButton;

    @FindBy(id = "nav-hamburger-menu")
    protected ExtendedWebElement navHamburger;

    @FindBy(id = "hmenu-customer-profile")
    private ExtendedWebElement signInField;

    @FindBy(id = "nav-link-accountList-nav-line-1")
    private ExtendedWebElement userSection;

    @FindBy(xpath = "//div[@id='hmenu-canvas']")
    private Menu menu;

    public Header(WebDriver driver, SearchContext searchContext) {
        super(driver, searchContext);
    }

    public void searchGoods(String searchText) {
        searchField.type(searchText);
        searchButton.click();
    }

    public void chooseUSLocation(String zipCode){
        deliveryLocation.click();
        zipCodeInput.type(zipCode);
        applyButton.click();
        continueButton.click();
    }

    public SearchResultsPageBase openRandomSuggestedGoods() {
        searchField.click();
        By by = By.xpath("//div[@class='two-pane-results-container']//span");
        waitUntil(ExpectedConditions.numberOfElementsToBeMoreThan(by, 1), 5);
        searchSuggestions.get(new Random().nextInt(searchSuggestions.size()))
                .click();
        return initPage(getDriver(), SearchResultsPageBase.class);
    }

    public void openLoginPage() {
        navHamburger.click();
        waitUntil(ExpectedConditions.visibilityOf(signInField.getElement()), 5);
        signInField.click();
    }

    public boolean isUserLogined(String userName) {
        return userSection.getText().contains(userName);
    }

    public Menu getMenu() {
        navHamburger.click();
        return menu;
    }
}
