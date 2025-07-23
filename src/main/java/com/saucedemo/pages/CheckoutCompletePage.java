package com.saucedemo.pages;

import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CheckoutCompletePage extends BasePage {
    private static final Logger logger = LoggerFactory.getLogger(CheckoutCompletePage.class);

    @FindBy(css = "[data-test='title']")
    private WebElement pageTitle;

    @FindBy(css = "[data-test='complete-header']")
    private WebElement completeHeader;

    @FindBy(css = "[data-test='complete-text']")
    private WebElement completeText;

    @FindBy(css = "[data-test='back-to-products']")
    private WebElement backToProductsButton;

    @FindBy(css = "[data-test='pony-express']")
    private WebElement ponyExpressImage;

    public CheckoutCompletePage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
        PageFactory.initElements(driver, this);
        logger.info("CheckoutCompletePage initialized");
    }

    @Step("Verify checkout complete page is loaded")
    public boolean isCheckoutCompletePageLoaded() {
        boolean isLoaded = isElementDisplayed(pageTitle) &&
                getCurrentUrl().contains("checkout-complete.html") &&
                getElementText(pageTitle).equals("Checkout: Complete!");
        logger.info("Checkout complete page loaded: {}", isLoaded);
        return isLoaded;
    }

    @Step("Verify order completion elements are displayed")
    public boolean areOrderCompletionElementsDisplayed() {
        boolean elementsDisplayed = isElementDisplayed(completeHeader) &&
                isElementDisplayed(completeText) &&
                isElementDisplayed(ponyExpressImage) &&
                isElementDisplayed(backToProductsButton);
        logger.info("Order completion elements displayed: {}", elementsDisplayed);
        return elementsDisplayed;
    }

    @Step("Get completion header text")
    public String getCompletionHeaderText() {
        String headerText = getElementText(completeHeader);
        logger.info("Completion header text: {}", headerText);
        return headerText;
    }

    @Step("Get completion message text")
    public String getCompletionMessageText() {
        String messageText = getElementText(completeText);
        logger.info("Completion message text: {}", messageText);
        return messageText;
    }

    @Step("Verify order completion message")
    public boolean isOrderCompletionMessageCorrect() {
        String headerText = getCompletionHeaderText();
        String messageText = getCompletionMessageText();

        boolean headerCorrect = headerText.equals("Thank you for your order!");
        boolean messageCorrect = messageText.contains("Your order has been dispatched") &&
                messageText.contains("pony can get there");

        logger.info("Order completion message verification - Header: {}, Message: {}",
                headerCorrect, messageCorrect);
        return headerCorrect && messageCorrect;
    }

    @Step("Click back to products button")
    public InventoryPage clickBackToProductsButton() {
        logger.info("Clicking back to products button");
        clickElement(backToProductsButton);
        return new InventoryPage(driver, wait);
    }

    @Step("Verify back to products button is displayed")
    public boolean isBackToProductsButtonDisplayed() {
        boolean isDisplayed = isElementDisplayed(backToProductsButton);
        logger.info("Back to products button displayed: {}", isDisplayed);
        return isDisplayed;
    }

    @Step("Verify pony express image is displayed")
    public boolean isPonyExpressImageDisplayed() {
        boolean isDisplayed = isElementDisplayed(ponyExpressImage);
        logger.info("Pony express image displayed: {}", isDisplayed);
        return isDisplayed;
    }
}