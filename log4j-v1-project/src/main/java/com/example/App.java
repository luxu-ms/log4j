package com.example;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public class App {
    private static final Logger logger = Logger.getLogger(App.class);

    public static void main(String[] args) {
        // Configure Log4j
        PropertyConfigurator.configure("log4j.properties");

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
