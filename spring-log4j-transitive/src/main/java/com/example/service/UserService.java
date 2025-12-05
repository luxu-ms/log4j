package com.example.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * User service that uses SLF4J for logging.
 * The application doesn't directly use Log4j 1.x, but it comes as a transitive dependency
 * through slf4j-log4j12 binding.
 */
@Service
public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    
    public void createUser(String username, String email) {
        logger.info("Creating user: {}", username);
        
        if (username == null || username.isEmpty()) {
            logger.error("Username cannot be null or empty");
            throw new IllegalArgumentException("Username is required");
        }
        
        if (email == null || !email.contains("@")) {
            logger.error("Invalid email format: {}", email);
            throw new IllegalArgumentException("Valid email is required");
        }
        
        logger.debug("Validating user data for: {}", username);
        // Simulate user creation
        logger.info("User created successfully: {} with email {}", username, email);
    }
    
    public String getUserById(Long userId) {
        logger.info("Fetching user with ID: {}", userId);
        
        if (userId == null || userId <= 0) {
            logger.warn("Invalid user ID provided: {}", userId);
            return null;
        }
        
        logger.debug("User found with ID: {}", userId);
        return "User-" + userId;
    }
    
    public void deleteUser(Long userId) {
        logger.info("Deleting user with ID: {}", userId);
        
        try {
            // Simulate deletion
            if (userId == null) {
                throw new IllegalArgumentException("User ID cannot be null");
            }
            logger.info("User deleted successfully: {}", userId);
        } catch (Exception e) {
            logger.error("Error deleting user: {}", userId, e);
            throw e;
        }
    }
}
