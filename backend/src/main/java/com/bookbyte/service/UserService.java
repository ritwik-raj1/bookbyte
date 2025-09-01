package com.bookbyte.service;

import com.bookbyte.dto.UserDTO;
import com.bookbyte.entity.User;
import com.bookbyte.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service class for managing user operations.
 * 
 * <p>This service handles user registration, authentication, and management
 * operations including password encryption and role management.</p>
 * 
 * <p>Key features:
 * <ul>
 *   <li>User registration and validation</li>
 *   <li>Password encryption</li>
 *   <li>User authentication</li>
 *   <li>Role-based user management</li>
 *   <li>Customer statistics</li>
 * </ul></p>
 * 
 * @author BookByte Team
 * @version 1.0.0
 * @since 2024-01-01
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Registers a new user in the system.
     * 
     * @param userDTO the user data transfer object
     * @return the created user
     * @throws IllegalArgumentException if validation fails
     */
    public User registerUser(UserDTO userDTO) {
        // Validate password confirmation
        if (!userDTO.isPasswordMatching()) {
            throw new IllegalArgumentException("Password and confirmation password do not match");
        }

        // Check if email already exists
        if (userRepository.existsByEmail(userDTO.getEmail())) {
            throw new IllegalArgumentException("Email already registered");
        }

        // Create new user
        User user = new User();
        user.setName(userDTO.getName());
        user.setEmail(userDTO.getEmail());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        user.setAge(userDTO.getAge());
        user.setAddress(userDTO.getAddress());
        user.setRole(userDTO.getRole());

        User savedUser = userRepository.save(user);
        log.info("User registered successfully: {}", savedUser.getEmail());
        return savedUser;
    }

    /**
     * Finds a user by their email address.
     * 
     * @param email the email address to search for
     * @return the user if found
     * @throws UsernameNotFoundException if user not found
     */
    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
    }

    /**
     * Finds a user by their ID.
     * 
     * @param id the user ID to search for
     * @return the user if found
     * @throws UsernameNotFoundException if user not found
     */
    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with ID: " + id));
    }

    /**
     * Finds a user by their customer ID.
     * 
     * @param customerId the customer ID to search for
     * @return the user if found
     * @throws UsernameNotFoundException if user not found
     */
    public User findByCustomerId(String customerId) {
        return userRepository.findByCustomerId(customerId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with customer ID: " + customerId));
    }

    /**
     * Retrieves all customers in the system.
     * 
     * @return list of all customers
     */
    public List<User> getAllCustomers() {
        return userRepository.findAllCustomers();
    }

    /**
     * Updates user information.
     * 
     * @param id the user ID
     * @param userDTO the updated user data
     * @return the updated user
     */
    public User updateUser(Long id, UserDTO userDTO) {
        User user = findById(id);
        
        if (userDTO.getName() != null) {
            user.setName(userDTO.getName());
        }
        if (userDTO.getAge() != null) {
            user.setAge(userDTO.getAge());
        }
        if (userDTO.getAddress() != null) {
            user.setAddress(userDTO.getAddress());
        }
        if (userDTO.getPassword() != null && userDTO.isPasswordMatching()) {
            user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        }

        return userRepository.save(user);
    }

    /**
     * Deactivates a user account.
     * 
     * @param id the user ID
     * @return the deactivated user
     */
    public User deactivateUser(Long id) {
        User user = findById(id);
        user.setIsActive(false);
        return userRepository.save(user);
    }

    /**
     * Loads user details for Spring Security authentication.
     * 
     * @param email the user's email address
     * @return UserDetails object for authentication
     * @throws UsernameNotFoundException if user not found
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = findByEmail(email);
        
        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getEmail())
                .password(user.getPassword())
                .authorities(List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().name())))
                .disabled(!user.getIsActive())
                .accountExpired(false)
                .credentialsExpired(false)
                .accountLocked(false)
                .build();
    }

    /**
     * Gets customer statistics for admin dashboard.
     * 
     * @return list of customers with order counts
     */
    public List<UserDTO> getCustomerStatistics() {
        return userRepository.findAllCustomers().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Converts User entity to UserDTO.
     * 
     * @param user the user entity
     * @return the user DTO
     */
    private UserDTO convertToDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setAge(user.getAge());
        dto.setAddress(user.getAddress());
        dto.setRole(user.getRole());
        dto.setCustomerId(user.getCustomerId());
        dto.setCreatedAt(user.getCreatedAt());
        dto.setIsActive(user.getIsActive());
        return dto;
    }
}