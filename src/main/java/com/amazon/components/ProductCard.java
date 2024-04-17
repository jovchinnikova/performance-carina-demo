package com.amazon.components;

import com.amazon.pages.common.ProductPageBase;
import com.zebrunner.carina.utils.factory.ICustomTypePageFactory;
import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import com.zebrunner.carina.webdriver.gui.AbstractUIObject;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

public class ProductCard extends AbstractUIObject implements ICustomTypePageFactory {

    @FindBy(xpath = ".//h2")
    private ExtendedWebElement productTitle;

    @FindBy(xpath = ".//span[contains(@aria-label, 'stars')]")
    private ExtendedWebElement productRating;

    @FindBy(xpath = ".//span[@class='a-price']")
    private ExtendedWebElement productPrice;

    public ProductCard(WebDriver driver, SearchContext searchContext) {
        super(driver, searchContext);
    }

    public Double getPrice() {
        if (productPrice.isPresent()) {
            return Double.parseDouble(productPrice.getText()
                    .replaceAll("\\$", "")
                    .replaceAll("\n", ".")
                    .replaceAll(",", ""));
        }
        return null;
    }

    public ProductPageBase openProductPage() {
        productTitle.click();
        return initPage(getDriver(), ProductPageBase.class);
    }
}
