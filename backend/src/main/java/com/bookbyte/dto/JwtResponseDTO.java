package com.bookbyte.dto;

import com.bookbyte.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object for JWT authentication response.
 * 
 * <p>This DTO contains the JWT token and user information
 * returned after successful authentication.</p>
 * 
 * @author BookByte Team
 * @version 1.0.0
 * @since 2024-01-01
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class JwtResponseDTO {

    /**
     * JWT token for authentication.
     */
    private String token;

    /**
     * Type of the token (Bearer).
     */
    private String type = "Bearer";

    /**
     * User's unique identifier.
     */
    private Long id;

    /**
     * User's email address.
     */
    private String email;

    /**
     * User's full name.
     */
    private String name;

    /**
     * User's role in the system.
     */
    private User.UserRole role;

    /**
     * User's customer ID.
     */
    private String customerId;

    /**
     * Constructor with token and user information.
     * 
     * @param token the JWT token
     * @param user the authenticated user
     */
    public JwtResponseDTO(String token, User user) {
        this.token = token;
        this.id = user.getId();
        this.email = user.getEmail();
        this.name = user.getName();
        this.role = user.getRole();
        this.customerId = user.getCustomerId();
    }
}