package com.saucedemo.runner;

import org.testng.TestNG;
import org.testng.xml.XmlClass;
import org.testng.xml.XmlSuite;
import org.testng.xml.XmlTest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestRunner {

    public static void main(String[] args) {
        // Create suite
        XmlSuite suite = new XmlSuite();
        suite.setName("SauceDemo Test Suite");
        suite.setVerbose(1);

        // Set suite parameters
        Map<String, String> suiteParameters = new HashMap<>();
        suiteParameters.put("browser", System.getProperty("browser", "chrome"));
        suite.setParameters(suiteParameters);

        // Create test
        XmlTest test = new XmlTest(suite);
        test.setName("SauceDemo E2E Tests");
        test.setPreserveOrder(true);

        // Add test classes
        List<XmlClass> classes = new ArrayList<>();
        classes.add(new XmlClass("com.saucedemo.tests.SauceDemoTest"));
        test.setXmlClasses(classes);

        // Create TestNG instance and run
        TestNG testng = new TestNG();
        List<XmlSuite> suites = new ArrayList<>();
        suites.add(suite);
        testng.setXmlSuites(suites);

        // Add listeners
        testng.addListener("com.saucedemo.listeners.TestListener");

        System.out.println("Running SauceDemo Test Suite...");
        testng.run();
    }
}