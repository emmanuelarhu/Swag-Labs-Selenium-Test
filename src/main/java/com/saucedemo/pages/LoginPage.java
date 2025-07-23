package com.saucedemo.pages;

import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoginPage extends BasePage {
    private static final Logger logger = LoggerFactory.getLogger(LoginPage.class);

    @FindBy(css = "[data-test='username']")
    private WebElement usernameField;

    @FindBy(css = "[data-test='password']")
    private WebElement passwordField;

    @FindBy(css = "[data-test='login-button']")
    private WebElement loginButton;

    @FindBy(css = ".login_logo")
    private WebElement swagLabsLogo;

    @FindBy(css = "[data-test='error']")
    private WebElement errorMessage;

    public LoginPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
        PageFactory.initElements(driver, this);
        logger.info("LoginPage initialized");
    }

    @Step("Verify login page is displayed")
    public boolean isLoginPageDisplayed() {
        boolean isDisplayed = isElementDisplayed(swagLabsLogo) &&
                isElementDisplayed(usernameField) &&
                isElementDisplayed(passwordField) &&
                isElementDisplayed(loginButton);
        logger.info("Login page displayed: {}", isDisplayed);
        return isDisplayed;
    }

    @Step("Enter username: {username}")
    public LoginPage enterUsername(String username) {
        logger.info("Entering username: {}", username);
        clearAndType(usernameField, username);
        return this;
    }

    @Step("Enter password")
    public LoginPage enterPassword(String password) {
        logger.info("Entering password");
        clearAndType(passwordField, password);
        return this;
    }

    @Step("Click login button")
    public InventoryPage clickLoginButton() {
        logger.info("Clicking login button");
        clickElement(loginButton);
        return new InventoryPage(driver, wait);
    }

    @Step("Login with credentials - Username: {username}")
    public InventoryPage login(String username, String password) {
        logger.info("Logging in with username: {}", username);
        enterUsername(username);
        enterPassword(password);
        return clickLoginButton();
    }

    @Step("Verify error message is displayed")
    public boolean isErrorMessageDisplayed() {
        boolean isDisplayed = isElementDisplayed(errorMessage);
        logger.info("Error message displayed: {}", isDisplayed);
        return isDisplayed;
    }

    @Step("Get error message text")
    public String getErrorMessageText() {
        String errorText = getElementText(errorMessage);
        logger.info("Error message: {}", errorText);
        return errorText;
    }

    @Step("Verify page title")
    public String getPageTitle() {
        String title = driver.getTitle();
        logger.info("Page title: {}", title);
        return title;
    }
}