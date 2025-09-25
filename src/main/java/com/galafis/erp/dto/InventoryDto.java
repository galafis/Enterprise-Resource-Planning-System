/**
 * Enterprise Resource Planning System
 * Inventory Data Transfer Object
 * 
 * This DTO represents inventory item information for data transfer between layers
 * supporting REST API operations and data validation for stock management.
 * 
 * @author Gabriel Demetrios Lafis
 * @version 1.0
 * @since 2025-09-25
 */
package com.galafis.erp.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class InventoryDto {
    
    private Long id;
    
    @NotBlank(message = "Product name is required")
    @Size(min = 2, max = 100, message = "Product name must be between 2 and 100 characters")
    private String productName;
    
    @NotBlank(message = "Product code is required")
    @Size(min = 3, max = 50, message = "Product code must be between 3 and 50 characters")
    private String productCode;
    
    @Size(max = 500, message = "Description cannot exceed 500 characters")
    private String description;
    
    @NotBlank(message = "Category is required")
    @Size(min = 2, max = 50, message = "Category must be between 2 and 50 characters")
    private String category;
    
    @NotNull(message = "Current stock is required")
    @Min(value = 0, message = "Current stock cannot be negative")
    private Integer currentStock;
    
    @NotNull(message = "Minimum stock level is required")
    @Min(value = 0, message = "Minimum stock level cannot be negative")
    private Integer minStockLevel;
    
    @NotNull(message = "Maximum stock level is required")
    @Min(value = 1, message = "Maximum stock level must be at least 1")
    private Integer maxStockLevel;
    
    @NotNull(message = "Unit price is required")
    @DecimalMin(value = "0.00", message = "Unit price cannot be negative")
    @Digits(integer = 10, fraction = 2, message = "Unit price must have at most 10 digits and 2 decimal places")
    private BigDecimal unitPrice;
    
    @NotNull(message = "Cost price is required")
    @DecimalMin(value = "0.00", message = "Cost price cannot be negative")
    @Digits(integer = 10, fraction = 2, message = "Cost price must have at most 10 digits and 2 decimal places")
    private BigDecimal costPrice;
    
    @NotBlank(message = "Unit of measure is required")
    @Size(min = 1, max = 20, message = "Unit of measure must be between 1 and 20 characters")
    private String unitOfMeasure;
    
    @Size(max = 100, message = "Supplier cannot exceed 100 characters")
    private String supplier;
    
    @Size(max = 50, message = "Location cannot exceed 50 characters")
    private String location;
    
    private Boolean active;
    
    private LocalDateTime lastUpdated;
    
    private LocalDateTime createdAt;
    
    @Size(max = 100, message = "Last updated by cannot exceed 100 characters")
    private String lastUpdatedBy;
    
    // Default constructor
    public InventoryDto() {
        this.active = true;
    }
    
    // Constructor with essential fields
    public InventoryDto(String productName, String productCode, String category, Integer currentStock) {
        this.productName = productName;
        this.productCode = productCode;
        this.category = category;
        this.currentStock = currentStock;
        this.active = true;
    }
    
    // Getters and Setters
    
    /**
     * Gets the inventory item ID
     * @return inventory item ID
     */
    public Long getId() {
        return id;
    }
    
    /**
     * Sets the inventory item ID
     * @param id inventory item ID
     */
    public void setId(Long id) {
        this.id = id;
    }
    
    /**
     * Gets the product name
     * @return product name
     */
    public String getProductName() {
        return productName;
    }
    
    /**
     * Sets the product name
     * @param productName product name
     */
    public void setProductName(String productName) {
        this.productName = productName;
    }
    
    /**
     * Gets the product code
     * @return product code
     */
    public String getProductCode() {
        return productCode;
    }
    
    /**
     * Sets the product code
     * @param productCode product code
     */
    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }
    
    /**
     * Gets the description
     * @return description
     */
    public String getDescription() {
        return description;
    }
    
    /**
     * Sets the description
     * @param description description
     */
    public void setDescription(String description) {
        this.description = description;
    }
    
    /**
     * Gets the category
     * @return category
     */
    public String getCategory() {
        return category;
    }
    
    /**
     * Sets the category
     * @param category category
     */
    public void setCategory(String category) {
        this.category = category;
    }
    
    /**
     * Gets the current stock
     * @return current stock
     */
    public Integer getCurrentStock() {
        return currentStock;
    }
    
    /**
     * Sets the current stock
     * @param currentStock current stock
     */
    public void setCurrentStock(Integer currentStock) {
        this.currentStock = currentStock;
    }
    
    /**
     * Gets the minimum stock level
     * @return minimum stock level
     */
    public Integer getMinStockLevel() {
        return minStockLevel;
    }
    
    /**
     * Sets the minimum stock level
     * @param minStockLevel minimum stock level
     */
    public void setMinStockLevel(Integer minStockLevel) {
        this.minStockLevel = minStockLevel;
    }
    
    /**
     * Gets the maximum stock level
     * @return maximum stock level
     */
    public Integer getMaxStockLevel() {
        return maxStockLevel;
    }
    
    /**
     * Sets the maximum stock level
     * @param maxStockLevel maximum stock level
     */
    public void setMaxStockLevel(Integer maxStockLevel) {
        this.maxStockLevel = maxStockLevel;
    }
    
    /**
     * Gets the unit price
     * @return unit price
     */
    public BigDecimal getUnitPrice() {
        return unitPrice;
    }
    
    /**
     * Sets the unit price
     * @param unitPrice unit price
     */
    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }
    
    /**
     * Gets the cost price
     * @return cost price
     */
    public BigDecimal getCostPrice() {
        return costPrice;
    }
    
    /**
     * Sets the cost price
     * @param costPrice cost price
     */
    public void setCostPrice(BigDecimal costPrice) {
        this.costPrice = costPrice;
    }
    
    /**
     * Gets the unit of measure
     * @return unit of measure
     */
    public String getUnitOfMeasure() {
        return unitOfMeasure;
    }
    
    /**
     * Sets the unit of measure
     * @param unitOfMeasure unit of measure
     */
    public void setUnitOfMeasure(String unitOfMeasure) {
        this.unitOfMeasure = unitOfMeasure;
    }
    
    /**
     * Gets the supplier
     * @return supplier
     */
    public String getSupplier() {
        return supplier;
    }
    
    /**
     * Sets the supplier
     * @param supplier supplier
     */
    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }
    
    /**
     * Gets the location
     * @return location
     */
    public String getLocation() {
        return location;
    }
    
    /**
     * Sets the location
     * @param location location
     */
    public void setLocation(String location) {
        this.location = location;
    }
    
    /**
     * Gets the active status
     * @return true if item is active
     */
    public Boolean getActive() {
        return active;
    }
    
    /**
     * Sets the active status
     * @param active active status
     */
    public void setActive(Boolean active) {
        this.active = active;
    }
    
    /**
     * Gets the last updated timestamp
     * @return last updated timestamp
     */
    public LocalDateTime getLastUpdated() {
        return lastUpdated;
    }
    
    /**
     * Sets the last updated timestamp
     * @param lastUpdated last updated timestamp
     */
    public void setLastUpdated(LocalDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
    
    /**
     * Gets the creation timestamp
     * @return creation timestamp
     */
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    /**
     * Sets the creation timestamp
     * @param createdAt creation timestamp
     */
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    /**
     * Gets the last updated by user
     * @return last updated by user
     */
    public String getLastUpdatedBy() {
        return lastUpdatedBy;
    }
    
    /**
     * Sets the last updated by user
     * @param lastUpdatedBy last updated by user
     */
    public void setLastUpdatedBy(String lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }
    
    /**
     * Checks if the stock is below minimum level
     * @return true if stock is low
     */
    public boolean isLowStock() {
        return currentStock != null && minStockLevel != null && currentStock <= minStockLevel;
    }
    
    /**
     * Calculates the profit margin
     * @return profit margin as BigDecimal
     */
    public BigDecimal getProfitMargin() {
        if (unitPrice != null && costPrice != null && costPrice.compareTo(BigDecimal.ZERO) > 0) {
            return unitPrice.subtract(costPrice).divide(costPrice, 4, BigDecimal.ROUND_HALF_UP);
        }
        return BigDecimal.ZERO;
    }
}
