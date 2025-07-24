package com.saucedemo.tests;

import com.saucedemo.base.BaseTest;
import com.saucedemo.pages.*;
import com.saucedemo.utils.TestDataReader;
import io.qameta.allure.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

@Epic("SauceDemo E2E Testing")
@Feature("Complete Shopping Flow")
public class SauceDemoTest extends BaseTest {
    private static final Logger logger = LoggerFactory.getLogger(SauceDemoTest.class);

    @Test(description = "Complete shopping flow from login to order completion")
    @Story("User can complete full shopping journey")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Test covers the complete user journey: login, add items to cart, checkout, and order completion")
    public void testCompleteShoppingFlow() {
        logger.info("Starting complete shopping flow test");

        // Step 1: Verify login page and login
        LoginPage loginPage = new LoginPage(driver, wait);
        Assert.assertTrue(loginPage.isLoginPageDisplayed(), "Login page should be displayed");
        Assert.assertEquals(loginPage.getPageTitle(), "Swag Labs", "Page title should be 'Swag Labs'");

        String username = TestDataReader.getTestDataAsString("credentials.username");
        String password = TestDataReader.getTestDataAsString("credentials.password");

        InventoryPage inventoryPage = loginPage.login(username, password);
        takeScreenshot();

        // Step 2: Verify inventory page
        Assert.assertTrue(inventoryPage.isInventoryPageLoaded(), "Inventory page should be loaded");
        Assert.assertTrue(inventoryPage.isPageTitleDisplayed(), "Page title 'Products' should be displayed");
        Assert.assertTrue(inventoryPage.areProductNamesDisplayed(), "Product names should be displayed correctly");

        // Step 3: Add items to cart
        inventoryPage.addBackpackToCart();
        Assert.assertTrue(inventoryPage.isBackpackAddedToCart(), "Backpack should be added to cart");

        inventoryPage.addBikeLightToCart();
        Assert.assertTrue(inventoryPage.isBikeLightAddedToCart(), "Bike light should be added to cart");

        Assert.assertEquals(inventoryPage.getCartBadgeCount(), "2", "Cart should show 2 items");
        takeScreenshot();

        // Step 4: Navigate to cart and verify items
        CartPage cartPage = inventoryPage.clickShoppingCartLink();
        Assert.assertTrue(cartPage.isCartPageLoaded(), "Cart page should be loaded");
        Assert.assertTrue(cartPage.areCartHeadersDisplayed(), "Cart headers should be displayed");
        Assert.assertEquals(cartPage.getCartItemCount(), 2, "Cart should contain 2 items");
        Assert.assertTrue(cartPage.areItemsInCart("Sauce Labs Backpack", "Sauce Labs Bike Light"),
                "Cart should contain expected items");
        takeScreenshot();

        // Step 5: Proceed to checkout step one
        CheckoutStepOnePage checkoutStepOnePage = cartPage.clickCheckoutButton();
        Assert.assertTrue(checkoutStepOnePage.isCheckoutStepOnePageLoaded(),
                "Checkout step one page should be loaded");
        Assert.assertTrue(checkoutStepOnePage.areFormFieldsDisplayed(), "Form fields should be displayed");

        // Step 6: Fill checkout information
        String firstName = TestDataReader.getTestDataAsString("checkout.firstName");
        String lastName = TestDataReader.getTestDataAsString("checkout.lastName");
        String postalCode = TestDataReader.getTestDataAsString("checkout.postalCode");

        checkoutStepOnePage.fillCheckoutInformation(firstName, lastName, postalCode);
        takeScreenshot();

        // Step 7: Continue to checkout step two
        CheckoutStepTwoPage checkoutStepTwoPage = checkoutStepOnePage.clickContinueButton();
        Assert.assertTrue(checkoutStepTwoPage.isCheckoutStepTwoPageLoaded(),
                "Checkout step two page should be loaded");
        Assert.assertTrue(checkoutStepTwoPage.isPaymentInfoDisplayed(), "Payment info should be displayed");
        Assert.assertTrue(checkoutStepTwoPage.isShippingInfoDisplayed(), "Shipping info should be displayed");
        Assert.assertTrue(checkoutStepTwoPage.isPriceTotalInfoDisplayed(), "Price total info should be displayed");

        // Step 8: Verify payment and pricing information using regex
        Assert.assertTrue(checkoutStepTwoPage.isSauceCardPaymentMethod(),
                "Payment method should be SauceCard");

        String expectedSubtotal = TestDataReader.getTestDataAsString("pricing.subtotal");
        String expectedTax = TestDataReader.getTestDataAsString("pricing.tax");
        String expectedTotal = TestDataReader.getTestDataAsString("pricing.total");

        Assert.assertTrue(checkoutStepTwoPage.verifySubtotal(expectedSubtotal),
                "Subtotal should match expected amount: $" + expectedSubtotal);
        Assert.assertTrue(checkoutStepTwoPage.verifyTax(expectedTax),
                "Tax should match expected amount: $" + expectedTax);
        Assert.assertTrue(checkoutStepTwoPage.verifyTotal(expectedTotal),
                "Total should match expected amount: $" + expectedTotal);
        takeScreenshot();

        // Step 9: Complete the order
        CheckoutCompletePage checkoutCompletePage = checkoutStepTwoPage.clickFinishButton();
        Assert.assertTrue(checkoutCompletePage.isCheckoutCompletePageLoaded(),
                "Checkout complete page should be loaded");
        Assert.assertTrue(checkoutCompletePage.areOrderCompletionElementsDisplayed(),
                "Order completion elements should be displayed");
        Assert.assertTrue(checkoutCompletePage.isOrderCompletionMessageCorrect(),
                "Order completion message should be correct");
        Assert.assertTrue(checkoutCompletePage.isPonyExpressImageDisplayed(),
                "Pony express image should be displayed");
        takeScreenshot();

        // Step 10: Return to products page
        InventoryPage finalInventoryPage = checkoutCompletePage.clickBackToProductsButton();
        Assert.assertTrue(finalInventoryPage.isInventoryPageLoaded(),
                "Should return to inventory page after completing order");
        Assert.assertTrue(finalInventoryPage.isPageTitleDisplayed(),
                "Products page title should be displayed");
        takeScreenshot();

        logger.info("Complete shopping flow test completed successfully");
    }

    @Test(description = "Verify login page elements and title", priority = 1)
    @Story("Login page validation")
    @Severity(SeverityLevel.NORMAL)
    public void testLoginPageElements() {
        logger.info("Starting login page elements test");

        LoginPage loginPage = new LoginPage(driver, wait);
        Assert.assertTrue(loginPage.isLoginPageDisplayed(), "Login page elements should be displayed");
        Assert.assertEquals(loginPage.getPageTitle(), "Swag Labs", "Page title should be 'Swag Labs'");
        takeScreenshot();

        logger.info("Login page elements test completed successfully");
    }

//    @Test(description = "Complete shopping flow with numeric postal code", priority = 3)
//    @Story("User can complete shopping journey with numeric postal code")
//    @Severity(SeverityLevel.CRITICAL)
//    @Description("Test covers the complete user journey with numeric postal code: login, add items to cart, checkout with numbers-only postal code, and order completion")
//    public void testCompleteShoppingFlowWithNumericPostalCode() {
//        logger.info("Starting complete shopping flow test with numeric postal code");
//
//        // Step 1: Verify login page and login
//        LoginPage loginPage = new LoginPage(driver, wait);
//        Assert.assertTrue(loginPage.isLoginPageDisplayed(), "Login page should be displayed");
//        Assert.assertEquals(loginPage.getPageTitle(), "Swag Labs", "Page title should be 'Swag Labs'");
//
//        String username = TestDataReader.getTestDataAsString("credentials.username");
//        String password = TestDataReader.getTestDataAsString("credentials.password");
//
//        InventoryPage inventoryPage = loginPage.login(username, password);
//        takeScreenshot();
//
//        // Step 2: Verify inventory page
//        Assert.assertTrue(inventoryPage.isInventoryPageLoaded(), "Inventory page should be loaded");
//        Assert.assertTrue(inventoryPage.isPageTitleDisplayed(), "Page title 'Products' should be displayed");
//        Assert.assertTrue(inventoryPage.areProductNamesDisplayed(), "Product names should be displayed correctly");
//
//        // Step 3: Add items to cart
//        inventoryPage.addBackpackToCart();
//        Assert.assertTrue(inventoryPage.isBackpackAddedToCart(), "Backpack should be added to cart");
//
//        inventoryPage.addBikeLightToCart();
//        Assert.assertTrue(inventoryPage.isBikeLightAddedToCart(), "Bike light should be added to cart");
//
//        Assert.assertEquals(inventoryPage.getCartBadgeCount(), "2", "Cart should show 2 items");
//        takeScreenshot();
//
//        // Step 4: Navigate to cart and verify items
//        CartPage cartPage = inventoryPage.clickShoppingCartLink();
//        Assert.assertTrue(cartPage.isCartPageLoaded(), "Cart page should be loaded");
//        Assert.assertTrue(cartPage.areCartHeadersDisplayed(), "Cart headers should be displayed");
//        Assert.assertEquals(cartPage.getCartItemCount(), 2, "Cart should contain 2 items");
//        Assert.assertTrue(cartPage.areItemsInCart("Sauce Labs Backpack", "Sauce Labs Bike Light"),
//                "Cart should contain expected items");
//        takeScreenshot();
//
//        // Step 5: Proceed to checkout step one
//        CheckoutStepOnePage checkoutStepOnePage = cartPage.clickCheckoutButton();
//        Assert.assertTrue(checkoutStepOnePage.isCheckoutStepOnePageLoaded(),
//                "Checkout step one page should be loaded");
//        Assert.assertTrue(checkoutStepOnePage.areFormFieldsDisplayed(), "Form fields should be displayed");
//
//        // Step 6: Fill checkout information with NUMERIC postal code
//        String firstName = TestDataReader.getTestDataAsString("checkout.firstName");
//        String lastName = TestDataReader.getTestDataAsString("checkout.lastName");
//        String postalCodeNumeric = TestDataReader.getTestDataAsString("checkout.postalCodeNumeric"); // 0248071613
//
//        logger.info("Using numeric postal code: {}", postalCodeNumeric);
//        checkoutStepOnePage.fillCheckoutInformation(firstName, lastName, postalCodeNumeric);
//        takeScreenshot();
//
//        // Step 7: Continue to checkout step two
//        CheckoutStepTwoPage checkoutStepTwoPage = checkoutStepOnePage.clickContinueButton();
//        Assert.assertTrue(checkoutStepTwoPage.isCheckoutStepTwoPageLoaded(),
//                "Checkout step two page should be loaded");
//        Assert.assertTrue(checkoutStepTwoPage.isPaymentInfoDisplayed(), "Payment info should be displayed");
//        Assert.assertTrue(checkoutStepTwoPage.isShippingInfoDisplayed(), "Shipping info should be displayed");
//        Assert.assertTrue(checkoutStepTwoPage.isPriceTotalInfoDisplayed(), "Price total info should be displayed");
//
//        // Step 8: Verify payment and pricing information using regex
//        Assert.assertTrue(checkoutStepTwoPage.isSauceCardPaymentMethod(),
//                "Payment method should be SauceCard");
//
//        String expectedSubtotal = TestDataReader.getTestDataAsString("pricing.subtotal");
//        String expectedTax = TestDataReader.getTestDataAsString("pricing.tax");
//        String expectedTotal = TestDataReader.getTestDataAsString("pricing.total");
//
//        Assert.assertTrue(checkoutStepTwoPage.verifySubtotal(expectedSubtotal),
//                "Subtotal should match expected amount: $" + expectedSubtotal);
//        Assert.assertTrue(checkoutStepTwoPage.verifyTax(expectedTax),
//                "Tax should match expected amount: $" + expectedTax);
//        Assert.assertTrue(checkoutStepTwoPage.verifyTotal(expectedTotal),
//                "Total should match expected amount: $" + expectedTotal);
//        takeScreenshot();
//
//        // Step 9: Complete the order
//        CheckoutCompletePage checkoutCompletePage = checkoutStepTwoPage.clickFinishButton();
//        Assert.assertTrue(checkoutCompletePage.isCheckoutCompletePageLoaded(),
//                "Checkout complete page should be loaded");
//        Assert.assertTrue(checkoutCompletePage.areOrderCompletionElementsDisplayed(),
//                "Order completion elements should be displayed");
//        Assert.assertTrue(checkoutCompletePage.isOrderCompletionMessageCorrect(),
//                "Order completion message should be correct");
//        Assert.assertTrue(checkoutCompletePage.isPonyExpressImageDisplayed(),
//                "Pony express image should be displayed");
//        takeScreenshot();
//
//        // Step 10: Return to products page
//        InventoryPage finalInventoryPage = checkoutCompletePage.clickBackToProductsButton();
//        Assert.assertTrue(finalInventoryPage.isInventoryPageLoaded(),
//                "Should return to inventory page after completing order");
//        Assert.assertTrue(finalInventoryPage.isPageTitleDisplayed(),
//                "Products page title should be displayed");
//        takeScreenshot();
//
//        logger.info("Complete shopping flow test with numeric postal code completed successfully");
//    }

    @Test(description = "Verify inventory page after login", priority = 2)
    @Story("Inventory page validation")
    @Severity(SeverityLevel.NORMAL)
    public void testInventoryPageAfterLogin() {
        logger.info("Starting inventory page test");

        LoginPage loginPage = new LoginPage(driver, wait);
        String username = TestDataReader.getTestDataAsString("credentials.username");
        String password = TestDataReader.getTestDataAsString("credentials.password");

        InventoryPage inventoryPage = loginPage.login(username, password);
        Assert.assertTrue(inventoryPage.isInventoryPageLoaded(), "Inventory page should be loaded");
        Assert.assertTrue(inventoryPage.areProductNamesDisplayed(), "Product names should be displayed");
        takeScreenshot();

        logger.info("Inventory page test completed successfully");
    }
}