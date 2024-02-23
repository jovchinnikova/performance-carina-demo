package com.performance.demo;

import com.amazon.components.desktop.ProductCard;
import com.amazon.enums.SortingOption;
import com.amazon.pages.common.HomePageBase;
import com.amazon.pages.common.ProductPageBase;
import com.amazon.pages.common.SearchResultsPageBase;
import com.performance.demo.performance.ios.DBService;
import com.performance.demo.performance.ios.IosPerformanceCollector;
import com.performance.demo.performance.ios.Parser;
import com.performance.demo.performance.ios.pojo.Performance;
import com.zebrunner.carina.core.IAbstractTest;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class IosAmazonTest implements IAbstractTest {

    @Test
    public void sortPriceIosTest() {
        SearchResultsPageBase searchResultsPage = initPage(getDriver(), SearchResultsPageBase.class);
        IosPerformanceCollector.startCollecting();
        searchResultsPage.open();
        searchResultsPage.chooseSortingOption(SortingOption.PRICE_DESC);
        List<Double> prices = searchResultsPage.getFoundProducts().stream()
                .map(ProductCard::getPrice)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        for (int i = 0; i < prices.size() - 1; i++) {
            Assert.assertTrue(prices.get(i) >= prices.get(i+1), "The price isn't sorted descending!");
        }
        String response = IosPerformanceCollector.stopCollecting();
        Performance performance = Parser.createPerformanceObject(response);
        DBService.writeData(performance);
    }

    @Test
    public void sortRatingIosTest() {
        SearchResultsPageBase searchResultsPage = initPage(getDriver(), SearchResultsPageBase.class);
        IosPerformanceCollector.startCollecting();
        searchResultsPage.open();
        searchResultsPage.chooseSortingOption(SortingOption.RATING);
        List<Double> ratings = searchResultsPage.getFoundProducts().stream()
                .map(ProductCard::getRating)
                .collect(Collectors.toList());
        for (int i = 0; i < ratings.size() - 1; i++) {
            Assert.assertTrue(ratings.get(i) >= ratings.get(i+1), "The rating isn't sorted descending!");
        }
        String response = IosPerformanceCollector.stopCollecting();
        Performance performance = Parser.createPerformanceObject(response);
        DBService.writeData(performance);
    }

    @Test
    public void openProductCardIOSTest() {
        SoftAssert softAssert = new SoftAssert();
        SearchResultsPageBase searchResultsPage = initPage(getDriver(), SearchResultsPageBase.class);
        IosPerformanceCollector.startCollecting();
        searchResultsPage.open();
        ProductCard product = searchResultsPage.getFullProductCard();
        String expectedTitle = product.getProductTitle();
        Double expectedPrice = product.getPrice();
        Double expectedRating = product.getRating();
        ProductPageBase productPage = product.openProductPage();
        softAssert.assertEquals(expectedTitle, productPage.getProductTitle(),
                "The title isn't as expected!");
        softAssert.assertEquals(expectedPrice, productPage.getProductPrice(),
                "The price isn't as expected!");
        softAssert.assertEquals(expectedRating, productPage.getProductRating(),
                "The rating isn't as expected!");
        softAssert.assertAll();
        String response = IosPerformanceCollector.stopCollecting();
        Performance performance = Parser.createPerformanceObject(response);
        DBService.writeData(performance);
    }

    @Test
    public void productLogoIosTest() {
        HomePageBase homePage = initPage(getDriver(), HomePageBase.class);
        IosPerformanceCollector.startCollecting();
        SearchResultsPageBase searchResultsPage = initPage(getDriver(), SearchResultsPageBase.class);
        searchResultsPage.open();
        ProductCard product = searchResultsPage.getRandomProductCard();
        ProductPageBase productPage = product.openProductPage();
        productPage.clickLogoButton();
        Assert.assertTrue(homePage.isPageOpened(), "Home Page isn't opened!");
        String response = IosPerformanceCollector.stopCollecting();
        Performance performance = Parser.createPerformanceObject(response);
        DBService.writeData(performance);
    }
}
