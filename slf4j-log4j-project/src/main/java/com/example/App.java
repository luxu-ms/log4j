package com.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class App {
    private static final Logger logger = LoggerFactory.getLogger(App.class);

    public static void main(String[] args) {
        logger.debug("Debug message");
        logger.info("Info message - Application started");
        logger.warn("Warning message");
        logger.error("Error message");

        performSomeOperation();

        logger.info("Application finished");
    }

    private static void performSomeOperation() {
        logger.info("Performing some operation...");
        try {
            int result = divide(10, 2);
            logger.info("Division result: {}", result);
            
            // Test error logging with exception
            result = divide(10, 0);
        } catch (Exception e) {
            logger.error("Error during operation", e);
        }
    }

    private static int divide(int a, int b) {
        return a / b;
    }
}
