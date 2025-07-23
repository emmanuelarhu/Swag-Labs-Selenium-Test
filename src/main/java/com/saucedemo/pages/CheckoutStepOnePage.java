package com.saucedemo.pages;

import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CheckoutStepOnePage extends BasePage {
    private static final Logger logger = LoggerFactory.getLogger(CheckoutStepOnePage.class);

    @FindBy(css = "[data-test='title']")
    private WebElement pageTitle;

    @FindBy(css = "[data-test='firstName']")
    private WebElement firstNameField;

    @FindBy(css = "[data-test='lastName']")
    private WebElement lastNameField;

    @FindBy(css = "[data-test='postalCode']")
    private WebElement postalCodeField;

    @FindBy(css = "[data-test='continue']")
    private WebElement continueButton;

    @FindBy(css = "[data-test='cancel']")
    private WebElement cancelButton;

    @FindBy(css = "[data-test='error']")
    private WebElement errorMessage;

    public CheckoutStepOnePage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
        PageFactory.initElements(driver, this);
        logger.info("CheckoutStepOnePage initialized");
    }

    @Step("Verify checkout step one page is loaded")
    public boolean isCheckoutStepOnePageLoaded() {
        boolean isLoaded = isElementDisplayed(pageTitle) &&
                getCurrentUrl().contains("checkout-step-one.html") &&
                getElementText(pageTitle).equals("Checkout: Your Information");
        logger.info("Checkout step one page loaded: {}", isLoaded);
        return isLoaded;
    }

    @Step("Enter first name: {firstName}")
    public CheckoutStepOnePage enterFirstName(String firstName) {
        logger.info("Entering first name: {}", firstName);
        clearAndType(firstNameField, firstName);
        return this;
    }

    @Step("Enter last name: {lastName}")
    public CheckoutStepOnePage enterLastName(String lastName) {
        logger.info("Entering last name: {}", lastName);
        clearAndType(lastNameField, lastName);
        return this;
    }

    @Step("Enter postal code: {postalCode}")
    public CheckoutStepOnePage enterPostalCode(String postalCode) {
        logger.info("Entering postal code: {}", postalCode);
        clearAndType(postalCodeField, postalCode);
        return this;
    }

    @Step("Fill checkout information - First: {firstName}, Last: {lastName}, Postal: {postalCode}")
    public CheckoutStepOnePage fillCheckoutInformation(String firstName, String lastName, String postalCode) {
        logger.info("Filling checkout information - First: {}, Last: {}, Postal: {}",
                firstName, lastName, postalCode);
        enterFirstName(firstName);
        enterLastName(lastName);
        enterPostalCode(postalCode);
        return this;
    }

    @Step("Click continue button")
    public CheckoutStepTwoPage clickContinueButton() {
        logger.info("Clicking continue button");
        clickElement(continueButton);
        return new CheckoutStepTwoPage(driver, wait);
    }

    @Step("Click cancel button")
    public CartPage clickCancelButton() {
        logger.info("Clicking cancel button");
        clickElement(cancelButton);
        return new CartPage(driver, wait);
    }

    @Step("Verify form fields are displayed")
    public boolean areFormFieldsDisplayed() {
        boolean fieldsDisplayed = isElementDisplayed(firstNameField) &&
                isElementDisplayed(lastNameField) &&
                isElementDisplayed(postalCodeField);
        logger.info("Form fields displayed: {}", fieldsDisplayed);
        return fieldsDisplayed;
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
}