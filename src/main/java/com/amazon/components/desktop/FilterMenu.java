package com.amazon.components.desktop;

import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import com.zebrunner.carina.webdriver.gui.AbstractUIObject;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

import java.util.List;
import java.util.NoSuchElementException;

public class FilterMenu extends AbstractUIObject {

    @FindBy(xpath = ".//div[@id='reviewsRefinements']//section")
    private List<ExtendedWebElement> ratingOptions;

    @FindBy(id = "low-price")
    private ExtendedWebElement lowPriceLimit;

    @FindBy(id = "high-price")
    private ExtendedWebElement highPriceLimit;

    @FindBy(xpath = ".//input[@class='a-button-input']")
    private ExtendedWebElement choosePriceButton;

    public FilterMenu(WebDriver driver, SearchContext searchContext) {
        super(driver, searchContext);
    }

    public void chooseRatingFilter(Integer starsBound) {
        ratingOptions.stream()
                .filter(element -> element.getAttribute("aria-label").contains(starsBound.toString()))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("Unable to choose this rating option!"))
                .click();
    }

    public void setPriceLimits(Double lowPrice, Double highPrice) {
        lowPriceLimit.type(lowPrice.toString());
        highPriceLimit.type(highPrice.toString());
        choosePriceButton.click();
    }

    public Double setLowPriceLimit(Double lowPrice) {
        lowPriceLimit.type(lowPrice.toString());
        choosePriceButton.click();
        return lowPrice;
    }
}
