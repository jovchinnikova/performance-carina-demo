package com.performance.demo.pages.ios;

import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import com.zebrunner.carina.webdriver.gui.AbstractPage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

public class SaucePage extends AbstractPage {
    
    @FindBy(id = "i_am_an_id")
    private ExtendedWebElement divElement;
    
    @FindBy(id = "comments")
    private ExtendedWebElement textComment;
    
    public SaucePage(WebDriver driver) {
        super(driver);
    }

    public void verifyElementText() {
        divElement.assertElementWithTextPresent("I am a div");
    }
    
    public void sendComment() {
        divElement.assertElementWithTextPresent("I am a div");
    }

}
