package com.bookbyte.dto;

import com.bookbyte.entity.User;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Data Transfer Object for User information.
 * 
 * <p>This DTO is used for transferring user data between the frontend
 * and backend, excluding sensitive information like passwords.</p>
 * 
 * <p>Key features:
 * <ul>
 *   <li>User registration and update</li>
 *   <li>Input validation</li>
 *   <li>Password confirmation</li>
 *   <li>Role management</li>
 * </ul></p>
 * 
 * @author BookByte Team
 * @version 1.0.0
 * @since 2024-01-01
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

    /**
     * User's unique identifier.
     */
    private Long id;

    /**
     * User's full name.
     */
    @NotBlank(message = "Name is required")
    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    private String name;

    /**
     * User's email address.
     */
    @Email(message = "Please provide a valid email address")
    @NotBlank(message = "Email is required")
    private String email;

    /**
     * User's password.
     */
    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters long")
    private String password;

    /**
     * Password confirmation for validation.
     */
    @NotBlank(message = "Password confirmation is required")
    private String confirmPassword;

    /**
     * User's age.
     */
    @Min(value = 18, message = "User must be at least 18 years old")
    private Integer age;

    /**
     * User's address.
     */
    @NotBlank(message = "Address is required")
    private String address;

    /**
     * User's role in the system.
     */
    private User.UserRole role = User.UserRole.CUSTOMER;

    /**
     * User's customer ID.
     */
    private String customerId;

    /**
     * Timestamp when the user account was created.
     */
    private LocalDateTime createdAt;

    /**
     * Flag indicating if the user account is active.
     */
    private Boolean isActive = true;

    /**
     * Validates that password and confirmPassword match.
     * 
     * @return true if passwords match, false otherwise
     */
    public boolean isPasswordMatching() {
        return password != null && password.equals(confirmPassword);
    }
}