package com.amazon.components;

import com.amazon.pages.common.SearchResultsPageBase;
import com.zebrunner.carina.utils.factory.ICustomTypePageFactory;
import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import com.zebrunner.carina.webdriver.gui.AbstractUIObject;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

public class Menu extends AbstractUIObject implements ICustomTypePageFactory {

    @FindBy(xpath = ".//ul[@data-menu-id='1']//a[@class='hmenu-item']/div | .//ul[@data-menu-id='1']//a[@class='hmenu-item' and text()]")
    private List<ExtendedWebElement> mainMenuItems;

    @FindBy(xpath = ".//ul[@data-menu-id!='1' and contains(@class,'hmenu-visible')]//a")
    protected List<ExtendedWebElement> subMenuItems;

    @FindBy(xpath = ".//ul[contains(@class,'hmenu-visible')]//div[text()='main menu']/..")
    protected ExtendedWebElement backToMenuButton;

    @FindBy(xpath = "//div[text()='see all']")
    private List<ExtendedWebElement> seeAllButtons;

    @FindBy(xpath = ".//div[text()='see less']")
    private List<ExtendedWebElement> seeLessButtons;

    @FindBy(xpath = ".//ul[contains(@class,'hmenu-visible')]//div[contains(@class,'hmenu-title')]")
    private ExtendedWebElement menuTitle;

    public Menu(WebDriver driver, SearchContext searchContext) {
        super(driver, searchContext);
    }

    public void showAllMenuItems() {
        seeAllButtons.stream()
                .filter(ExtendedWebElement::isClickable)
                .forEach(ExtendedWebElement::click);
    }

    public void openMenuItem(String itemTitle) {
        showAllMenuItems();
        mainMenuItems.stream()
                .filter(item -> itemTitle.equals(item.getText()))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("Menu element " + itemTitle + " doesn't exist!")).click();
    }

    public SearchResultsPageBase openSubMenuItem(String itemTitle) {
        subMenuItems.stream()
                .filter(item -> itemTitle.equals(item.getText()))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("Sub menu element " + itemTitle + " doesn't exist!")).clickByActions();
        return initPage(getDriver(), SearchResultsPageBase.class);
    }

    public void goBackToMainMenu() {
        backToMenuButton.clickByActions();
    }

    public boolean isMenuTitlePresent(String title) {
        waitUntil(ExpectedConditions.textToBe(menuTitle.getBy(), title), 5);
        return menuTitle.isPresent();
    }

    public boolean isMenuItemsPresent() {
        List<Boolean> presenceOfElements = mainMenuItems.stream()
                .map(ExtendedWebElement::isElementPresent)
                .collect(Collectors.toList());
        return !presenceOfElements.contains(false);
    }
}
