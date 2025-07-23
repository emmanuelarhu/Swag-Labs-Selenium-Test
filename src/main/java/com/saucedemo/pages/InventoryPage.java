package com.saucedemo.pages;

import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InventoryPage extends BasePage {
    private static final Logger logger = LoggerFactory.getLogger(InventoryPage.class);

    @FindBy(css = "[data-test='title']")
    private WebElement pageTitle;

    @FindBy(css = "[data-test='shopping-cart-link']")
    private WebElement shoppingCartLink;

    @FindBy(css = "[data-test='add-to-cart-sauce-labs-backpack']")
    private WebElement addBackpackButton;

    @FindBy(css = "[data-test='add-to-cart-sauce-labs-bike-light']")
    private WebElement addBikeLightButton;

    @FindBy(css = "[data-test='remove-sauce-labs-backpack']")
    private WebElement removeBackpackButton;

    @FindBy(css = "[data-test='remove-sauce-labs-bike-light']")
    private WebElement removeBikeLightButton;

    @FindBy(css = ".shopping_cart_badge")
    private WebElement cartBadge;

    @FindBy(css = "[data-test='item-4-title-link'] [data-test='inventory-item-name']")
    private WebElement backpackTitle;

    @FindBy(css = "[data-test='item-0-title-link'] [data-test='inventory-item-name']")
    private WebElement bikeLightTitle;

    @FindBy(css = ".inventory_item_price")
    private WebElement itemPrice;

    public InventoryPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
        PageFactory.initElements(driver, this);
        logger.info("InventoryPage initialized");
    }

    @Step("Verify inventory page is loaded")
    public boolean isInventoryPageLoaded() {
        boolean isLoaded = isElementDisplayed(pageTitle) &&
                getCurrentUrl().contains("inventory.html");
        logger.info("Inventory page loaded: {}", isLoaded);
        return isLoaded;
    }

    @Step("Verify page title is 'Products'")
    public boolean isPageTitleDisplayed() {
        boolean isDisplayed = isElementDisplayed(pageTitle) &&
                getElementText(pageTitle).equals("Products");
        logger.info("Page title 'Products' displayed: {}", isDisplayed);
        return isDisplayed;
    }

    @Step("Add Sauce Labs Backpack to cart")
    public InventoryPage addBackpackToCart() {
        logger.info("Adding Sauce Labs Backpack to cart");
        clickElement(addBackpackButton);
        return this;
    }

    @Step("Add Sauce Labs Bike Light to cart")
    public InventoryPage addBikeLightToCart() {
        logger.info("Adding Sauce Labs Bike Light to cart");
        clickElement(addBikeLightButton);
        return this;
    }

    @Step("Verify backpack is added to cart")
    public boolean isBackpackAddedToCart() {
        boolean isAdded = isElementDisplayed(removeBackpackButton);
        logger.info("Backpack added to cart: {}", isAdded);
        return isAdded;
    }

    @Step("Verify bike light is added to cart")
    public boolean isBikeLightAddedToCart() {
        boolean isAdded = isElementDisplayed(removeBikeLightButton);
        logger.info("Bike light added to cart: {}", isAdded);
        return isAdded;
    }

    @Step("Get cart badge count")
    public String getCartBadgeCount() {
        if (isElementDisplayed(cartBadge)) {
            String count = getElementText(cartBadge);
            logger.info("Cart badge count: {}", count);
            return count;
        }
        logger.info("Cart badge not displayed");
        return "0";
    }

    @Step("Click shopping cart link")
    public CartPage clickShoppingCartLink() {
        logger.info("Clicking shopping cart link");
        clickElement(shoppingCartLink);
        return new CartPage(driver, wait);
    }

    @Step("Verify product names are displayed correctly")
    public boolean areProductNamesDisplayed() {
        boolean backpackDisplayed = isElementDisplayed(backpackTitle) &&
                getElementText(backpackTitle).contains("Sauce Labs Backpack");
        boolean bikeLightDisplayed = isElementDisplayed(bikeLightTitle) &&
                getElementText(bikeLightTitle).contains("Sauce Labs Bike Light");

        logger.info("Backpack title displayed: {}, Bike Light title displayed: {}",
                backpackDisplayed, bikeLightDisplayed);
        return backpackDisplayed && bikeLightDisplayed;
    }
}