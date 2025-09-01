package com.bookbyte.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * User entity representing a registered user in the BookByte system.
 * 
 * <p>This entity stores user authentication and profile information.
 * Users can be either customers or administrators based on their role.</p>
 * 
 * <p>Key features:
 * <ul>
 *   <li>Unique customer ID generation</li>
 *   <li>Email-based authentication</li>
 *   <li>Age verification (18+)</li>
 *   <li>Role-based access control</li>
 *   <li>Audit trail with timestamps</li>
 * </ul></p>
 * 
 * @author BookByte Team
 * @version 1.0.0
 * @since 2024-01-01
 */
@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    /**
     * Unique identifier for the user.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Unique customer ID generated for the user.
     */
    @Column(name = "customer_id", unique = true, nullable = false)
    private String customerId;

    /**
     * User's full name.
     */
    @NotBlank(message = "Name is required")
    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    @Column(name = "name", nullable = false)
    private String name;

    /**
     * User's email address used for login.
     */
    @Email(message = "Please provide a valid email address")
    @NotBlank(message = "Email is required")
    @Column(name = "email", unique = true, nullable = false)
    private String email;

    /**
     * User's encrypted password.
     */
    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters long")
    @Column(name = "password", nullable = false)
    private String password;

    /**
     * User's age for verification purposes.
     */
    @Min(value = 18, message = "User must be at least 18 years old")
    @Column(name = "age", nullable = false)
    private Integer age;

    /**
     * User's address information.
     */
    @NotBlank(message = "Address is required")
    @Column(name = "address", nullable = false, columnDefinition = "TEXT")
    private String address;

    /**
     * User's role in the system (CUSTOMER or ADMIN).
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private UserRole role = UserRole.CUSTOMER;

    /**
     * Timestamp when the user account was created.
     */
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /**
     * Timestamp when the user account was last updated.
     */
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    /**
     * Flag indicating if the user account is active.
     */
    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    /**
     * List of orders placed by this user.
     */
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Order> orders = new ArrayList<>();

    /**
     * User's shopping cart.
     */
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Cart cart;

    /**
     * Enumeration of user roles in the system.
     */
    public enum UserRole {
        CUSTOMER, ADMIN
    }

    /**
     * Pre-persist method to generate customer ID if not set.
     */
    @PrePersist
    protected void onCreate() {
        if (customerId == null) {
            this.customerId = "CUST" + System.currentTimeMillis();
        }
    }
}