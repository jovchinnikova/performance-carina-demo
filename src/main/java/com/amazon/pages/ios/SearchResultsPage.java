package com.amazon.pages.ios;

import com.amazon.components.desktop.FilterMenu;
import com.amazon.components.ios.IosFilterMenu;
import com.amazon.enums.SearchOptions;
import com.amazon.enums.SortingOption;
import com.amazon.pages.common.SearchResultsPageBase;
import com.zebrunner.carina.utils.factory.DeviceType;
import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

@DeviceType(pageType = DeviceType.Type.IOS_PHONE, parentClass = SearchResultsPageBase.class)
public class SearchResultsPage extends SearchResultsPageBase {

    @FindBy(xpath = "//h4")
    private ExtendedWebElement productCategory;

    @FindBy(id = "s-all-filters")
    private ExtendedWebElement filterButton;

    @FindBy(id = "dropdown-content-s-all-filters")
    private IosFilterMenu filterMenu;

    public SearchResultsPage(WebDriver driver) {
        super(driver);
        StringBuilder searchUrl = new StringBuilder();
        for (String word: SearchOptions.getRandomSearch().split(" ")) {
            searchUrl.append(word).append("+");
        }
        setPageURL("/s?k=" + searchUrl);
    }

    @Override
    public String getProductCategory() {
        return productCategory.getText();
    }

    @Override
    public void chooseSortingOption(SortingOption sortingOption) {
        filterButton.click();
        filterMenu.chooseSortingOption(sortingOption);
    }

    @Override
    public FilterMenu getFilterMenu() {
        filterButton.click();
        return filterMenu;
    }
}
