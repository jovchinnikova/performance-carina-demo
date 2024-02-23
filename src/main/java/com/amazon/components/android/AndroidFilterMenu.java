package com.amazon.components.android;

import com.amazon.components.desktop.FilterMenu;
import com.amazon.enums.SortingOption;
import com.zebrunner.carina.utils.android.AndroidService;
import com.zebrunner.carina.utils.mobile.IMobileUtils;
import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import org.openqa.selenium.Keys;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

import java.util.List;
import java.util.NoSuchElementException;

public class AndroidFilterMenu extends FilterMenu {

    @FindBy(xpath = ".//input[contains(@id,'lower-bound-slider')]")
    private ExtendedWebElement lowPriceSlider;

    @FindBy(xpath = "//*[@id='sf-all-price']/following-sibling::a//span")
    private List<ExtendedWebElement> filterPriceOptions;

    @FindBy(xpath = "//a/span[contains(text(),'results')]")
    private ExtendedWebElement applyFilterButton;

    @FindBy(xpath = ".//div[@id='filter-p_72']//div[contains(@aria-label,'& Up')]")
    private List<ExtendedWebElement> ratingOptions;

    @FindBy(xpath = ".//div[@id='filter-sort']//div[@class='a-section']/span/..")
    private List<ExtendedWebElement> sortingOptions;

    public AndroidFilterMenu(WebDriver driver, SearchContext searchContext) {
        super(driver, searchContext);
    }

    @Override
    public Double setLowPriceLimit(Double lowPrice) {
        IMobileUtils mobileUtils = new AndroidService();
        double lowLimit;
        if (lowPriceSlider.isPresent()) {
            for (int i = 0; i < lowPrice.intValue(); i++) {
                lowPriceSlider.sendKeys(Keys.RIGHT);
            }
            lowLimit = lowPrice;
        } else {
            ExtendedWebElement firstLowPriceOption = filterPriceOptions.get(1);
            int elementHeight = firstLowPriceOption.getSize().getHeight();
            int windowHeight = getDriver().manage().window().getSize().height;
            while (firstLowPriceOption.getLocation().getY() + elementHeight > windowHeight) {
                mobileUtils.swipeUp(2, 15);
            }
            String limit = firstLowPriceOption.getText().split(" to")[0].split("\\$")[1];
            firstLowPriceOption.click();
            lowLimit = Double.parseDouble(limit);
        }
        applyFilterButton.click();
        return lowLimit;
    }

    public void chooseSortingOption(SortingOption sortingOption) {
        IMobileUtils mobileUtils = new AndroidService();
        ExtendedWebElement sorted = sortingOptions.stream()
                .filter(option -> sortingOption.getTitle().equals(option.getText()))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("Not possible to chose this sorting option!"));
        int elementHeight = sorted.getSize().getHeight();
        int windowHeight = getDriver().manage().window().getSize().height;
        while (sorted.getLocation().getY() + elementHeight > windowHeight) {
            mobileUtils.swipeUp(2, 15);
        }
        sorted.click();
        applyFilterButton.click();
    }

    @Override
    public void chooseRatingFilter(Integer starsBound) {
        IMobileUtils mobileUtils = new AndroidService();
        ExtendedWebElement ratingOption = ratingOptions.stream()
                .filter(element -> element.getAttribute("aria-label").contains(starsBound.toString()))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("Unable to choose this rating option!"));
        int elementHeight = ratingOption.getSize().getHeight();
        int windowHeight = getDriver().manage().window().getSize().height;
        while (ratingOption.getLocation().getY() + elementHeight > windowHeight) {
            mobileUtils.swipeUp(2, 15);
        }
        mobileUtils.tap(ratingOption);
        applyFilterButton.click();
    }
}
