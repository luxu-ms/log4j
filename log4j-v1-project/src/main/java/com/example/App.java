package com.example;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class App {
    private static final Logger logger = LogManager.getLogger(App.class);

    public static void main(String[] args) {
        // Log4j 2 auto-configures from log4j2.xml

        logger.debug("Debug message");
        logger.info("Info message - Application started");
        logger.warn("Warning message");
        logger.error("Error message");
        logger.fatal("Fatal message");

        performSomeOperation();

        logger.info("Application finished");
    }

    private static void performSomeOperation() {
        logger.info("Performing some operation...");
        try {
            int result = divide(10, 2);
            logger.info("Division result: " + result);
        } catch (Exception e) {
            logger.error("Error during operation", e);
        }
    }

    private static int divide(int a, int b) {
        return a / b;
    }
}
