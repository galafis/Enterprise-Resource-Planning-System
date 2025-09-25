package com.galafis.erp.service;

import com.galafis.erp.entity.User;
import com.galafis.erp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * UserService - Comprehensive user management service for the ERP system.
 * Implements business logic for user operations, authentication, and authorization.
 * Follows Spring Security best practices and enterprise patterns.
 * 
 * @author Gabriel Demetrios Lafis
 * @version 1.0
 * @since 2025-09-24
 */
@Service
@Transactional
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Spring Security UserDetailsService implementation
     * @param username the username identifying the user
     * @return UserDetails for authentication
     * @throws UsernameNotFoundException if user not found
     */
    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsernameIgnoreCase(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
        
        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .authorities(user.getRole().getName())
                .accountExpired(false)
                .accountLocked(!user.getActive())
                .credentialsExpired(false)
                .disabled(!user.getActive())
                .build();
    }

    /**
     * Create a new user with encrypted password
     * @param user the user to create
     * @return the created user
     */
    public User createUser(User user) {
        if (userRepository.findByUsernameIgnoreCase(user.getUsername()).isPresent()) {
            throw new IllegalArgumentException("Username already exists: " + user.getUsername());
        }
        
        if (userRepository.findByEmailIgnoreCase(user.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email already exists: " + user.getEmail());
        }

        // Encrypt password
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setCreatedAt(LocalDateTime.now());
        user.setLastLogin(null);
        user.setActive(true);
        
        return userRepository.save(user);
    }

    /**
     * Update user information
     * @param userId the user ID
     * @param updatedUser the updated user data
     * @return the updated user
     */
    public User updateUser(Long userId, User updatedUser) {
        User existingUser = getUserById(userId);
        
        // Update allowed fields
        existingUser.setFirstName(updatedUser.getFirstName());
        existingUser.setLastName(updatedUser.getLastName());
        existingUser.setEmail(updatedUser.getEmail());
        existingUser.setDepartment(updatedUser.getDepartment());
        existingUser.setPosition(updatedUser.getPosition());
        existingUser.setPhone(updatedUser.getPhone());
        
        return userRepository.save(existingUser);
    }

    /**
     * Change user password with validation
     * @param userId the user ID
     * @param oldPassword the current password
     * @param newPassword the new password
     */
    public void changePassword(Long userId, String oldPassword, String newPassword) {
        User user = getUserById(userId);
        
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new IllegalArgumentException("Current password is incorrect");
        }
        
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    /**
     * Get user by ID
     * @param userId the user ID
     * @return the user
     */
    @Transactional(readOnly = true)
    public User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userId));
    }

    /**
     * Get user by username
     * @param username the username
     * @return the user if found
     */
    @Transactional(readOnly = true)
    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByUsernameIgnoreCase(username);
    }

    /**
     * Get user by email
     * @param email the email
     * @return the user if found
     */
    @Transactional(readOnly = true)
    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmailIgnoreCase(email);
    }

    /**
     * Get all users
     * @return list of all users
     */
    @Transactional(readOnly = true)
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    /**
     * Get active users only
     * @return list of active users
     */
    @Transactional(readOnly = true)
    public List<User> getActiveUsers() {
        return userRepository.findByActiveTrue();
    }

    /**
     * Get users by department
     * @param department the department name
     * @return list of users in the department
     */
    @Transactional(readOnly = true)
    public List<User> getUsersByDepartment(String department) {
        return userRepository.findByDepartmentIgnoreCase(department);
    }

    /**
     * Search users by name or email
     * @param searchTerm the search term
     * @return list of matching users
     */
    @Transactional(readOnly = true)
    public List<User> searchUsers(String searchTerm) {
        return userRepository.findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCaseOrEmailContainingIgnoreCase(
                searchTerm, searchTerm, searchTerm);
    }

    /**
     * Activate or deactivate user account
     * @param userId the user ID
     * @param active the new active status
     */
    public void setUserActiveStatus(Long userId, boolean active) {
        User user = getUserById(userId);
        user.setActive(active);
        userRepository.save(user);
    }

    /**
     * Delete user by ID
     * @param userId the user ID
     */
    public void deleteUser(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new IllegalArgumentException("User not found with ID: " + userId);
        }
        userRepository.deleteById(userId);
    }

    /**
     * Update user's last login time
     * @param username the username
     */
    public void updateLastLogin(String username) {
        userRepository.findByUsernameIgnoreCase(username)
                .ifPresent(user -> {
                    user.setLastLogin(LocalDateTime.now());
                    userRepository.save(user);
                });
    }

    /**
     * Get user count by active status
     * @param active the active status
     * @return count of users
     */
    @Transactional(readOnly = true)
    public long getUserCount(boolean active) {
        return active ? userRepository.countByActiveTrue() : userRepository.countByActiveFalse();
    }

    /**
     * Get recently created users
     * @param days number of days to look back
     * @return list of recently created users
     */
    @Transactional(readOnly = true)
    public List<User> getRecentlyCreatedUsers(int days) {
        LocalDateTime since = LocalDateTime.now().minusDays(days);
        return userRepository.findByCreatedAtAfterOrderByCreatedAtDesc(since);
    }

    /**
     * Validate user credentials
     * @param username the username
     * @param password the raw password
     * @return true if credentials are valid
     */
    @Transactional(readOnly = true)
    public boolean validateCredentials(String username, String password) {
        return userRepository.findByUsernameIgnoreCase(username)
                .map(user -> passwordEncoder.matches(password, user.getPassword()))
                .orElse(false);
    }

    /**
     * Check if username is available
     * @param username the username to check
     * @return true if available
     */
    @Transactional(readOnly = true)
    public boolean isUsernameAvailable(String username) {
        return !userRepository.findByUsernameIgnoreCase(username).isPresent();
    }

    /**
     * Check if email is available
     * @param email the email to check
     * @return true if available
     */
    @Transactional(readOnly = true)
    public boolean isEmailAvailable(String email) {
        return !userRepository.findByEmailIgnoreCase(email).isPresent();
    }
}
