package com.saucedemo.listeners;

import com.saucedemo.base.BaseTest;
import io.qameta.allure.Attachment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class TestListener implements ITestListener {
    private static final Logger logger = LoggerFactory.getLogger(TestListener.class);

    @Override
    public void onTestStart(ITestResult result) {
        logger.info("Starting test: {}", result.getMethod().getMethodName());
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        logger.info("Test passed: {}", result.getMethod().getMethodName());
    }

    @Override
    public void onTestFailure(ITestResult result) {
        logger.error("Test failed: {} - {}", result.getMethod().getMethodName(),
                result.getThrowable().getMessage());

        Object testClass = result.getInstance();
        if (testClass instanceof BaseTest) {
            BaseTest baseTest = (BaseTest) testClass;
            attachScreenshot(baseTest.takeScreenshot());
        }
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        logger.warn("Test skipped: {}", result.getMethod().getMethodName());
    }

    @Attachment(value = "Failure Screenshot", type = "image/png")
    private byte[] attachScreenshot(byte[] screenshot) {
        return screenshot;
    }
}