package com.amazon.pages.common;

import com.amazon.components.desktop.CartSideBar;
import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import com.zebrunner.carina.webdriver.gui.AbstractPage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;
import java.util.Random;

public class ProductPageBase extends AbstractPage {

    @FindBy(id = "productTitle")
    private ExtendedWebElement productTitle;

    @FindBy(xpath = "//div[@id='corePrice_feature_div']//span[@class='a-offscreen']")
    protected ExtendedWebElement productPrice;

    @FindBy(xpath = "//div[@id='centerCol']//span[@id='acrPopover']//a/span")
    private ExtendedWebElement productRating;

    @FindBy(id = "native_dropdown_selected_size_name")
    private ExtendedWebElement sizeSelector;

    @FindBy(xpath = "//a[@class='a-dropdown-link']")
    private List<ExtendedWebElement> sizeOptions;

    @FindBy(xpath = "//span[contains(text(),'Qty')]")
    private ExtendedWebElement quantitySelector;

    @FindBy(xpath = "//span[contains(text(),'Qty')]/following-sibling::span")
    private ExtendedWebElement chosenQuantity;

    @FindBy(xpath = "//ul[@role='listbox']/li")
    private List<ExtendedWebElement> quantityOptions;

    @FindBy(id = "add-to-cart-button")
    protected ExtendedWebElement addToCartButton;

    @FindBy(id = "buy-now-button")
    private ExtendedWebElement buyNowButton;

    @FindBy(id = "ewc-content")
    private CartSideBar cartSideBar;

    @FindBy(xpath = "//div[@id='attach-close_sideSheet-link' | @id='atc-details-success']")
    private ExtendedWebElement closeCartPopupButton;

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

    public void addProductToCart() {
        if (sizeSelector.isPresent(3)){
            choseRandomSize();
        }
        addToCartButton.click();
        if (closeCartPopupButton.isPresent(3)){
            waitUntil(ExpectedConditions.elementToBeClickable(closeCartPopupButton.getElement()), 3);
            closeCartPopupButton.click();
        }
    }

    public boolean isCartSidebarPresent() {
        return cartSideBar.isUIObjectPresent();
    }

    public void choseRandomSize() {
        sizeSelector.click();
        sizeOptions.get(new Random().nextInt(sizeOptions.size()))
                .click();
    }

    public void chooseRandomQuantity() {
        quantitySelector.click();
        quantityOptions.get(new Random().nextInt(quantityOptions.size() - 1) + 1)
                .click();
    }

    public Integer getChosenQuantity() {
        return Integer.parseInt(chosenQuantity.getText());
    }

    public CartSideBar getCartSideBar() {
        return cartSideBar;
    }

    public void clickLogoButton() {
        throw new RuntimeException("Method isn't implemented for this platform");
    }
}
