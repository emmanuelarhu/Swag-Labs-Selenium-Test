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

import java.util.List;

public class CartPage extends BasePage {
    private static final Logger logger = LoggerFactory.getLogger(CartPage.class);

    @FindBy(css = "[data-test='title']")
    private WebElement pageTitle;

    @FindBy(css = "[data-test='checkout']")
    private WebElement checkoutButton;

    @FindBy(css = "[data-test='continue-shopping']")
    private WebElement continueShoppingButton;

    @FindBy(css = "[data-test='cart-quantity-label']")
    private WebElement quantityLabel;

    @FindBy(css = "[data-test='cart-desc-label']")
    private WebElement descriptionLabel;

    @FindBy(css = ".cart_item")
    private List<WebElement> cartItems;

    @FindBy(css = ".inventory_item_name")
    private List<WebElement> itemNames;

    @FindBy(css = ".inventory_item_price")
    private List<WebElement> itemPrices;

    @FindBy(css = ".cart_quantity")
    private List<WebElement> itemQuantities;

    public CartPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
        PageFactory.initElements(driver, this);
        logger.info("CartPage initialized");
    }

    @Step("Verify cart page is loaded")
    public boolean isCartPageLoaded() {
        boolean isLoaded = isElementDisplayed(pageTitle) &&
                getCurrentUrl().contains("cart.html") &&
                getElementText(pageTitle).equals("Your Cart");
        logger.info("Cart page loaded: {}", isLoaded);
        return isLoaded;
    }

    @Step("Verify cart headers are displayed")
    public boolean areCartHeadersDisplayed() {
        boolean qtyDisplayed = isElementDisplayed(quantityLabel) &&
                getElementText(quantityLabel).equals("QTY");
        boolean descDisplayed = isElementDisplayed(descriptionLabel) &&
                getElementText(descriptionLabel).equals("Description");

        logger.info("Cart headers displayed - QTY: {}, Description: {}", qtyDisplayed, descDisplayed);
        return qtyDisplayed && descDisplayed;
    }

    @Step("Get number of items in cart")
    public int getCartItemCount() {
        int count = cartItems.size();
        logger.info("Cart item count: {}", count);
        return count;
    }

    @Step("Verify specific items are in cart")
    public boolean areItemsInCart(String... expectedItems) {
        for (String expectedItem : expectedItems) {
            boolean found = itemNames.stream()
                    .anyMatch(item -> getElementText(item).contains(expectedItem));
            if (!found) {
                logger.warn("Item '{}' not found in cart", expectedItem);
                return false;
            }
        }
        logger.info("All expected items found in cart: {}", String.join(", ", expectedItems));
        return true;
    }

    @Step("Click checkout button")
    public CheckoutStepOnePage clickCheckoutButton() {
        logger.info("Clicking checkout button");
        clickElement(checkoutButton);
        return new CheckoutStepOnePage(driver, wait);
    }

    @Step("Click continue shopping button")
    public InventoryPage clickContinueShoppingButton() {
        logger.info("Clicking continue shopping button");
        clickElement(continueShoppingButton);
        return new InventoryPage(driver, wait);
    }

    @Step("Get item prices from cart")
    public String[] getItemPrices() {
        String[] prices = itemPrices.stream()
                .map(this::getElementText)
                .toArray(String[]::new);
        logger.info("Item prices in cart: {}", String.join(", ", prices));
        return prices;
    }

    @Step("Get item quantities from cart")
    public String[] getItemQuantities() {
        String[] quantities = itemQuantities.stream()
                .map(this::getElementText)
                .toArray(String[]::new);
        logger.info("Item quantities in cart: {}", String.join(", ", quantities));
        return quantities;
    }


    @Step("Check if checkout button is available")
    public boolean isCheckoutButtonAvailable() {
        try {
            boolean isAvailable = isElementDisplayed(checkoutButton) && checkoutButton.isEnabled();
            logger.info("Checkout button available: {}", isAvailable);
            return isAvailable;
        } catch (Exception e) {
            logger.info("Checkout button not available: {}", e.getMessage());
            return false;
        }
    }

    @Step("Verify cart is empty")
    public boolean isCartEmpty() {
        boolean isEmpty = getCartItemCount() == 0;
        logger.info("Cart is empty: {}", isEmpty);
        return isEmpty;
    }

    @Step("Get empty cart message")
    public String getEmptyCartMessage() {
        try {
            // Look for common empty cart message elements
            WebElement emptyMessage = driver.findElement(By.cssSelector(
                    ".cart_item_label, .empty-cart, .no-items, .cart-empty-message"));
            String message = getElementText(emptyMessage);
            logger.info("Empty cart message: {}", message);
            return message;
        } catch (Exception e) {
            logger.debug("No empty cart message found: {}", e.getMessage());
            return "";
        }
    }
}