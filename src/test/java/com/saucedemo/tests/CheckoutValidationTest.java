package com.saucedemo.tests;

import com.saucedemo.base.BaseTest;
import com.saucedemo.pages.*;
import com.saucedemo.utils.TestDataReader;
import io.qameta.allure.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

@Epic("SauceDemo Checkout Testing")
@Feature("Checkout Field Validation and Empty Cart Scenarios")
public class CheckoutValidationTest extends BaseTest {
    private static final Logger logger = LoggerFactory.getLogger(CheckoutValidationTest.class);

    @Test(description = "Verify checkout form validation for First Name with numbers only", priority = 1)
    @Story("Checkout Form Validation")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Test validates that First Name field shows error when numbers are entered")
    public void testCheckoutFirstNameNumbersValidation() {
        logger.info("Starting checkout First Name numbers validation test");

        // Step 1: Login and add items to cart
        LoginPage loginPage = new LoginPage(driver, wait);
        String username = TestDataReader.getTestDataAsString("credentials.username");
        String password = TestDataReader.getTestDataAsString("credentials.password");

        InventoryPage inventoryPage = loginPage.login(username, password);
        handleAnyPopups();

        // Add items to cart
        inventoryPage.addBackpackToCart();
        inventoryPage.addBikeLightToCart();

        // Step 2: Navigate to checkout
        CartPage cartPage = inventoryPage.clickShoppingCartLink();
        CheckoutStepOnePage checkoutPage = cartPage.clickCheckoutButton();

        // Step 3: Fill form with numbers in First Name
        checkoutPage.enterFirstName("12345");
        checkoutPage.enterLastName("Arhu");
        checkoutPage.enterPostalCode("Kumasi");
        checkoutPage.clickContinueButton();

        // Step 4: Verify error message appears
        Assert.assertTrue(checkoutPage.isErrorMessageDisplayed(),
                "Error message should be displayed for numeric First Name");

        String errorMessage = checkoutPage.getErrorMessageText();
        Assert.assertTrue(errorMessage.contains("First Name should be letters") ||
                        errorMessage.contains("letters only") ||
                        errorMessage.contains("alphabetic"),
                "Error message should indicate First Name should be letters only. Actual: " + errorMessage);

        takeScreenshot();
        logger.info("First Name numbers validation test completed successfully");
    }

    @Test(description = "Verify checkout form validation for Last Name with numbers only", priority = 2)
    @Story("Checkout Form Validation")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Test validates that Last Name field shows error when numbers are entered")
    public void testCheckoutLastNameNumbersValidation() {
        logger.info("Starting checkout Last Name numbers validation test");

        // Step 1: Login and add items to cart
        LoginPage loginPage = new LoginPage(driver, wait);
        String username = TestDataReader.getTestDataAsString("credentials.username");
        String password = TestDataReader.getTestDataAsString("credentials.password");

        InventoryPage inventoryPage = loginPage.login(username, password);
        handleAnyPopups();

        // Add items to cart
        inventoryPage.addBackpackToCart();

        // Step 2: Navigate to checkout
        CartPage cartPage = inventoryPage.clickShoppingCartLink();
        CheckoutStepOnePage checkoutPage = cartPage.clickCheckoutButton();

        // Step 3: Fill form with numbers in Last Name
        checkoutPage.enterFirstName("Emmanuel");
        checkoutPage.enterLastName("67890");
        checkoutPage.enterPostalCode("12345");
        checkoutPage.clickContinueButton();

        // Step 4: Verify error message appears
        Assert.assertTrue(checkoutPage.isErrorMessageDisplayed(),
                "Error message should be displayed for numeric Last Name");

        String errorMessage = checkoutPage.getErrorMessageText();
        Assert.assertTrue(errorMessage.contains("Last Name should be letters") ||
                        errorMessage.contains("letters only") ||
                        errorMessage.contains("alphabetic"),
                "Error message should indicate Last Name should be letters only. Actual: " + errorMessage);

        takeScreenshot();
        logger.info("Last Name numbers validation test completed successfully");
    }

    @Test(description = "Verify checkout form accepts valid mixed postal code", priority = 3)
    @Story("Checkout Form Validation")
    @Severity(SeverityLevel.NORMAL)
    @Description("Test validates that postal code accepts mix of letters and numbers")
    public void testCheckoutValidPostalCodeMixed() {
        logger.info("Starting checkout valid mixed postal code test");

        // Step 1: Login and add items to cart
        LoginPage loginPage = new LoginPage(driver, wait);
        String username = TestDataReader.getTestDataAsString("credentials.username");
        String password = TestDataReader.getTestDataAsString("credentials.password");

        InventoryPage inventoryPage = loginPage.login(username, password);
        handleAnyPopups();

        // Add items to cart
        inventoryPage.addBackpackToCart();

        // Step 2: Navigate to checkout
        CartPage cartPage = inventoryPage.clickShoppingCartLink();
        CheckoutStepOnePage checkoutPage = cartPage.clickCheckoutButton();

        // Step 3: Fill form with valid data including mixed postal code
        checkoutPage.enterFirstName("Emmanuel");
        checkoutPage.enterLastName("Arhu");
        checkoutPage.enterPostalCode("K1A0A6"); // Canadian postal code format

        // Step 4: Continue to next step
        CheckoutStepTwoPage checkoutStepTwo = checkoutPage.clickContinueButton();

        // Step 5: Verify successful navigation to step two
        Assert.assertTrue(checkoutStepTwo.isCheckoutStepTwoPageLoaded(),
                "Should navigate to checkout step two with valid mixed postal code");

        takeScreenshot();
        logger.info("Valid mixed postal code test completed successfully");
    }

    @Test(description = "Verify checkout with empty cart shows appropriate message", priority = 4)
    @Story("Empty Cart Checkout")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Test validates behavior when trying to checkout with empty cart")
    public void testCheckoutWithEmptyCart() {
        logger.info("Starting checkout with empty cart test");

        // Step 1: Login without adding items to cart
        LoginPage loginPage = new LoginPage(driver, wait);
        String username = TestDataReader.getTestDataAsString("credentials.username");
        String password = TestDataReader.getTestDataAsString("credentials.password");

        InventoryPage inventoryPage = loginPage.login(username, password);
        handleAnyPopups();

        // Step 2: Navigate directly to cart (empty)
        CartPage cartPage = inventoryPage.clickShoppingCartLink();

        // Step 3: Verify cart is empty
        Assert.assertEquals(cartPage.getCartItemCount(), 0, "Cart should be empty");

        // Step 4: Check if checkout button is present/clickable
        boolean isCheckoutAvailable = cartPage.isCheckoutButtonAvailable();

        if (isCheckoutAvailable) {
            // If checkout button is available, click it and verify behavior
            CheckoutStepOnePage checkoutPage = cartPage.clickCheckoutButton();

            // Fill valid form data
            checkoutPage.enterFirstName("Emmanuel");
            checkoutPage.enterLastName("Arhu");
            checkoutPage.enterPostalCode("Kumasi");

            // Try to continue
            checkoutPage.clickContinueButton();

            // Check if error message appears or if we're still on the same page
            if (checkoutPage.isErrorMessageDisplayed()) {
                String errorMessage = checkoutPage.getErrorMessageText();
                logger.info("Error message for empty cart checkout: {}", errorMessage);
                Assert.assertTrue(errorMessage.contains("cart") || errorMessage.contains("item") ||
                                errorMessage.contains("product") || errorMessage.contains("empty"),
                        "Error should indicate issue with empty cart");
            } else {
                // Check if we're still on checkout step one (shouldn't proceed)
                Assert.assertTrue(checkoutPage.isCheckoutStepOnePageLoaded(),
                        "Should remain on checkout step one with empty cart");
            }
        } else {
            logger.info("Checkout button not available for empty cart - expected behavior");
        }

        takeScreenshot();
        logger.info("Empty cart checkout test completed successfully");
    }

    @Test(description = "Verify comprehensive checkout validation with all invalid data", priority = 5)
    @Story("Comprehensive Checkout Validation")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Test validates multiple field validation errors simultaneously")
    public void testCheckoutComprehensiveValidation() {
        logger.info("Starting comprehensive checkout validation test");

        // Step 1: Login and add items to cart
        LoginPage loginPage = new LoginPage(driver, wait);
        String username = TestDataReader.getTestDataAsString("credentials.username");
        String password = TestDataReader.getTestDataAsString("credentials.password");

        InventoryPage inventoryPage = loginPage.login(username, password);
        handleAnyPopups();

        // Add items to cart
        inventoryPage.addBackpackToCart();
        inventoryPage.addBikeLightToCart();

        // Step 2: Navigate to checkout
        CartPage cartPage = inventoryPage.clickShoppingCartLink();
        CheckoutStepOnePage checkoutPage = cartPage.clickCheckoutButton();

        // Step 3: Fill form with all invalid data
        checkoutPage.enterFirstName("123456");      // Numbers only - should fail
        checkoutPage.enterLastName("789012");       // Numbers only - should fail
        checkoutPage.enterPostalCode("");           // Empty - may fail

        // Step 4: Try to continue
        checkoutPage.clickContinueButton();

        // Step 5: Verify error message appears
        Assert.assertTrue(checkoutPage.isErrorMessageDisplayed(),
                "Error message should be displayed for invalid form data");

        String errorMessage = checkoutPage.getErrorMessageText();
        logger.info("Comprehensive validation error message: {}", errorMessage);

        // Should contain reference to field validation issues
        boolean hasValidationError = errorMessage.contains("letters") ||
                errorMessage.contains("alphabetic") ||
                errorMessage.contains("required") ||
                errorMessage.contains("invalid");

        Assert.assertTrue(hasValidationError,
                "Error message should indicate validation issues. Actual: " + errorMessage);

        takeScreenshot();
        logger.info("Comprehensive checkout validation test completed successfully");
    }

    @Test(description = "Verify successful checkout with valid letter-only names", priority = 6)
    @Story("Valid Checkout Flow")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Test validates successful checkout with properly formatted names")
    public void testSuccessfulCheckoutWithValidNames() {
        logger.info("Starting successful checkout with valid names test");

        // Step 1: Login and add items to cart
        LoginPage loginPage = new LoginPage(driver, wait);
        String username = TestDataReader.getTestDataAsString("credentials.username");
        String password = TestDataReader.getTestDataAsString("credentials.password");

        InventoryPage inventoryPage = loginPage.login(username, password);
        handleAnyPopups();

        // Add items to cart
        inventoryPage.addBackpackToCart();
        inventoryPage.addBikeLightToCart();

        // Step 2: Navigate to checkout
        CartPage cartPage = inventoryPage.clickShoppingCartLink();
        CheckoutStepOnePage checkoutPage = cartPage.clickCheckoutButton();

        // Step 3: Fill form with valid letter-only names
        checkoutPage.enterFirstName("Emmanuel");     // Letters only - should pass
        checkoutPage.enterLastName("Arhu");          // Letters only - should pass
        checkoutPage.enterPostalCode("A1B2C3");      // Mixed format - should pass

        // Step 4: Continue to next step
        CheckoutStepTwoPage checkoutStepTwo = checkoutPage.clickContinueButton();

        // Step 5: Verify successful navigation
        Assert.assertTrue(checkoutStepTwo.isCheckoutStepTwoPageLoaded(),
                "Should successfully navigate to checkout step two with valid data");

        // Step 6: Verify checkout overview details
        Assert.assertTrue(checkoutStepTwo.isPaymentInfoDisplayed(),
                "Payment information should be displayed");
        Assert.assertTrue(checkoutStepTwo.isShippingInfoDisplayed(),
                "Shipping information should be displayed");

        // Step 7: Complete the checkout
        CheckoutCompletePage completePage = checkoutStepTwo.clickFinishButton();
        Assert.assertTrue(completePage.isCheckoutCompletePageLoaded(),
                "Should complete checkout successfully");

        takeScreenshot();
        logger.info("Successful checkout with valid names test completed successfully");
    }
}