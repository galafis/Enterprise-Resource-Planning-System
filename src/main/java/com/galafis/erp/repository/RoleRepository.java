package com.galafis.erp.repository;

import com.galafis.erp.entity.Role;
import com.galafis.erp.entity.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Role entity
 * 
 * Provides comprehensive data access layer for Role operations including:
 * - Basic CRUD operations through JpaRepository
 * - Role name-based queries
 * - Role validation and management
 * - Role assignment analytics
 * 
 * @author Gabriel Demetrios Lafis
 */
@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    
    /**
     * Find role by role name
     * Used for role assignment and validation
     */
    Optional<Role> findByName(RoleName name);
    
    /**
     * Check if role exists by name
     * Used for role validation
     */
    boolean existsByName(RoleName name);
    
    /**
     * Find all roles ordered by name
     * Used for role listing and management
     */
    @Query("SELECT r FROM Role r ORDER BY r.name")
    List<Role> findAllOrderByName();
    
    /**
     * Find roles by description containing text (case-insensitive)
     * Used for role search functionality
     */
    @Query("SELECT r FROM Role r WHERE LOWER(r.description) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<Role> findByDescriptionContainingIgnoreCase(@Param("searchTerm") String searchTerm);
    
    /**
     * Count users assigned to each role
     * Used for role analytics and statistics
     */
    @Query("SELECT r.name, COUNT(u) FROM Role r LEFT JOIN r.users u GROUP BY r.name")
    List<Object[]> countUsersByRole();
    
    /**
     * Find roles that have no users assigned
     * Used for role cleanup and management
     */
    @Query("SELECT r FROM Role r WHERE r.users IS EMPTY")
    List<Role> findRolesWithoutUsers();
    
    /**
     * Find roles with user count greater than specified threshold
     * Used for role monitoring and capacity planning
     */
    @Query("SELECT r FROM Role r WHERE SIZE(r.users) > :threshold")
    List<Role> findRolesWithUserCountAbove(@Param("threshold") int threshold);
}
