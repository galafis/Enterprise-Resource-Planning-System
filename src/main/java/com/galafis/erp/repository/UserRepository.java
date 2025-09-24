package com.galafis.erp.repository;

import com.galafis.erp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for User entity
 * 
 * Provides data access layer for User operations including:
 * - Basic CRUD operations through JpaRepository
 * - Custom query methods for business requirements
 * - User authentication and authorization support
 * 
 * @author Gabriel Demetrios Lafis
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    /**
     * Find user by username (case-insensitive)
     * Used for authentication purposes
     */
    Optional<User> findByUsernameIgnoreCase(String username);
    
    /**
     * Find user by email address (case-insensitive)
     * Used for login and password recovery
     */
    Optional<User> findByEmailIgnoreCase(String email);
    
    /**
     * Check if username already exists (case-insensitive)
     * Used for user registration validation
     */
    boolean existsByUsernameIgnoreCase(String username);
    
    /**
     * Check if email already exists (case-insensitive)
     * Used for user registration validation
     */
    boolean existsByEmailIgnoreCase(String email);
    
    /**
     * Find all users with a specific role
     * Used for role-based management
     */
    List<User> findByRole(String role);
    
    /**
     * Find all enabled users
     * Used for active user management
     */
    List<User> findByEnabledTrue();
    
    /**
     * Find all disabled users
     * Used for user administration
     */
    List<User> findByEnabledFalse();
    
    /**
     * Find users created within a specific date range
     * Used for reporting and analytics
     */
    @Query("SELECT u FROM User u WHERE u.createdAt BETWEEN :startDate AND :endDate ORDER BY u.createdAt DESC")
    List<User> findUsersByCreationDateRange(@Param("startDate") LocalDateTime startDate, 
                                          @Param("endDate") LocalDateTime endDate);
    
    /**
     * Find users who have logged in recently
     * Used for activity monitoring
     */
    @Query("SELECT u FROM User u WHERE u.lastLogin >= :since ORDER BY u.lastLogin DESC")
    List<User> findRecentlyActiveUsers(@Param("since") LocalDateTime since);
    
    /**
     * Search users by name or username
     * Used for user search functionality
     */
    @Query("SELECT u FROM User u WHERE " +
           "LOWER(u.firstName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(u.lastName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(u.username) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<User> searchUsers(@Param("searchTerm") String searchTerm);
    
    /**
     * Count users by role
     * Used for dashboard statistics
     */
    @Query("SELECT COUNT(u) FROM User u WHERE u.role = :role")
    long countByRole(@Param("role") String role);
    
    /**
     * Update user's last login timestamp
     * Used during authentication process
     */
    @Query("UPDATE User u SET u.lastLogin = :loginTime WHERE u.id = :userId")
    void updateLastLogin(@Param("userId") Long userId, @Param("loginTime") LocalDateTime loginTime);
}
