package com.galafis.erp.repository;

import com.galafis.erp.entity.Employee;
import com.galafis.erp.entity.Department;
import com.galafis.erp.entity.Position;
import com.galafis.erp.entity.EmployeeStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Human Resources management
 * 
 * Provides comprehensive data access layer for HR operations including:
 * - Employee management and lifecycle
 * - Department and position management
 * - Payroll and compensation tracking
 * - Performance evaluation support
 * - Leave management and attendance
 * - Training and development records
 * 
 * @author Gabriel Demetrios Lafis
 */
@Repository
public interface HumanResourcesRepository extends JpaRepository<Employee, Long> {
    
    /**
     * Find employee by employee ID/number
     * Used for employee lookup and verification
     */
    Optional<Employee> findByEmployeeNumber(String employeeNumber);
    
    /**
     * Find employee by email address
     * Used for authentication and communication
     */
    Optional<Employee> findByEmailIgnoreCase(String email);
    
    /**
     * Find all employees by department
     * Used for department-based HR management
     */
    List<Employee> findByDepartmentOrderByLastNameAsc(Department department);
    
    /**
     * Find all employees by position
     * Used for position-based analysis and management
     */
    List<Employee> findByPositionOrderByHireDateAsc(Position position);
    
    /**
     * Find employees by status
     * Used for filtering active, inactive, or terminated employees
     */
    List<Employee> findByStatusOrderByLastNameAsc(EmployeeStatus status);
    
    /**
     * Find active employees
     * Used for current workforce management
     */
    @Query("SELECT e FROM Employee e WHERE e.status = 'ACTIVE' ORDER BY e.lastName, e.firstName")
    List<Employee> findActiveEmployees();
    
    /**
     * Find employees by hire date range
     * Used for onboarding tracking and anniversary reports
     */
    @Query("SELECT e FROM Employee e WHERE e.hireDate BETWEEN :startDate AND :endDate ORDER BY e.hireDate DESC")
    List<Employee> findEmployeesByHireDateRange(@Param("startDate") LocalDate startDate, 
                                               @Param("endDate") LocalDate endDate);
    
    /**
     * Find employees with upcoming anniversaries
     * Used for recognition and anniversary planning
     */
    @Query("SELECT e FROM Employee e WHERE MONTH(e.hireDate) = MONTH(:targetDate) AND DAY(e.hireDate) = DAY(:targetDate)")
    List<Employee> findEmployeesWithAnniversary(@Param("targetDate") LocalDate targetDate);
    
    /**
     * Search employees by name or employee number
     * Used for employee search functionality
     */
    @Query("SELECT e FROM Employee e WHERE " +
           "LOWER(e.firstName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(e.lastName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(e.employeeNumber) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<Employee> searchEmployees(@Param("searchTerm") String searchTerm);
    
    /**
     * Find employees by manager
     * Used for organizational hierarchy management
     */
    @Query("SELECT e FROM Employee e WHERE e.managerId = :managerId ORDER BY e.lastName, e.firstName")
    List<Employee> findEmployeesByManager(@Param("managerId") Long managerId);
    
    /**
     * Find employees with salary in range
     * Used for compensation analysis and budgeting
     */
    @Query("SELECT e FROM Employee e WHERE e.salary BETWEEN :minSalary AND :maxSalary ORDER BY e.salary DESC")
    List<Employee> findEmployeesBySalaryRange(@Param("minSalary") BigDecimal minSalary, 
                                             @Param("maxSalary") BigDecimal maxSalary);
    
    /**
     * Calculate average salary by department
     * Used for compensation benchmarking and budgeting
     */
    @Query("SELECT e.department, AVG(e.salary) FROM Employee e WHERE e.status = 'ACTIVE' GROUP BY e.department")
    List<Object[]> calculateAverageSalaryByDepartment();
    
    /**
     * Count employees by department
     * Used for organizational metrics and planning
     */
    @Query("SELECT e.department, COUNT(e) FROM Employee e WHERE e.status = 'ACTIVE' GROUP BY e.department")
    List<Object[]> countEmployeesByDepartment();
    
    /**
     * Find employees eligible for performance review
     * Used for performance management cycles
     */
    @Query("SELECT e FROM Employee e WHERE e.lastReviewDate IS NULL OR e.lastReviewDate < :cutoffDate")
    List<Employee> findEmployeesDueForReview(@Param("cutoffDate") LocalDate cutoffDate);
    
    /**
     * Find employees with pending leave requests
     * Used for leave management and approval workflows
     */
    @Query("SELECT e FROM Employee e JOIN e.leaveRequests lr WHERE lr.status = 'PENDING'")
    List<Employee> findEmployeesWithPendingLeave();
    
    /**
     * Find employees on leave during date range
     * Used for workforce planning and coverage
     */
    @Query("SELECT e FROM Employee e JOIN e.leaveRequests lr WHERE " +
           "lr.status = 'APPROVED' AND lr.startDate <= :endDate AND lr.endDate >= :startDate")
    List<Employee> findEmployeesOnLeave(@Param("startDate") LocalDate startDate, 
                                       @Param("endDate") LocalDate endDate);
    
    /**
     * Find employees with certifications expiring soon
     * Used for training and compliance management
     */
    @Query("SELECT e FROM Employee e JOIN e.certifications c WHERE c.expiryDate <= :expiryThreshold")
    List<Employee> findEmployeesWithExpiringCertifications(@Param("expiryThreshold") LocalDate expiryThreshold);
    
    /**
     * Find employees by skill or competency
     * Used for project assignment and skill gap analysis
     */
    @Query("SELECT e FROM Employee e JOIN e.skills s WHERE LOWER(s.skillName) = LOWER(:skillName)")
    List<Employee> findEmployeesBySkill(@Param("skillName") String skillName);
    
    /**
     * Find employees with training due
     * Used for training schedule management
     */
    @Query("SELECT e FROM Employee e WHERE e.lastTrainingDate IS NULL OR e.lastTrainingDate < :cutoffDate")
    List<Employee> findEmployeesDueForTraining(@Param("cutoffDate") LocalDate cutoffDate);
    
    /**
     * Calculate total payroll cost by department
     * Used for budgeting and financial planning
     */
    @Query("SELECT e.department, SUM(e.salary) FROM Employee e WHERE e.status = 'ACTIVE' GROUP BY e.department")
    List<Object[]> calculateTotalPayrollByDepartment();
    
    /**
     * Find probationary employees
     * Used for onboarding and probation management
     */
    @Query("SELECT e FROM Employee e WHERE e.probationEndDate >= CURRENT_DATE AND e.status = 'PROBATION'")
    List<Employee> findProbationaryEmployees();
    
    /**
     * Find employees with contract expiring soon
     * Used for contract renewal management
     */
    @Query("SELECT e FROM Employee e WHERE e.contractEndDate <= :expiryThreshold AND e.contractEndDate IS NOT NULL")
    List<Employee> findEmployeesWithExpiringContracts(@Param("expiryThreshold") LocalDate expiryThreshold);
    
    /**
     * Find employees by age range
     * Used for demographic analysis and planning
     */
    @Query("SELECT e FROM Employee e WHERE YEAR(CURRENT_DATE) - YEAR(e.birthDate) BETWEEN :minAge AND :maxAge")
    List<Employee> findEmployeesByAgeRange(@Param("minAge") int minAge, @Param("maxAge") int maxAge);
    
    /**
     * Find employees approaching retirement
     * Used for succession planning
     */
    @Query("SELECT e FROM Employee e WHERE YEAR(CURRENT_DATE) - YEAR(e.birthDate) >= :retirementAge")
    List<Employee> findEmployeesNearingRetirement(@Param("retirementAge") int retirementAge);
    
    /**
     * Calculate employee tenure statistics
     * Used for retention analysis and reporting
     */
    @Query("SELECT AVG(DATEDIFF(CURRENT_DATE, e.hireDate)), MIN(DATEDIFF(CURRENT_DATE, e.hireDate)), " +
           "MAX(DATEDIFF(CURRENT_DATE, e.hireDate)) FROM Employee e WHERE e.status = 'ACTIVE'")
    List<Object[]> calculateTenureStatistics();
    
    /**
     * Update employee status
     * Used for employee lifecycle management
     */
    @Modifying
    @Transactional
    @Query("UPDATE Employee e SET e.status = :status, e.statusChangeDate = CURRENT_DATE " +
           "WHERE e.id = :employeeId")
    void updateEmployeeStatus(@Param("employeeId") Long employeeId, @Param("status") EmployeeStatus status);
    
    /**
     * Update employee salary
     * Used for compensation management
     */
    @Modifying
    @Transactional
    @Query("UPDATE Employee e SET e.salary = :newSalary, e.lastSalaryChangeDate = CURRENT_DATE " +
           "WHERE e.id = :employeeId")
    void updateEmployeeSalary(@Param("employeeId") Long employeeId, @Param("newSalary") BigDecimal newSalary);
    
    /**
     * Update employee department
     * Used for organizational changes and transfers
     */
    @Modifying
    @Transactional
    @Query("UPDATE Employee e SET e.department = :department, e.departmentChangeDate = CURRENT_DATE " +
           "WHERE e.id = :employeeId")
    void updateEmployeeDepartment(@Param("employeeId") Long employeeId, @Param("department") Department department);
    
    /**
     * Record performance review
     * Used for performance management
     */
    @Modifying
    @Transactional
    @Query("UPDATE Employee e SET e.lastReviewDate = CURRENT_DATE, e.performanceRating = :rating " +
           "WHERE e.id = :employeeId")
    void recordPerformanceReview(@Param("employeeId") Long employeeId, @Param("rating") String rating);
    
    /**
     * Find employees with pagination
     * Used for efficient data retrieval in large organizations
     */
    @Query("SELECT e FROM Employee e WHERE e.status = 'ACTIVE' ORDER BY e.lastName, e.firstName")
    Page<Employee> findActiveEmployeesWithPagination(Pageable pageable);
    
    /**
     * Check if employee number exists
     * Used for validation during employee creation
     */
    boolean existsByEmployeeNumber(String employeeNumber);
    
    /**
     * Check if email exists
     * Used for validation during employee creation
     */
    boolean existsByEmailIgnoreCase(String email);
    
    /**
     * Find employees by custom criteria for reporting
     * Used for flexible HR reporting and analytics
     */
    @Query("SELECT e FROM Employee e WHERE " +
           "(:department IS NULL OR e.department = :department) AND " +
           "(:position IS NULL OR e.position = :position) AND " +
           "(:status IS NULL OR e.status = :status) " +
           "ORDER BY e.lastName, e.firstName")
    List<Employee> findEmployeesByCriteria(@Param("department") Department department,
                                          @Param("position") Position position,
                                          @Param("status") EmployeeStatus status);
}
