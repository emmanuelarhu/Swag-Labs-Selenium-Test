package com.saucedemo.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BasePage {
    private static final Logger logger = LoggerFactory.getLogger(BasePage.class);
    protected WebDriver driver;
    protected WebDriverWait wait;

    public BasePage(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
    }

    protected void clickElement(WebElement element) {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(element));
            element.click();
            logger.debug("Clicked element: {}", element);
        } catch (Exception e) {
            logger.error("Failed to click element: {}", element, e);
            throw e;
        }
    }

    protected void clearAndType(WebElement element, String text) {
        try {
            wait.until(ExpectedConditions.visibilityOf(element));
            element.clear();
            element.sendKeys(text);
            logger.debug("Typed '{}' into element: {}", text, element);
        } catch (Exception e) {
            logger.error("Failed to type '{}' into element: {}", text, element, e);
            throw e;
        }
    }

    protected String getElementText(WebElement element) {
        try {
            wait.until(ExpectedConditions.visibilityOf(element));
            String text = element.getText();
            logger.debug("Got text '{}' from element: {}", text, element);
            return text;
        } catch (Exception e) {
            logger.error("Failed to get text from element: {}", element, e);
            throw e;
        }
    }

    protected boolean isElementDisplayed(WebElement element) {
        try {
            wait.until(ExpectedConditions.visibilityOf(element));
            boolean isDisplayed = element.isDisplayed();
            logger.debug("Element displayed: {} - {}", isDisplayed, element);
            return isDisplayed;
        } catch (Exception e) {
            logger.debug("Element not displayed or not found: {}", element);
            return false;
        }
    }

    protected void waitForElementToBeVisible(WebElement element) {
        try {
            wait.until(ExpectedConditions.visibilityOf(element));
            logger.debug("Element is now visible: {}", element);
        } catch (Exception e) {
            logger.error("Element did not become visible: {}", element, e);
            throw e;
        }
    }

    protected void waitForElementToBeClickable(WebElement element) {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(element));
            logger.debug("Element is now clickable: {}", element);
        } catch (Exception e) {
            logger.error("Element did not become clickable: {}", element, e);
            throw e;
        }
    }

    public String getCurrentUrl() {
        String url = driver.getCurrentUrl();
        logger.debug("Current URL: {}", url);
        return url;
    }

    public String getPageTitle() {
        String title = driver.getTitle();
        logger.debug("Page title: {}", title);
        return title;
    }
}