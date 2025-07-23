package com.saucedemo.utils;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DriverManager {
    private static final Logger logger = LoggerFactory.getLogger(DriverManager.class);
    private static final ThreadLocal<WebDriver> driverThreadLocal = new ThreadLocal<>();
    private static final String BROWSER_PROPERTY = "browser";
    private static final String HEADLESS_PROPERTY = "headless";

    public static WebDriver getDriver() {
        if (driverThreadLocal.get() == null) {
            String browser = System.getProperty(BROWSER_PROPERTY, "chrome").toLowerCase();
            boolean headless = Boolean.parseBoolean(System.getProperty(HEADLESS_PROPERTY, "false"));

            logger.info("Initializing {} driver (headless: {})", browser, headless);

            switch (browser) {
                case "firefox":
                    setupSimpleFirefoxDriver(headless);
                    break;
                case "chrome":
                default:
                    setupSimpleChromeDriver(headless);
                    break;
            }
        }
        return driverThreadLocal.get();
    }

    private static void setupSimpleChromeDriver(boolean headless) {
        try {
            // Use automatic WebDriverManager setup
            WebDriverManager.chromedriver().setup();

            ChromeOptions options = new ChromeOptions();

            // Minimal essential options
            options.addArguments("--disable-dev-shm-usage");
            options.addArguments("--no-sandbox");
            options.addArguments("--disable-gpu");
            options.addArguments("--remote-allow-origins=*");

            if (headless) {
                options.addArguments("--headless=new");
                options.addArguments("--window-size=1920,1080");
            }

            driverThreadLocal.set(new ChromeDriver(options));
            logger.info("Simple Chrome driver initialized successfully");
        } catch (Exception e) {
            logger.error("Failed to initialize simple Chrome driver: {}", e.getMessage());
            // Try Firefox as fallback
            logger.info("Attempting Firefox fallback...");
            setupSimpleFirefoxDriver(headless);
        }
    }

    private static void setupSimpleFirefoxDriver(boolean headless) {
        try {
            WebDriverManager.firefoxdriver().setup();
            FirefoxOptions options = new FirefoxOptions();

            if (headless) {
                options.addArguments("--headless");
            }

            driverThreadLocal.set(new FirefoxDriver(options));
            logger.info("Simple Firefox driver initialized successfully");
        } catch (Exception e) {
            logger.error("Failed to initialize Firefox driver: {}", e.getMessage());
            throw new RuntimeException("Both Chrome and Firefox driver initialization failed", e);
        }
    }

    public static void closeDriver() {
        WebDriver driver = driverThreadLocal.get();
        if (driver != null) {
            try {
                driver.quit();
                logger.info("WebDriver closed successfully");
            } catch (Exception e) {
                logger.error("Error closing WebDriver: {}", e.getMessage());
            } finally {
                driverThreadLocal.remove();
            }
        }
    }

    public static byte[] takeScreenshot() {
        try {
            WebDriver driver = driverThreadLocal.get();
            if (driver != null) {
                return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
            }
        } catch (Exception e) {
            logger.error("Failed to take screenshot: {}", e.getMessage());
        }
        return null;
    }
}