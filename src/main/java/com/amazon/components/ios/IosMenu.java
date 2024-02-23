package com.amazon.components.ios;

import com.amazon.components.Menu;
import com.amazon.pages.common.SearchResultsPageBase;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebDriver;

import java.util.NoSuchElementException;

public class IosMenu extends Menu {

    public IosMenu(WebDriver driver, SearchContext searchContext) {
        super(driver, searchContext);
    }

    @Override
    public void goBackToMainMenu() {
        backToMenuButton.click();
    }

    @Override
    public SearchResultsPageBase openSubMenuItem(String itemTitle) {
        subMenuItems.stream()
                .filter(item -> itemTitle.equals(item.getText()))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("Sub menu element " + itemTitle + " doesn't exist!")).click();
        return initPage(getDriver(), SearchResultsPageBase.class);
    }
}
