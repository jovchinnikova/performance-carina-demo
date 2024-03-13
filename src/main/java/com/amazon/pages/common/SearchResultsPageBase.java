package com.amazon.pages.common;

import com.amazon.components.ProductCard;
import com.amazon.enums.SortingOption;
import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import com.zebrunner.carina.webdriver.gui.AbstractPage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.stream.Collectors;

public class SearchResultsPageBase extends AbstractPage {

    @FindBy(xpath = "//div[@data-component-type='s-search-result']")
    private List<ProductCard> foundProducts;

    @FindBy(id = "a-autoid-0")
    private ExtendedWebElement sortingSelector;

    @FindBy(xpath = "//div[@id='a-popover-2']//a")
    private List<ExtendedWebElement> sortingOptions;

    public SearchResultsPageBase(WebDriver driver) {
        super(driver);
    }

    public void chooseSortingOption(SortingOption sortingOption) {
        sortingSelector.click();
        sortingOptions.stream()
                .filter(option -> sortingOption.getTitle().equals(option.getText()))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("Not possible to chose this sorting option!")).click();
    }

    public List<ProductCard> getFoundProducts() {
        return foundProducts;
    }

    public ProductCard getRandomProductCard() {
        return foundProducts.get(new Random().nextInt(foundProducts.size()));
    }

    public ProductCard getFullProductCard() {
        List<ProductCard> allInfo = foundProducts.stream()
                .filter(ProductCard::isAllInfoPresent).collect(Collectors.toList());
        return allInfo.get(new Random().nextInt(allInfo.size()));
    }

}
