package com.bookbyte.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object for user login requests.
 * 
 * <p>This DTO contains the credentials required for user authentication.
 * It includes validation rules for email and password fields.</p>
 * 
 * @author BookByte Team
 * @version 1.0.0
 * @since 2024-01-01
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginDTO {

    /**
     * User's email address for login.
     */
    @Email(message = "Please provide a valid email address")
    @NotBlank(message = "Email is required")
    private String email;

    /**
     * User's password for login.
     */
    @NotBlank(message = "Password is required")
    private String password;
}