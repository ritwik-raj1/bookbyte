package com.bookbyte.controller;

import com.bookbyte.dto.JwtResponseDTO;
import com.bookbyte.dto.LoginDTO;
import com.bookbyte.dto.UserDTO;
import com.bookbyte.entity.User;
import com.bookbyte.security.JwtTokenProvider;
import com.bookbyte.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Controller for handling authentication and user registration.
 * 
 * <p>This controller provides endpoints for user registration, login,
 * and authentication-related operations.</p>
 * 
 * <p>Key features:
 * <ul>
 *   <li>User registration</li>
 *   <li>User authentication</li>
 *   <li>JWT token generation</li>
 *   <li>Input validation</li>
 * </ul></p>
 * 
 * @author BookByte Team
 * @version 1.0.0
 * @since 2024-01-01
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;
    private final UserService userService;

    /**
     * Registers a new user in the system.
     * 
     * @param userDTO the user registration data
     * @return response with user information
     */
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody UserDTO userDTO) {
        try {
            User user = userService.registerUser(userDTO);
            
            Map<String, Object> response = new HashMap<>();
            response.put("message", "User registered successfully");
            response.put("user", convertToDTO(user));
            
            log.info("User registered successfully: {}", user.getEmail());
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    /**
     * Authenticates a user and returns JWT token.
     * 
     * @param loginDTO the login credentials
     * @return JWT response with token and user information
     */
    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginDTO loginDTO) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDTO.getEmail(), loginDTO.getPassword())
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = tokenProvider.generateToken(authentication);
            
            User user = userService.findByEmail(loginDTO.getEmail());
            JwtResponseDTO jwtResponse = new JwtResponseDTO(jwt, user);
            
            log.info("User logged in successfully: {}", user.getEmail());
            return ResponseEntity.ok(jwtResponse);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Invalid email or password");
            log.warn("Login failed for email: {}", loginDTO.getEmail());
            return ResponseEntity.badRequest().body(error);
        }
    }

    /**
     * Retrieves current user information.
     * 
     * @return current user information
     */
    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String email = authentication.getName();
            User user = userService.findByEmail(email);
            
            return ResponseEntity.ok(convertToDTO(user));
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "User not authenticated");
            return ResponseEntity.badRequest().body(error);
        }
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