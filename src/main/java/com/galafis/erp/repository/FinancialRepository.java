package com.galafis.erp.repository;

import com.galafis.erp.entity.Financial;
import com.galafis.erp.entity.TransactionType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Financial entity
 * 
 * Provides comprehensive data access layer for Financial operations including:
 * - Basic CRUD operations through JpaRepository
 * - Transaction management and tracking
 * - Financial reporting and analytics
 * - Account balance calculations
 * - Budget and expense monitoring
 * - Revenue and profit analysis
 * 
 * @author Gabriel Demetrios Lafis
 */
@Repository
public interface FinancialRepository extends JpaRepository<Financial, Long> {
    
    /**
     * Find financial transaction by transaction ID
     * Used for transaction lookup and verification
     */
    Optional<Financial> findByTransactionId(String transactionId);
    
    /**
     * Find all transactions by type
     * Used for categorizing financial activities
     */
    List<Financial> findByTransactionTypeOrderByTransactionDateDesc(TransactionType transactionType);
    
    /**
     * Find transactions by account
     * Used for account-specific financial tracking
     */
    List<Financial> findByAccountIdOrderByTransactionDateDesc(Long accountId);
    
    /**
     * Find transactions within date range
     * Used for period-based financial reporting
     */
    @Query("SELECT f FROM Financial f WHERE f.transactionDate BETWEEN :startDate AND :endDate ORDER BY f.transactionDate DESC")
    List<Financial> findTransactionsByDateRange(@Param("startDate") LocalDateTime startDate, 
                                               @Param("endDate") LocalDateTime endDate);
    
    /**
     * Calculate total balance for an account
     * Used for account balance verification
     */
    @Query("SELECT COALESCE(SUM(CASE WHEN f.transactionType = 'CREDIT' THEN f.amount ELSE -f.amount END), 0) " +
           "FROM Financial f WHERE f.accountId = :accountId")
    BigDecimal calculateAccountBalance(@Param("accountId") Long accountId);
    
    /**
     * Calculate total revenue within date range
     * Used for revenue analysis and reporting
     */
    @Query("SELECT COALESCE(SUM(f.amount), 0) FROM Financial f " +
           "WHERE f.transactionType = 'CREDIT' AND f.category = 'REVENUE' " +
           "AND f.transactionDate BETWEEN :startDate AND :endDate")
    BigDecimal calculateRevenueByDateRange(@Param("startDate") LocalDateTime startDate, 
                                          @Param("endDate") LocalDateTime endDate);
    
    /**
     * Calculate total expenses within date range
     * Used for expense tracking and budgeting
     */
    @Query("SELECT COALESCE(SUM(f.amount), 0) FROM Financial f " +
           "WHERE f.transactionType = 'DEBIT' AND f.category = 'EXPENSE' " +
           "AND f.transactionDate BETWEEN :startDate AND :endDate")
    BigDecimal calculateExpensesByDateRange(@Param("startDate") LocalDateTime startDate, 
                                           @Param("endDate") LocalDateTime endDate);
    
    /**
     * Find transactions by category
     * Used for category-based financial analysis
     */
    @Query("SELECT f FROM Financial f WHERE LOWER(f.category) = LOWER(:category) ORDER BY f.transactionDate DESC")
    List<Financial> findByCategory(@Param("category") String category);
    
    /**
     * Find transactions by amount range
     * Used for filtering transactions by value
     */
    @Query("SELECT f FROM Financial f WHERE f.amount BETWEEN :minAmount AND :maxAmount ORDER BY f.amount DESC")
    List<Financial> findByAmountRange(@Param("minAmount") BigDecimal minAmount, 
                                     @Param("maxAmount") BigDecimal maxAmount);
    
    /**
     * Find large transactions above threshold
     * Used for monitoring high-value transactions
     */
    @Query("SELECT f FROM Financial f WHERE f.amount > :threshold ORDER BY f.amount DESC")
    List<Financial> findLargeTransactions(@Param("threshold") BigDecimal threshold);
    
    /**
     * Search transactions by description or reference
     * Used for transaction search functionality
     */
    @Query("SELECT f FROM Financial f WHERE " +
           "LOWER(f.description) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(f.reference) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<Financial> searchTransactions(@Param("searchTerm") String searchTerm);
    
    /**
     * Find pending transactions
     * Used for transaction processing and approval workflows
     */
    @Query("SELECT f FROM Financial f WHERE f.status = 'PENDING' ORDER BY f.transactionDate ASC")
    List<Financial> findPendingTransactions();
    
    /**
     * Find failed transactions
     * Used for error handling and retry mechanisms
     */
    @Query("SELECT f FROM Financial f WHERE f.status = 'FAILED' ORDER BY f.transactionDate DESC")
    List<Financial> findFailedTransactions();
    
    /**
     * Calculate profit/loss for date range
     * Used for profitability analysis
     */
    @Query("SELECT (COALESCE(SUM(CASE WHEN f.transactionType = 'CREDIT' THEN f.amount ELSE 0 END), 0) - " +
           "COALESCE(SUM(CASE WHEN f.transactionType = 'DEBIT' THEN f.amount ELSE 0 END), 0)) " +
           "FROM Financial f WHERE f.transactionDate BETWEEN :startDate AND :endDate")
    BigDecimal calculateProfitLoss(@Param("startDate") LocalDateTime startDate, 
                                  @Param("endDate") LocalDateTime endDate);
    
    /**
     * Find transactions by user/employee
     * Used for user activity tracking and auditing
     */
    @Query("SELECT f FROM Financial f WHERE f.createdBy = :userId ORDER BY f.transactionDate DESC")
    List<Financial> findTransactionsByUser(@Param("userId") Long userId);
    
    /**
     * Calculate monthly revenue trends
     * Used for financial dashboards and analytics
     */
    @Query("SELECT YEAR(f.transactionDate), MONTH(f.transactionDate), SUM(f.amount) " +
           "FROM Financial f WHERE f.transactionType = 'CREDIT' AND f.category = 'REVENUE' " +
           "GROUP BY YEAR(f.transactionDate), MONTH(f.transactionDate) " +
           "ORDER BY YEAR(f.transactionDate) DESC, MONTH(f.transactionDate) DESC")
    List<Object[]> calculateMonthlyRevenue();
    
    /**
     * Calculate expense breakdown by category
     * Used for expense analysis and budgeting
     */
    @Query("SELECT f.category, SUM(f.amount) FROM Financial f " +
           "WHERE f.transactionType = 'DEBIT' GROUP BY f.category ORDER BY SUM(f.amount) DESC")
    List<Object[]> calculateExpensesByCategory();
    
    /**
     * Find transactions requiring approval
     * Used for approval workflow management
     */
    @Query("SELECT f FROM Financial f WHERE f.amount > :approvalThreshold AND f.status = 'PENDING'")
    List<Financial> findTransactionsRequiringApproval(@Param("approvalThreshold") BigDecimal approvalThreshold);
    
    /**
     * Count transactions by status
     * Used for dashboard metrics and monitoring
     */
    @Query("SELECT f.status, COUNT(f) FROM Financial f GROUP BY f.status")
    List<Object[]> countTransactionsByStatus();
    
    /**
     * Find recurring transactions
     * Used for subscription and recurring payment management
     */
    @Query("SELECT f FROM Financial f WHERE f.isRecurring = true ORDER BY f.nextRecurrenceDate ASC")
    List<Financial> findRecurringTransactions();
    
    /**
     * Find overdue recurring transactions
     * Used for automated payment processing alerts
     */
    @Query("SELECT f FROM Financial f WHERE f.isRecurring = true AND f.nextRecurrenceDate < CURRENT_TIMESTAMP")
    List<Financial> findOverdueRecurringTransactions();
    
    /**
     * Update transaction status
     * Used for transaction processing workflows
     */
    @Modifying
    @Transactional
    @Query("UPDATE Financial f SET f.status = :status, f.updatedAt = CURRENT_TIMESTAMP WHERE f.id = :transactionId")
    void updateTransactionStatus(@Param("transactionId") Long transactionId, @Param("status") String status);
    
    /**
     * Approve transaction by ID
     * Used for transaction approval workflows
     */
    @Modifying
    @Transactional
    @Query("UPDATE Financial f SET f.status = 'APPROVED', f.approvedBy = :approvedBy, " +
           "f.approvedAt = CURRENT_TIMESTAMP, f.updatedAt = CURRENT_TIMESTAMP WHERE f.id = :transactionId")
    void approveTransaction(@Param("transactionId") Long transactionId, @Param("approvedBy") Long approvedBy);
    
    /**
     * Find transactions with pagination
     * Used for efficient data retrieval in large datasets
     */
    @Query("SELECT f FROM Financial f ORDER BY f.transactionDate DESC")
    Page<Financial> findAllTransactionsWithPagination(Pageable pageable);
    
    /**
     * Check if transaction ID exists
     * Used for duplicate transaction prevention
     */
    boolean existsByTransactionId(String transactionId);
    
    /**
     * Calculate cash flow for date range
     * Used for cash flow analysis and forecasting
     */
    @Query("SELECT DATE(f.transactionDate), " +
           "SUM(CASE WHEN f.transactionType = 'CREDIT' THEN f.amount ELSE -f.amount END) " +
           "FROM Financial f WHERE f.transactionDate BETWEEN :startDate AND :endDate " +
           "GROUP BY DATE(f.transactionDate) ORDER BY DATE(f.transactionDate)")
    List<Object[]> calculateDailyCashFlow(@Param("startDate") LocalDateTime startDate, 
                                         @Param("endDate") LocalDateTime endDate);
}
