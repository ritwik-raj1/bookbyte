package com.bookbyte.repository;

import com.bookbyte.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for User entity operations.
 * 
 * <p>This repository provides methods for database operations related to users,
 * including authentication, role-based queries, and customer management.</p>
 * 
 * <p>Key features:
 * <ul>
 *   <li>User authentication queries</li>
 *   <li>Role-based user queries</li>
 *   <li>Customer statistics</li>
 *   <li>Email uniqueness validation</li>
 * </ul></p>
 * 
 * @author BookByte Team
 * @version 1.0.0
 * @since 2024-01-01
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Finds a user by their email address.
     * 
     * @param email the email address to search for
     * @return Optional containing the user if found
     */
    Optional<User> findByEmail(String email);

    /**
     * Finds a user by their customer ID.
     * 
     * @param customerId the customer ID to search for
     * @return Optional containing the user if found
     */
    Optional<User> findByCustomerId(String customerId);

    /**
     * Checks if a user exists with the given email address.
     * 
     * @param email the email address to check
     * @return true if a user exists with this email, false otherwise
     */
    boolean existsByEmail(String email);

    /**
     * Finds all users with a specific role.
     * 
     * @param role the role to filter by
     * @return List of users with the specified role
     */
    List<User> findByRole(User.UserRole role);

    /**
     * Finds all active users.
     * 
     * @return List of active users
     */
    List<User> findByIsActiveTrue();

    /**
     * Finds all customers (users with CUSTOMER role).
     * 
     * @return List of customers
     */
    @Query("SELECT u FROM User u WHERE u.role = 'CUSTOMER' AND u.isActive = true")
    List<User> findAllCustomers();

    /**
     * Counts the number of orders for a specific user.
     * 
     * @param userId the user's ID
     * @return the number of orders
     */
    @Query("SELECT COUNT(o) FROM User u JOIN u.orders o WHERE u.id = :userId")
    Long countOrdersByUserId(@Param("userId") Long userId);

    /**
     * Finds users by name containing the given string (case-insensitive).
     * 
     * @param name the name to search for
     * @return List of users whose names contain the search string
     */
    List<User> findByNameContainingIgnoreCase(String name);
}