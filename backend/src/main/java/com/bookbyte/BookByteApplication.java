package com.bookbyte;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main Spring Boot application class for BookByte Online Bookstore.
 * 
 * <p>This class serves as the entry point for the Spring Boot application.
 * It automatically configures the application context, embedded web server,
 * and database connection.</p>
 * 
 * <p>The application provides RESTful APIs for:
 * <ul>
 *   <li>User authentication and authorization</li>
 *   <li>Book management (CRUD operations)</li>
 *   <li>Shopping cart functionality</li>
 *   <li>Order processing and management</li>
 *   <li>Customer management</li>
 * </ul></p>
 * 
 * @author BookByte Team
 * @version 1.0.0
 * @since 2024-01-01
 */
@SpringBootApplication
public class BookByteApplication {

    /**
     * Main method to start the Spring Boot application.
     * 
     * @param args command line arguments passed to the application
     */
    public static void main(String[] args) {
        SpringApplication.run(BookByteApplication.class, args);
    }
}