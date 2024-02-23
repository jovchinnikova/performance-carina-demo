package com.amazon.components.desktop;

import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import com.zebrunner.carina.webdriver.gui.AbstractUIObject;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

public class CartProduct extends AbstractUIObject {

    @FindBy(xpath = ".//input[@data-action='delete']")
    private ExtendedWebElement deleteButton;

    @FindBy(xpath = ".//span[contains(@id,'quantity-label')] | .//div[./input[@name='quantityBox']and not(contains(@class,'hidden'))]")
    private ExtendedWebElement productQuantity;

    public CartProduct(WebDriver driver, SearchContext searchContext) {
        super(driver, searchContext);
    }

    public void deleteFromCart() {
        deleteButton.click();
    }

    public Integer getProductQuantity() {
        if (!productQuantity.getText().equals(""))
            return Integer.parseInt(productQuantity.getText());
        else return Integer.parseInt(productQuantity.getAttribute("value"));
    }
}
