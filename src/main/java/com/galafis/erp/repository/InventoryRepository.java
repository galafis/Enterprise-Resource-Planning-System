package com.galafis.erp.repository;

import com.galafis.erp.entity.Inventory;
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
 * Repository interface for Inventory entity
 * 
 * Provides comprehensive data access layer for Inventory management including:
 * - Basic CRUD operations through JpaRepository
 * - Stock level monitoring and alerts
 * - Product availability queries
 * - Inventory movement tracking
 * - Cost and valuation calculations
 * - Supplier and location-based queries
 * 
 * @author Gabriel Demetrios Lafis
 */
@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Long> {
    
    /**
     * Find inventory item by product SKU
     * Used for product lookup and stock verification
     */
    Optional<Inventory> findByProductSku(String productSku);
    
    /**
     * Find all inventory items by location
     * Used for location-based inventory management
     */
    List<Inventory> findByLocationIgnoreCase(String location);
    
    /**
     * Find all inventory items by supplier
     * Used for supplier management and procurement
     */
    List<Inventory> findBySupplierIdOrderByProductSkuAsc(Long supplierId);
    
    /**
     * Find items with low stock levels
     * Used for automated reorder alerts
     */
    @Query("SELECT i FROM Inventory i WHERE i.currentStock <= i.minimumStockLevel ORDER BY i.currentStock ASC")
    List<Inventory> findLowStockItems();
    
    /**
     * Find items with zero stock
     * Used for out-of-stock management
     */
    @Query("SELECT i FROM Inventory i WHERE i.currentStock = 0")
    List<Inventory> findOutOfStockItems();
    
    /**
     * Find items with stock above maximum level
     * Used for overstock identification
     */
    @Query("SELECT i FROM Inventory i WHERE i.currentStock > i.maximumStockLevel ORDER BY i.currentStock DESC")
    List<Inventory> findOverstockedItems();
    
    /**
     * Find items by product category
     * Used for category-based inventory reporting
     */
    @Query("SELECT i FROM Inventory i WHERE LOWER(i.category) = LOWER(:category) ORDER BY i.productName")
    List<Inventory> findByCategory(@Param("category") String category);
    
    /**
     * Search inventory items by product name or SKU
     * Used for inventory search functionality
     */
    @Query("SELECT i FROM Inventory i WHERE " +
           "LOWER(i.productName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(i.productSku) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<Inventory> searchInventoryItems(@Param("searchTerm") String searchTerm);
    
    /**
     * Find items requiring reorder (below minimum stock level)
     * Used for procurement planning
     */
    @Query("SELECT i FROM Inventory i WHERE i.currentStock < i.reorderPoint AND i.isActive = true")
    List<Inventory> findItemsRequiringReorder();
    
    /**
     * Calculate total inventory value
     * Used for financial reporting and valuation
     */
    @Query("SELECT SUM(i.currentStock * i.unitCost) FROM Inventory i WHERE i.isActive = true")
    BigDecimal calculateTotalInventoryValue();
    
    /**
     * Calculate inventory value by category
     * Used for category-based financial analysis
     */
    @Query("SELECT i.category, SUM(i.currentStock * i.unitCost) FROM Inventory i " +
           "WHERE i.isActive = true GROUP BY i.category ORDER BY SUM(i.currentStock * i.unitCost) DESC")
    List<Object[]> calculateInventoryValueByCategory();
    
    /**
     * Find items with movement in date range
     * Used for inventory turnover analysis
     */
    @Query("SELECT i FROM Inventory i WHERE i.lastMovementDate BETWEEN :startDate AND :endDate")
    List<Inventory> findItemsWithMovementInDateRange(@Param("startDate") LocalDateTime startDate, 
                                                    @Param("endDate") LocalDateTime endDate);
    
    /**
     * Find slow-moving items (no movement in specified days)
     * Used for inventory optimization
     */
    @Query("SELECT i FROM Inventory i WHERE i.lastMovementDate < :cutoffDate OR i.lastMovementDate IS NULL")
    List<Inventory> findSlowMovingItems(@Param("cutoffDate") LocalDateTime cutoffDate);
    
    /**
     * Find expired or expiring items
     * Used for expiry management and waste reduction
     */
    @Query("SELECT i FROM Inventory i WHERE i.expiryDate <= :expiryDate AND i.expiryDate IS NOT NULL")
    List<Inventory> findExpiringItems(@Param("expiryDate") LocalDateTime expiryDate);
    
    /**
     * Count items by location
     * Used for location-based analytics
     */
    @Query("SELECT i.location, COUNT(i) FROM Inventory i GROUP BY i.location")
    List<Object[]> countItemsByLocation();
    
    /**
     * Update stock level for a product
     * Used for inventory adjustments and transactions
     */
    @Modifying
    @Transactional
    @Query("UPDATE Inventory i SET i.currentStock = :newStock, i.lastMovementDate = CURRENT_TIMESTAMP " +
           "WHERE i.productSku = :productSku")
    void updateStockLevel(@Param("productSku") String productSku, @Param("newStock") Integer newStock);
    
    /**
     * Adjust stock level (add or subtract)
     * Used for stock movements and transactions
     */
    @Modifying
    @Transactional
    @Query("UPDATE Inventory i SET i.currentStock = i.currentStock + :adjustment, " +
           "i.lastMovementDate = CURRENT_TIMESTAMP WHERE i.productSku = :productSku")
    void adjustStockLevel(@Param("productSku") String productSku, @Param("adjustment") Integer adjustment);
    
    /**
     * Deactivate inventory item
     * Used for product discontinuation
     */
    @Modifying
    @Transactional
    @Query("UPDATE Inventory i SET i.isActive = false WHERE i.id = :inventoryId")
    void deactivateInventoryItem(@Param("inventoryId") Long inventoryId);
    
    /**
     * Find active inventory items with pagination
     * Used for efficient data retrieval in large datasets
     */
    @Query("SELECT i FROM Inventory i WHERE i.isActive = true ORDER BY i.productName")
    Page<Inventory> findActiveInventoryItems(Pageable pageable);
    
    /**
     * Find items by price range
     * Used for pricing analysis and filtering
     */
    @Query("SELECT i FROM Inventory i WHERE i.unitPrice BETWEEN :minPrice AND :maxPrice")
    List<Inventory> findByPriceRange(@Param("minPrice") BigDecimal minPrice, @Param("maxPrice") BigDecimal maxPrice);
    
    /**
     * Check if product exists by SKU (case-insensitive)
     * Used for validation during product creation
     */
    boolean existsByProductSkuIgnoreCase(String productSku);
}
