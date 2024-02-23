package com.amazon.components.desktop;

import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import com.zebrunner.carina.webdriver.gui.AbstractUIObject;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

import java.util.List;

public class CartSideBar extends AbstractUIObject {

    @FindBy(xpath = ".//span[contains(@class,'subtotal')]/span")
    private ExtendedWebElement cartSubTotal;

    @FindBy(xpath = ".//div[@data-asin]")
    private List<SideBarProduct> sideBarProducts;

    public CartSideBar(WebDriver driver, SearchContext searchContext) {
        super(driver, searchContext);
    }

    public Double getSubtotalAmount() {
        return Double.parseDouble(cartSubTotal.getText().replaceAll("\\$", "").replaceAll(",",""));
    }

    public SideBarProduct getFirstSideBarProduct() {
        return sideBarProducts.get(0);
    }
}
