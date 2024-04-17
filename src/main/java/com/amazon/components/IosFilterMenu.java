package com.amazon.components;

import com.amazon.enums.SortingOption;
import com.zebrunner.carina.utils.android.AndroidService;
import com.zebrunner.carina.utils.mobile.IMobileUtils;
import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import com.zebrunner.carina.webdriver.gui.AbstractUIObject;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

import java.util.List;
import java.util.NoSuchElementException;

public class IosFilterMenu extends AbstractUIObject {

    @FindBy(xpath = "//a/span[contains(text(),'results')]")
    private ExtendedWebElement applyFilterButton;

    @FindBy(xpath = ".//div[@id='filter-sort']//div[@class='a-section']/span/..")
    private List<ExtendedWebElement> sortingOptions;

    public IosFilterMenu(WebDriver driver, SearchContext searchContext) {
        super(driver, searchContext);
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
}
