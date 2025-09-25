package com.galafis.erp.repository;

import com.galafis.erp.entity.Customer;
import com.galafis.erp.entity.CustomerStatus;
import com.galafis.erp.entity.CustomerType;
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
 * Repository interface for Customer Relationship Management (CRM)
 * 
 * Provides comprehensive data access layer for CRM operations including:
 * - Customer management and lifecycle
 * - Lead tracking and conversion
 * - Sales opportunity management
 * - Customer segmentation and analytics
 * - Contact and communication history
 * - Customer service and support tracking
 * 
 * @author Gabriel Demetrios Lafis
 */
@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    
    /**
     * Find customer by customer code/number
     * Used for customer lookup and verification
     */
    Optional<Customer> findByCustomerCode(String customerCode);
    
    /**
     * Find customer by email address
     * Used for customer identification and communication
     */
    Optional<Customer> findByEmailIgnoreCase(String email);
    
    /**
     * Find customers by status
     * Used for customer lifecycle management
     */
    List<Customer> findByStatusOrderByCompanyNameAsc(CustomerStatus status);
    
    /**
     * Find customers by type
     * Used for customer segmentation and targeted marketing
     */
    List<Customer> findByCustomerTypeOrderByCompanyNameAsc(CustomerType customerType);
    
    /**
     * Find active customers
     * Used for current customer management
     */
    @Query("SELECT c FROM Customer c WHERE c.status = 'ACTIVE' ORDER BY c.companyName")
    List<Customer> findActiveCustomers();
    
    /**
     * Find customers by industry
     * Used for industry-based analysis and targeting
     */
    @Query("SELECT c FROM Customer c WHERE LOWER(c.industry) = LOWER(:industry) ORDER BY c.companyName")
    List<Customer> findByIndustry(@Param("industry") String industry);
    
    /**
     * Search customers by company name or contact name
     * Used for customer search functionality
     */
    @Query("SELECT c FROM Customer c WHERE " +
           "LOWER(c.companyName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(c.contactFirstName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(c.contactLastName) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<Customer> searchCustomers(@Param("searchTerm") String searchTerm);
    
    /**
     * Find customers by location/region
     * Used for geographical analysis and regional management
     */
    @Query("SELECT c FROM Customer c WHERE LOWER(c.city) = LOWER(:city) OR LOWER(c.state) = LOWER(:state)")
    List<Customer> findByLocation(@Param("city") String city, @Param("state") String state);
    
    /**
     * Find customers by registration date range
     * Used for onboarding tracking and campaign analysis
     */
    @Query("SELECT c FROM Customer c WHERE c.registrationDate BETWEEN :startDate AND :endDate ORDER BY c.registrationDate DESC")
    List<Customer> findCustomersByRegistrationDateRange(@Param("startDate") LocalDate startDate, 
                                                       @Param("endDate") LocalDate endDate);
    
    /**
     * Find customers with recent activity
     * Used for customer engagement tracking
     */
    @Query("SELECT c FROM Customer c WHERE c.lastContactDate >= :since ORDER BY c.lastContactDate DESC")
    List<Customer> findRecentlyActiveCustomers(@Param("since") LocalDateTime since);
    
    /**
     * Find inactive customers (no recent contact)
     * Used for customer retention campaigns
     */
    @Query("SELECT c FROM Customer c WHERE c.lastContactDate < :cutoffDate OR c.lastContactDate IS NULL")
    List<Customer> findInactiveCustomers(@Param("cutoffDate") LocalDateTime cutoffDate);
    
    /**
     * Find high-value customers by total spent
     * Used for VIP customer management and retention
     */
    @Query("SELECT c FROM Customer c WHERE c.totalSpent >= :threshold ORDER BY c.totalSpent DESC")
    List<Customer> findHighValueCustomers(@Param("threshold") BigDecimal threshold);
    
    /**
     * Find customers by credit limit range
     * Used for credit management and risk assessment
     */
    @Query("SELECT c FROM Customer c WHERE c.creditLimit BETWEEN :minLimit AND :maxLimit")
    List<Customer> findCustomersByCreditLimitRange(@Param("minLimit") BigDecimal minLimit, 
                                                  @Param("maxLimit") BigDecimal maxLimit);
    
    /**
     * Find customers with payment issues
     * Used for accounts receivable management
     */
    @Query("SELECT c FROM Customer c WHERE c.outstandingBalance > :threshold")
    List<Customer> findCustomersWithPaymentIssues(@Param("threshold") BigDecimal threshold);
    
    /**
     * Find customers by assigned sales rep
     * Used for sales territory and account management
     */
    @Query("SELECT c FROM Customer c WHERE c.assignedSalesRepId = :salesRepId ORDER BY c.companyName")
    List<Customer> findCustomersBySalesRep(@Param("salesRepId") Long salesRepId);
    
    /**
     * Count customers by status
     * Used for dashboard metrics and reporting
     */
    @Query("SELECT c.status, COUNT(c) FROM Customer c GROUP BY c.status")
    List<Object[]> countCustomersByStatus();
    
    /**
     * Count customers by type
     * Used for customer segmentation analysis
     */
    @Query("SELECT c.customerType, COUNT(c) FROM Customer c GROUP BY c.customerType")
    List<Object[]> countCustomersByType();
    
    /**
     * Calculate total revenue by customer type
     * Used for revenue analysis and business intelligence
     */
    @Query("SELECT c.customerType, SUM(c.totalSpent) FROM Customer c GROUP BY c.customerType ORDER BY SUM(c.totalSpent) DESC")
    List<Object[]> calculateRevenueByCustomerType();
    
    /**
     * Find customers with upcoming contract renewals
     * Used for contract management and renewal campaigns
     */
    @Query("SELECT c FROM Customer c WHERE c.contractEndDate <= :renewalThreshold AND c.contractEndDate IS NOT NULL")
    List<Customer> findCustomersWithUpcomingRenewals(@Param("renewalThreshold") LocalDate renewalThreshold);
    
    /**
     * Find new customers (recent registrations)
     * Used for onboarding and welcome campaigns
     */
    @Query("SELECT c FROM Customer c WHERE c.registrationDate >= :recentDate ORDER BY c.registrationDate DESC")
    List<Customer> findNewCustomers(@Param("recentDate") LocalDate recentDate);
    
    /**
     * Find customers with open support tickets
     * Used for customer service and support management
     */
    @Query("SELECT DISTINCT c FROM Customer c JOIN c.supportTickets st WHERE st.status = 'OPEN'")
    List<Customer> findCustomersWithOpenTickets();
    
    /**
     * Find top customers by total spending
     * Used for VIP program management and targeted marketing
     */
    @Query("SELECT c FROM Customer c WHERE c.status = 'ACTIVE' ORDER BY c.totalSpent DESC")
    Page<Customer> findTopCustomersBySpending(Pageable pageable);
    
    /**
     * Calculate customer lifetime value statistics
     * Used for business analytics and customer insights
     */
    @Query("SELECT AVG(c.totalSpent), MIN(c.totalSpent), MAX(c.totalSpent), COUNT(c) FROM Customer c WHERE c.status = 'ACTIVE'")
    List<Object[]> calculateCustomerLifetimeValueStats();
    
    /**
     * Find customers by acquisition source
     * Used for marketing channel analysis and ROI tracking
     */
    @Query("SELECT c FROM Customer c WHERE LOWER(c.acquisitionSource) = LOWER(:source) ORDER BY c.registrationDate DESC")
    List<Customer> findCustomersByAcquisitionSource(@Param("source") String source);
    
    /**
     * Find customers with birthdays in current month
     * Used for birthday campaigns and customer engagement
     */
    @Query("SELECT c FROM Customer c WHERE MONTH(c.dateOfBirth) = MONTH(CURRENT_DATE) AND c.dateOfBirth IS NOT NULL")
    List<Customer> findCustomersWithBirthdaysThisMonth();
    
    /**
     * Find potential churning customers
     * Used for customer retention and proactive engagement
     */
    @Query("SELECT c FROM Customer c WHERE " +
           "c.lastOrderDate < :churnThreshold AND c.status = 'ACTIVE' " +
           "ORDER BY c.lastOrderDate ASC")
    List<Customer> findPotentialChurningCustomers(@Param("churnThreshold") LocalDate churnThreshold);
    
    /**
     * Update customer status
     * Used for customer lifecycle management
     */
    @Modifying
    @Transactional
    @Query("UPDATE Customer c SET c.status = :status, c.statusChangeDate = CURRENT_DATE " +
           "WHERE c.id = :customerId")
    void updateCustomerStatus(@Param("customerId") Long customerId, @Param("status") CustomerStatus status);
    
    /**
     * Update customer last contact date
     * Used for interaction tracking
     */
    @Modifying
    @Transactional
    @Query("UPDATE Customer c SET c.lastContactDate = CURRENT_TIMESTAMP WHERE c.id = :customerId")
    void updateLastContactDate(@Param("customerId") Long customerId);
    
    /**
     * Update customer total spent
     * Used for customer value tracking
     */
    @Modifying
    @Transactional
    @Query("UPDATE Customer c SET c.totalSpent = c.totalSpent + :amount WHERE c.id = :customerId")
    void updateTotalSpent(@Param("customerId") Long customerId, @Param("amount") BigDecimal amount);
    
    /**
     * Update customer outstanding balance
     * Used for accounts receivable management
     */
    @Modifying
    @Transactional
    @Query("UPDATE Customer c SET c.outstandingBalance = :balance WHERE c.id = :customerId")
    void updateOutstandingBalance(@Param("customerId") Long customerId, @Param("balance") BigDecimal balance);
    
    /**
     * Find customers with pagination
     * Used for efficient data retrieval in large customer databases
     */
    @Query("SELECT c FROM Customer c ORDER BY c.companyName")
    Page<Customer> findAllCustomersWithPagination(Pageable pageable);
    
    /**
     * Check if customer code exists
     * Used for validation during customer creation
     */
    boolean existsByCustomerCode(String customerCode);
    
    /**
     * Check if email exists
     * Used for validation during customer registration
     */
    boolean existsByEmailIgnoreCase(String email);
    
    /**
     * Find customers for targeted marketing campaigns
     * Used for campaign management and customer targeting
     */
    @Query("SELECT c FROM Customer c WHERE " +
           "c.status = 'ACTIVE' AND " +
           "c.marketingOptIn = true AND " +
           "(:industry IS NULL OR LOWER(c.industry) = LOWER(:industry)) AND " +
           "(:customerType IS NULL OR c.customerType = :customerType) " +
           "ORDER BY c.totalSpent DESC")
    List<Customer> findCustomersForCampaign(@Param("industry") String industry,
                                           @Param("customerType") CustomerType customerType);
}
