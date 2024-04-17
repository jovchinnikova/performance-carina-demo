package com.amazon.pages.common;

import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import com.zebrunner.carina.webdriver.gui.AbstractPage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class ProductPageBase extends AbstractPage {

    @FindBy(id = "productTitle")
    private ExtendedWebElement productTitle;

    @FindBy(xpath = "//div[@id='corePrice_feature_div']//span[@class='a-offscreen']")
    protected ExtendedWebElement productPrice;

    @FindBy(xpath = "//div[@id='centerCol']//span[@id='acrPopover']//a/span")
    private ExtendedWebElement productRating;

    public ProductPageBase(WebDriver driver) {
        super(driver);
        waitUntil(ExpectedConditions.visibilityOf(productTitle.getElement()), 5);
    }

    public String getProductTitle() {
        return productTitle.getText();
    }

    public Double getProductPrice() {
        double price = Double.parseDouble(productPrice.getAttribute("innerText").replaceAll("\\$", "").replaceAll(",",""));
        String result = String.format("%.2f",price);
        return Double.parseDouble(result);
    }

    public Double getProductRating() {
        return Double.parseDouble(productRating.getText().split(" out of")[0]);
    }

    public void clickLogoButton() {
        throw new RuntimeException("Method isn't implemented for this platform");
    }
}
