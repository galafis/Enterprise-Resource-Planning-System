package com.galafis.erp.repository;

import com.galafis.erp.entity.User;
import com.galafis.erp.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for User entity
 * 
 * Provides comprehensive data access layer for User operations including:
 * - Basic CRUD operations through JpaRepository
 * - Custom query methods for business requirements
 * - User authentication and authorization support
 * - Advanced search and filtering capabilities
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
    @Query("SELECT u FROM User u JOIN u.roles r WHERE r.name = :roleName")
    List<User> findByRoleName(@Param("roleName") String roleName);
    
    /**
     * Find all active users
     * Used for active user management
     */
    List<User> findByIsActiveTrue();
    
    /**
     * Find all inactive users
     * Used for user administration
     */
    List<User> findByIsActiveFalse();
    
    /**
     * Find users created within a specific date range
     * Used for reporting and analytics
     */
    @Query("SELECT u FROM User u WHERE u.createdAt BETWEEN :startDate AND :endDate ORDER BY u.createdAt DESC")
    List<User> findUsersByCreationDateRange(@Param("startDate") LocalDateTime startDate, 
                                          @Param("endDate") LocalDateTime endDate);
    
    /**
     * Find users who have been updated recently
     * Used for activity monitoring
     */
    @Query("SELECT u FROM User u WHERE u.updatedAt >= :since ORDER BY u.updatedAt DESC")
    List<User> findRecentlyUpdatedUsers(@Param("since") LocalDateTime since);
    
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
    @Query("SELECT COUNT(u) FROM User u JOIN u.roles r WHERE r.name = :roleName")
    long countByRoleName(@Param("roleName") String roleName);
    
    /**
     * Count active users
     * Used for dashboard statistics
     */
    long countByIsActiveTrue();
    
    /**
     * Count inactive users
     * Used for dashboard statistics
     */
    long countByIsActiveFalse();
    
    /**
     * Find users without any roles
     * Used for user management and validation
     */
    @Query("SELECT u FROM User u WHERE u.roles IS EMPTY")
    List<User> findUsersWithoutRoles();
    
    /**
     * Find users with multiple roles
     * Used for privilege analysis
     */
    @Query("SELECT u FROM User u WHERE SIZE(u.roles) > 1")
    List<User> findUsersWithMultipleRoles();
    
    /**
     * Deactivate user by ID
     * Used for user management
     */
    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.isActive = false, u.updatedAt = CURRENT_TIMESTAMP WHERE u.id = :userId")
    void deactivateUser(@Param("userId") Long userId);
    
    /**
     * Activate user by ID
     * Used for user management
     */
    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.isActive = true, u.updatedAt = CURRENT_TIMESTAMP WHERE u.id = :userId")
    void activateUser(@Param("userId") Long userId);
}
