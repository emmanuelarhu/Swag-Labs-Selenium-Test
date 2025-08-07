package com.saucedemo.pages;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
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

    @FindBy(css = ".error-message-container")
    private WebElement errorMessageContainer;

    @FindBy(css = ".error-message-container .error")
    private WebElement errorBanner;

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

        // Wait a moment for potential error messages or navigation
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

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
        // Check multiple possible error message locations
        boolean standardErrorDisplayed = isElementDisplayed(errorMessage);
        boolean containerErrorDisplayed = false;
        boolean bannerErrorDisplayed = false;

        try {
            containerErrorDisplayed = isElementDisplayed(errorMessageContainer);
        } catch (Exception e) {
            logger.debug("Error message container not found: {}", e.getMessage());
        }

        try {
            bannerErrorDisplayed = isElementDisplayed(errorBanner);
        } catch (Exception e) {
            logger.debug("Error banner not found: {}", e.getMessage());
        }

        // Also check for any element with error-related classes
        boolean anyErrorDisplayed = false;
        try {
            WebElement anyErrorElement = driver.findElement(By.cssSelector(
                    ".error-message-container, [data-test='error'], .error, .error-banner, .field-error"));
            anyErrorDisplayed = anyErrorElement.isDisplayed();
        } catch (Exception e) {
            logger.debug("No error elements found with common selectors");
        }

        boolean isDisplayed = standardErrorDisplayed || containerErrorDisplayed ||
                bannerErrorDisplayed || anyErrorDisplayed;
        logger.info("Error message displayed: {} (standard: {}, container: {}, banner: {}, any: {})",
                isDisplayed, standardErrorDisplayed, containerErrorDisplayed,
                bannerErrorDisplayed, anyErrorDisplayed);
        return isDisplayed;
    }

    @Step("Get error message text")
    public String getErrorMessageText() {
        String errorText = "";

        // Try different error message locations
        try {
            if (isElementDisplayed(errorMessage)) {
                errorText = getElementText(errorMessage);
                logger.info("Standard error message: {}", errorText);
                return errorText;
            }
        } catch (Exception e) {
            logger.debug("Standard error message not available: {}", e.getMessage());
        }

        try {
            if (isElementDisplayed(errorMessageContainer)) {
                errorText = getElementText(errorMessageContainer);
                logger.info("Container error message: {}", errorText);
                return errorText;
            }
        } catch (Exception e) {
            logger.debug("Container error message not available: {}", e.getMessage());
        }

        try {
            if (isElementDisplayed(errorBanner)) {
                errorText = getElementText(errorBanner);
                logger.info("Banner error message: {}", errorText);
                return errorText;
            }
        } catch (Exception e) {
            logger.debug("Banner error message not available: {}", e.getMessage());
        }

        // Try to find any error element with common selectors
        try {
            WebElement anyErrorElement = driver.findElement(By.cssSelector(
                    ".error-message-container, [data-test='error'], .error, .error-banner, .field-error"));
            if (anyErrorElement.isDisplayed()) {
                errorText = anyErrorElement.getText();
                logger.info("Generic error message found: {}", errorText);
                return errorText;
            }
        } catch (Exception e) {
            logger.debug("No error message found with any selector: {}", e.getMessage());
        }

        logger.warn("No error message text found");
        return errorText;
    }

    @Step("Check if error message contains specific text: {expectedText}")
    public boolean errorMessageContains(String expectedText) {
        try {
            String errorText = getErrorMessageText();
            boolean contains = errorText.toLowerCase().contains(expectedText.toLowerCase());
            logger.info("Error message contains '{}': {} (actual message: '{}')",
                    expectedText, contains, errorText);
            return contains;
        } catch (Exception e) {
            logger.error("Error checking error message content: {}", e.getMessage());
            return false;
        }
    }

    @Step("Verify field validation errors are displayed")
    public boolean areFieldValidationErrorsDisplayed() {
        // Check if any input fields have error styling
        try {
            // Look for error styling on input fields
            WebElement firstNameWithError = driver.findElement(By.cssSelector(
                    "[data-test='firstName'].error, [data-test='firstName']:invalid, " +
                            "[data-test='firstName'][aria-invalid='true']"));

            WebElement lastNameWithError = driver.findElement(By.cssSelector(
                    "[data-test='lastName'].error, [data-test='lastName']:invalid, " +
                            "[data-test='lastName'][aria-invalid='true']"));

            boolean hasFieldErrors = firstNameWithError.isDisplayed() || lastNameWithError.isDisplayed();
            logger.info("Field validation errors displayed: {}", hasFieldErrors);
            return hasFieldErrors;

        } catch (Exception e) {
            logger.debug("No field validation errors found: {}", e.getMessage());
            return false;
        }
    }

    @Step("Get first name field value")
    public String getFirstNameValue() {
        try {
            String value = firstNameField.getAttribute("value");
            logger.info("First name field value: {}", value);
            return value;
        } catch (Exception e) {
            logger.error("Error getting first name value: {}", e.getMessage());
            return "";
        }
    }

    @Step("Get last name field value")
    public String getLastNameValue() {
        try {
            String value = lastNameField.getAttribute("value");
            logger.info("Last name field value: {}", value);
            return value;
        } catch (Exception e) {
            logger.error("Error getting last name value: {}", e.getMessage());
            return "";
        }
    }

    @Step("Get postal code field value")
    public String getPostalCodeValue() {
        try {
            String value = postalCodeField.getAttribute("value");
            logger.info("Postal code field value: {}", value);
            return value;
        } catch (Exception e) {
            logger.error("Error getting postal code value: {}", e.getMessage());
            return "";
        }
    }
}