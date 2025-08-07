package com.saucedemo.utils;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.List;

public class handleAnyPopups {
    private static final Logger logger = LoggerFactory.getLogger(handleAnyPopups.class);
    private final WebDriver driver;
    private final WebDriverWait wait;

    public handleAnyPopups(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
    }

    /**
     * Handles various browser popups that might appear during testing
     * This method should be called after navigation or form interactions
     */
    public void handleBrowserPopups() {
        try {
            // Handle Google Password Manager popup
            handlePasswordManagerPopup();

            // Handle other potential popups
            handleGenericOKPopup();

            // Small wait to ensure popups are processed
            Thread.sleep(500);

        } catch (Exception e) {
            logger.debug("No popups detected or error handling popups: {}", e.getMessage());
        }
    }

    /**
     * Specifically handles Google Password Manager "Change your password" popup
     */
    private void handlePasswordManagerPopup() {
        try {
            // Multiple selectors for password manager popup
            String[] passwordPopupSelectors = {
                    "button:contains('OK')",                    // Generic OK button
                    "[data-test='OK']",                         // Data test attribute
                    "button[class*='password']",                // Button with password in class
                    "//button[text()='OK']",                    // XPath for OK button
                    "//button[contains(text(),'OK')]",          // XPath contains OK
                    ".password-popup button",                   // CSS class selector
                    "[role='button'][aria-label*='OK']"        // ARIA label selector
            };

            for (String selector : passwordPopupSelectors) {
                if (tryClickPopupButton(selector)) {
                    logger.info("Successfully handled password manager popup using selector: {}", selector);
                    return;
                }
            }

            // Try to find any visible button with common popup text
            List<WebElement> buttons = driver.findElements(By.tagName("button"));
            for (WebElement button : buttons) {
                if (button.isDisplayed() && button.isEnabled()) {
                    String buttonText = button.getText().toLowerCase();
                    if (buttonText.contains("ok") || buttonText.contains("continue") ||
                            buttonText.contains("dismiss") || buttonText.contains("close")) {
                        button.click();
                        logger.info("Clicked popup button with text: {}", button.getText());
                        Thread.sleep(500);
                        return;
                    }
                }
            }

        } catch (Exception e) {
            logger.debug("No password manager popup found or clickable: {}", e.getMessage());
        }
    }

    /**
     * Handles generic OK/Close/Dismiss popups
     */
    private void handleGenericOKPopup() {
        try {
            // Common popup button selectors
            String[] genericSelectors = {
                    "button[type='button']",
                    ".modal button",
                    ".popup button",
                    ".dialog button",
                    "[role='dialog'] button",
                    ".notification button"
            };

            for (String selector : genericSelectors) {
                List<WebElement> elements = driver.findElements(By.cssSelector(selector));
                for (WebElement element : elements) {
                    if (element.isDisplayed() && element.isEnabled()) {
                        String text = element.getText().toLowerCase();
                        if (text.contains("ok") || text.contains("close") ||
                                text.contains("dismiss") || text.contains("continue")) {
                            element.click();
                            logger.info("Clicked generic popup button: {}", text);
                            Thread.sleep(300);
                            return;
                        }
                    }
                }
            }

        } catch (Exception e) {
            logger.debug("No generic popup found: {}", e.getMessage());
        }
    }

    /**
     * Attempts to click a popup button using the provided selector
     */
    private boolean tryClickPopupButton(String selector) {
        try {
            WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(2));

            WebElement element;
            if (selector.startsWith("//")) {
                // XPath selector
                element = shortWait.until(ExpectedConditions.elementToBeClickable(By.xpath(selector)));
            } else if (selector.startsWith("button:contains")) {
                // Skip CSS pseudo-selectors (not supported by Selenium)
                return false;
            } else {
                // CSS selector
                element = shortWait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(selector)));
            }

            if (element.isDisplayed() && element.isEnabled()) {
                element.click();
                Thread.sleep(300);
                return true;
            }
        } catch (Exception e) {
            // Element not found or not clickable - this is expected
            logger.debug("Selector {} not found or not clickable: {}", selector, e.getMessage());
        }
        return false;
    }

    /**
     * Checks if any popup is currently visible on the page
     */
    public boolean isPopupVisible() {
        try {
            // Check for common popup containers
            String[] popupContainerSelectors = {
                    ".modal",
                    ".popup",
                    ".dialog",
                    "[role='dialog']",
                    ".notification",
                    ".alert"
            };

            for (String selector : popupContainerSelectors) {
                List<WebElement> elements = driver.findElements(By.cssSelector(selector));
                for (WebElement element : elements) {
                    if (element.isDisplayed()) {
                        logger.info("Popup detected with selector: {}", selector);
                        return true;
                    }
                }
            }

            // Check for buttons that commonly appear in popups
            List<WebElement> buttons = driver.findElements(By.tagName("button"));
            for (WebElement button : buttons) {
                if (button.isDisplayed() && button.getText().toLowerCase().contains("ok")) {
                    logger.info("Potential popup button detected: {}", button.getText());
                    return true;
                }
            }

        } catch (Exception e) {
            logger.debug("Error checking for popups: {}", e.getMessage());
        }
        return false;
    }

    /**
     * Waits for popups to disappear after handling them
     */
    public void waitForPopupsToDisappear() {
        try {
            WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(3));

            // Wait for common popup elements to become invisible
            shortWait.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector(".modal")));

        } catch (Exception e) {
            // Timeout is expected if no popups are present
            logger.debug("Popup disappearance timeout (expected): {}", e.getMessage());
        }
    }
}