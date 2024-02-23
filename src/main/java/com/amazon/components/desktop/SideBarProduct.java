package com.amazon.components.desktop;

import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import com.zebrunner.carina.webdriver.gui.AbstractUIObject;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class SideBarProduct extends AbstractUIObject {

    @FindBy(xpath = ".//div[contains(@class,'content')]//img[contains(@class,'product')]")
    private ExtendedWebElement productImg;

    @FindBy(xpath = ".//input[contains(@title,'Delete')]")
    private ExtendedWebElement deleteProductButton;

    @FindBy(xpath = ".//span[contains(@class,'prompt')] | .//input[@name='quantityBox' and not(contains(@class,'hidden'))]")
    private ExtendedWebElement productQuantity;

    public SideBarProduct(WebDriver driver, SearchContext searchContext) {
        super(driver, searchContext);
    }

    public Integer getProductQuantity() {
        if (!productQuantity.getText().equals("")) {
            return Integer.parseInt(productQuantity.getText());
        } else {
            return Integer.parseInt(productQuantity.getAttribute("value"));
        }
    }

    public void deleteFromCart() {
        deleteProductButton.click();
        waitUntil(ExpectedConditions.invisibilityOf(getRootExtendedElement().getElement()), 5);
    }
}
