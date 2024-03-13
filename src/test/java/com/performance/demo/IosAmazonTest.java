package com.performance.demo;

import com.amazon.components.ProductCard;
import com.amazon.enums.SortingOption;
import com.amazon.pages.common.HomePageBase;
import com.amazon.pages.common.ProductPageBase;
import com.amazon.pages.common.SearchResultsPageBase;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Objects;

public class IosAmazonTest implements IosPerformanceTest {

    @Test
    public void sortPriceIosTest() {
        SearchResultsPageBase searchResultsPage = initPage(getDriver(), SearchResultsPageBase.class);
        searchResultsPage.open();
        searchResultsPage.chooseSortingOption(SortingOption.PRICE_DESC);
        List<Double> prices = searchResultsPage.getFoundProducts().stream()
                .map(ProductCard::getPrice)
                .filter(Objects::nonNull).toList();
        for (int i = 0; i < prices.size() - 1; i++) {
            Assert.assertTrue(prices.get(i) >= prices.get(i+1), "The price isn't sorted descending!");
        }
    }

    @Test
    public void productLogoIosTest() {
        HomePageBase homePage = initPage(getDriver(), HomePageBase.class);
        SearchResultsPageBase searchResultsPage = initPage(getDriver(), SearchResultsPageBase.class);
        searchResultsPage.open();
        ProductCard product = searchResultsPage.getRandomProductCard();
        ProductPageBase productPage = product.openProductPage();
        productPage.clickLogoButton();
        Assert.assertTrue(homePage.isPageOpened(), "Home Page isn't opened!");
    }

}
