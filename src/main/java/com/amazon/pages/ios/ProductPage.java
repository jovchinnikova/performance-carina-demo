package com.amazon.pages.ios;

import com.amazon.pages.common.ProductPageBase;
import com.zebrunner.carina.utils.factory.DeviceType;
import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

@DeviceType(pageType = DeviceType.Type.IOS_PHONE, parentClass = ProductPageBase.class)
public class ProductPage extends ProductPageBase {

    @FindBy(id = "title")
    private ExtendedWebElement productTitle;

    @FindBy(xpath = "//span[contains(@class,'PriceToPay')]/span[@class='a-offscreen']")
    private ExtendedWebElement iosPrice;

    @FindBy(xpath = "//div[@id='productTitleExpanderRow']//a[@id='acrCustomerReviewLink']/span[1]")
    private ExtendedWebElement anotherRating;

    @FindBy(id = "nav-logo-sprites")
    private ExtendedWebElement logo;

    public ProductPage(WebDriver driver) {
        super(driver);
    }

    @Override
    public String getProductTitle() {
        return productTitle.getText();
    }

    @Override
    public Double getProductPrice() {
        String price = iosPrice.getAttribute("innerText")
                    .replaceAll("\\$", "")
                    .replaceAll(",","");
        return Double.parseDouble(price);
    }

    @Override
    public Double getProductRating() {
        return Double.parseDouble(anotherRating.getText().trim());
    }

    @Override
    public void clickLogoButton() {
        logo.click();
    }
}
