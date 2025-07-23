package com.saucedemo.utils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.Alert;
import org.openqa.selenium.TimeoutException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;

public class AlertHandler {
    private static final Logger logger = LoggerFactory.getLogger(AlertHandler.class);
    private static final int DEFAULT_TIMEOUT = 10;

    public static boolean handlePasswordChangeAlert(WebDriver driver) {
        return handlePasswordChangeAlert(driver, DEFAULT_TIMEOUT);
    }

    public static boolean handlePasswordChangeAlert(WebDriver driver, int timeoutSeconds) {
        logger.info("Checking for password change alert/notification");

        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));

            // First, try to handle browser alert if present
            if (handleBrowserAlert(driver, wait)) {
                return true;
            }

            // If no browser alert, check for password change modal/notification
            if (handlePasswordChangeModal(driver, wait)) {
                return true;
            }

            logger.debug("No password change alert or modal found");
            return false;

        } catch (Exception e) {
            logger.debug("No alert present or error handling alert: {}", e.getMessage());
            return false;
        }
    }

    private static boolean handleBrowserAlert(WebDriver driver, WebDriverWait wait) {
        try {
            Alert alert = wait.until(ExpectedConditions.alertIsPresent());
            if (alert != null) {
                String alertText = alert.getText();
                logger.info("Browser alert detected with text: {}", alertText);
                alert.accept();
                logger.info("Browser alert accepted successfully");
                return true;
            }
        } catch (TimeoutException e) {
            logger.debug("No browser alert present");
        }
        return false;
    }

    private static boolean handlePasswordChangeModal(WebDriver driver, WebDriverWait wait) {
        try {
            // Check for Google Password Manager modal elements
            String[] possibleSelectors = {
                    "button[jsname='V67aGc']", // Google Password Manager OK button
                    "button[data-mdc-dialog-action='ok']", // Material Design OK button
                    "button:contains('OK')", // Generic OK button
                    "button[aria-label='OK']", // OK button with aria-label
                    ".modal button[type='button']", // Modal OK button
                    "[role='dialog'] button", // Dialog OK button
                    "button[jsaction*='dismiss']", // Dismiss button
                    "[data-testid='password-manager-ok']", // TestID OK button
                    "button[class*='password'][class*='ok']", // Password-related OK button
                    "input[type='button'][value='OK']" // Input button with OK value
            };

            for (String selector : possibleSelectors) {
                try {
                    var element = driver.findElement(org.openqa.selenium.By.cssSelector(selector));
                    if (element != null && element.isDisplayed() && element.isEnabled()) {
                        element.click();
                        logger.info("Password change modal dismissed using selector: {}", selector);

                        // Wait a moment for the modal to disappear
                        Thread.sleep(1000);
                        return true;
                    }
                } catch (Exception e) {
                    // Continue to next selector
                    logger.debug("Selector {} not found or not clickable", selector);
                }
            }

            // Try XPath selectors for text-based matching
            String[] xpathSelectors = {
                    "//button[text()='OK']",
                    "//button[contains(text(), 'OK')]",
                    "//button[contains(@class, 'ok') or contains(@class, 'confirm')]",
                    "//input[@type='button' and @value='OK']",
                    "//*[@role='dialog']//button[contains(text(), 'OK')]",
                    "//div[contains(@class, 'modal')]//button",
                    "//*[contains(@class, 'password-manager')]//button"
            };

            for (String xpath : xpathSelectors) {
                try {
                    var element = driver.findElement(org.openqa.selenium.By.xpath(xpath));
                    if (element != null && element.isDisplayed() && element.isEnabled()) {
                        element.click();
                        logger.info("Password change modal dismissed using xpath: {}", xpath);

                        // Wait a moment for the modal to disappear
                        Thread.sleep(1000);
                        return true;
                    }
                } catch (Exception e) {
                    // Continue to next xpath
                    logger.debug("XPath {} not found or not clickable", xpath);
                }
            }

        } catch (Exception e) {
            logger.debug("Error handling password change modal: {}", e.getMessage());
        }
        return false;
    }

    public static boolean isPasswordChangeModalPresent(WebDriver driver) {
        try {
            // Check if any password change related modal is visible
            String[] indicators = {
                    "[role='dialog']",
                    ".modal",
                    "[class*='password-manager']",
                    "[class*='password'][class*='modal']",
                    "div[jscontroller]", // Google components
                    "[data-mdc-dialog-container]" // Material Design Dialog
            };

            for (String selector : indicators) {
                try {
                    var elements = driver.findElements(org.openqa.selenium.By.cssSelector(selector));
                    for (var element : elements) {
                        if (element.isDisplayed() &&
                                (element.getText().toLowerCase().contains("password") ||
                                        element.getText().toLowerCase().contains("change") ||
                                        element.getText().toLowerCase().contains("breach"))) {
                            logger.info("Password change modal detected");
                            return true;
                        }
                    }
                } catch (Exception e) {
                    // Continue checking
                }
            }
            return false;
        } catch (Exception e) {
            logger.debug("Error checking for password change modal: {}", e.getMessage());
            return false;
        }
    }
}