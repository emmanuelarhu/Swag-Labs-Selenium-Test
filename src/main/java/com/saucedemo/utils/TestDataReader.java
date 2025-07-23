package com.saucedemo.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;

public class TestDataReader {
    private static final Logger logger = LoggerFactory.getLogger(TestDataReader.class);
    private static JsonNode testData;

    static {
        loadTestData();
    }

    private static void loadTestData() {
        ObjectMapper mapper = new ObjectMapper();
        try (InputStream inputStream = TestDataReader.class.getClassLoader()
                .getResourceAsStream("testdata.json")) {
            if (inputStream != null) {
                testData = mapper.readTree(inputStream);
                logger.info("Test data loaded successfully");
            } else {
                logger.error("testdata.json file not found in classpath");
                throw new RuntimeException("testdata.json file not found");
            }
        } catch (IOException e) {
            logger.error("Error loading test data", e);
            throw new RuntimeException("Failed to load test data", e);
        }
    }

    public static JsonNode getTestData(String path) {
        String[] keys = path.split("\\.");
        JsonNode node = testData;

        for (String key : keys) {
            node = node.get(key);
            if (node == null) {
                logger.warn("Test data not found for path: {}", path);
                return null;
            }
        }
        return node;
    }

    public static String getTestDataAsString(String path) {
        JsonNode node = getTestData(path);
        return node != null ? node.asText() : null;
    }
}