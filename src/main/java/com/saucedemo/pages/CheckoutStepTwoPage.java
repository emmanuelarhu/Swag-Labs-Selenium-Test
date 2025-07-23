package com.saucedemo.pages;

import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CheckoutStepTwoPage extends BasePage {
    private static final Logger logger = LoggerFactory.getLogger(CheckoutStepTwoPage.class);

    @FindBy(css = "[data-test='title']")
    private WebElement pageTitle;

    @FindBy(css = "[data-test='payment-info-label']")
    private WebElement paymentInfoLabel;

    @FindBy(css = "[data-test='payment-info-value']")
    private WebElement paymentInfoValue;

    @FindBy(css = "[data-test='shipping-info-label']")
    private WebElement shippingInfoLabel;

    @FindBy(css = "[data-test='shipping-info-value']")
    private WebElement shippingInfoValue;

    @FindBy(css = "[data-test='total-info-label']")
    private WebElement totalInfoLabel;

    @FindBy(css = "[data-test='subtotal-label']")
    private WebElement subtotalLabel;

    @FindBy(css = "[data-test='tax-label']")
    private WebElement taxLabel;

    @FindBy(css = "[data-test='total-label']")
    private WebElement totalLabel;

    @FindBy(css = "[data-test='finish']")
    private WebElement finishButton;

    @FindBy(css = "[data-test='cancel']")
    private WebElement cancelButton;

    // Regex patterns for price validation
    private static final Pattern SUBTOTAL_PATTERN = Pattern.compile("Item total: \\$(\\d+\\.\\d{2})");
    private static final Pattern TAX_PATTERN = Pattern.compile("Tax: \\$(\\d+\\.\\d{2})");
    private static final Pattern TOTAL_PATTERN = Pattern.compile("Total: \\$(\\d+\\.\\d{2})");
    private static final Pattern SAUCECARD_PATTERN = Pattern.compile("SauceCard #(\\d+)");

    public CheckoutStepTwoPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
        PageFactory.initElements(driver, this);
        logger.info("CheckoutStepTwoPage initialized");
    }

    @Step("Verify checkout step two page is loaded")
    public boolean isCheckoutStepTwoPageLoaded() {
        boolean isLoaded = isElementDisplayed(pageTitle) &&
                getCurrentUrl().contains("checkout-step-two.html") &&
                getElementText(pageTitle).equals("Checkout: Overview");
        logger.info("Checkout step two page loaded: {}", isLoaded);
        return isLoaded;
    }

    @Step("Verify payment information is displayed")
    public boolean isPaymentInfoDisplayed() {
        boolean isDisplayed = isElementDisplayed(paymentInfoLabel) &&
                isElementDisplayed(paymentInfoValue);
        logger.info("Payment info displayed: {}", isDisplayed);
        return isDisplayed;
    }

    @Step("Verify shipping information is displayed")
    public boolean isShippingInfoDisplayed() {
        boolean isDisplayed = isElementDisplayed(shippingInfoLabel) &&
                isElementDisplayed(shippingInfoValue);
        logger.info("Shipping info displayed: {}", isDisplayed);
        return isDisplayed;
    }

    @Step("Verify price total information is displayed")
    public boolean isPriceTotalInfoDisplayed() {
        boolean isDisplayed = isElementDisplayed(totalInfoLabel) &&
                isElementDisplayed(subtotalLabel) &&
                isElementDisplayed(taxLabel) &&
                isElementDisplayed(totalLabel);
        logger.info("Price total info displayed: {}", isDisplayed);
        return isDisplayed;
    }

    @Step("Get payment information")
    public String getPaymentInfo() {
        String paymentInfo = getElementText(paymentInfoValue);
        logger.info("Payment info: {}", paymentInfo);
        return paymentInfo;
    }

    @Step("Verify SauceCard payment method")
    public boolean isSauceCardPaymentMethod() {
        String paymentInfo = getPaymentInfo();
        Matcher matcher = SAUCECARD_PATTERN.matcher(paymentInfo);
        boolean isSauceCard = matcher.find();
        if (isSauceCard) {
            String cardNumber = matcher.group(1);
            logger.info("SauceCard payment method verified with card number: {}", cardNumber);
        } else {
            logger.warn("SauceCard payment method not found. Payment info: {}", paymentInfo);
        }
        return isSauceCard;
    }

    @Step("Get shipping information")
    public String getShippingInfo() {
        String shippingInfo = getElementText(shippingInfoValue);
        logger.info("Shipping info: {}", shippingInfo);
        return shippingInfo;
    }

    @Step("Get subtotal amount")
    public String getSubtotal() {
        String subtotal = getElementText(subtotalLabel);
        logger.info("Subtotal: {}", subtotal);
        return subtotal;
    }

    @Step("Get tax amount")
    public String getTax() {
        String tax = getElementText(taxLabel);
        logger.info("Tax: {}", tax);
        return tax;
    }

    @Step("Get total amount")
    public String getTotal() {
        String total = getElementText(totalLabel);
        logger.info("Total: {}", total);
        return total;
    }

    @Step("Verify subtotal matches expected pattern and value")
    public boolean verifySubtotal(String expectedAmount) {
        String subtotalText = getSubtotal();
        Matcher matcher = SUBTOTAL_PATTERN.matcher(subtotalText);
        if (matcher.find()) {
            String actualAmount = matcher.group(1);
            boolean matches = actualAmount.equals(expectedAmount);
            logger.info("Subtotal verification - Expected: ${}, Actual: ${}, Matches: {}",
                    expectedAmount, actualAmount, matches);
            return matches;
        }
        logger.warn("Subtotal pattern not matched. Text: {}", subtotalText);
        return false;
    }

    @Step("Verify tax matches expected pattern and value")
    public boolean verifyTax(String expectedAmount) {
        String taxText = getTax();
        Matcher matcher = TAX_PATTERN.matcher(taxText);
        if (matcher.find()) {
            String actualAmount = matcher.group(1);
            boolean matches = actualAmount.equals(expectedAmount);
            logger.info("Tax verification - Expected: ${}, Actual: ${}, Matches: {}",
                    expectedAmount, actualAmount, matches);
            return matches;
        }
        logger.warn("Tax pattern not matched. Text: {}", taxText);
        return false;
    }

    @Step("Verify total matches expected pattern and value")
    public boolean verifyTotal(String expectedAmount) {
        String totalText = getTotal();
        Matcher matcher = TOTAL_PATTERN.matcher(totalText);
        if (matcher.find()) {
            String actualAmount = matcher.group(1);
            boolean matches = actualAmount.equals(expectedAmount);
            logger.info("Total verification - Expected: ${}, Actual: ${}, Matches: {}",
                    expectedAmount, actualAmount, matches);
            return matches;
        }
        logger.warn("Total pattern not matched. Text: {}", totalText);
        return false;
    }

    @Step("Click finish button")
    public CheckoutCompletePage clickFinishButton() {
        logger.info("Clicking finish button");
        clickElement(finishButton);
        return new CheckoutCompletePage(driver, wait);
    }

    @Step("Click cancel button")
    public InventoryPage clickCancelButton() {
        logger.info("Clicking cancel button");
        clickElement(cancelButton);
        return new InventoryPage(driver, wait);
    }
}