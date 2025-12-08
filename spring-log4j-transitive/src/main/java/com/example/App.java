package com.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import com.example.config.AppConfig;
import com.example.service.UserService;

/**
 * Spring application that uses SLF4J for logging.
 * Log4j 2.x is used as the backend logging implementation
 * through log4j-slf4j-impl binding. This represents the recommended approach
 * for Spring applications using modern logging infrastructure.
 */
public class App {
    private static final Logger logger = LoggerFactory.getLogger(App.class);
    
    public static void main(String[] args) {
        logger.info("Starting Spring application with Log4j 2.x backend");
        
        // Initialize Spring context
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        
        logger.info("Spring context initialized successfully");
        
        // Get UserService bean
        UserService userService = context.getBean(UserService.class);
        
        // Demonstrate application functionality
        try {
            logger.info("Demonstrating user service operations...");
            
            userService.createUser("john.doe", "john.doe@example.com");
            userService.createUser("jane.smith", "jane.smith@example.com");
            
            String user = userService.getUserById(123L);
            logger.info("Retrieved user: {}", user);
            
            userService.deleteUser(123L);
            
            logger.info("Application completed successfully");
        } catch (Exception e) {
            logger.error("Error during application execution", e);
        }
        
        logger.info("Application finished");
    }
}
