/**
 * Enterprise Resource Planning System
 * Customer Data Transfer Object
 * 
 * This DTO represents customer information for data transfer between layers
 * supporting REST API operations and customer relationship management.
 * 
 * @author Gabriel Demetrios Lafis
 * @version 1.0
 * @since 2025-09-25
 */
package com.galafis.erp.dto;

import jakarta.validation.constraints.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.math.BigDecimal;
import java.util.List;

public class CustomerDto {
    
    private Long id;
    
    @NotBlank(message = "Customer name is required")
    @Size(min = 2, max = 100, message = "Customer name must be between 2 and 100 characters")
    private String customerName;
    
    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    private String email;
    
    @Pattern(regexp = "^[+]?[1-9]\\d{1,14}$", message = "Phone number must be valid")
    private String phoneNumber;
    
    @Size(max = 200, message = "Address cannot exceed 200 characters")
    private String address;
    
    @Size(max = 50, message = "City cannot exceed 50 characters")
    private String city;
    
    @Size(max = 50, message = "State cannot exceed 50 characters")
    private String state;
    
    @Pattern(regexp = "^[0-9]{5}(-[0-9]{4})?$", message = "Postal code must be valid")
    private String postalCode;
    
    @Size(max = 50, message = "Country cannot exceed 50 characters")
    private String country;
    
    @Size(max = 100, message = "Company name cannot exceed 100 characters")
    private String companyName;
    
    @Size(max = 50, message = "Industry cannot exceed 50 characters")
    private String industry;
    
    @Size(max = 20, message = "Customer type cannot exceed 20 characters")
    private String customerType; // INDIVIDUAL, BUSINESS, ENTERPRISE
    
    @Size(max = 20, message = "Status cannot exceed 20 characters")
    private String status; // ACTIVE, INACTIVE, PROSPECT, LEAD
    
    @DecimalMin(value = "0.00", message = "Credit limit cannot be negative")
    @Digits(integer = 15, fraction = 2, message = "Credit limit format invalid")
    private BigDecimal creditLimit;
    
    @DecimalMin(value = "0.00", message = "Total orders value cannot be negative")
    @Digits(integer = 15, fraction = 2, message = "Total orders value format invalid")
    private BigDecimal totalOrdersValue;
    
    @Min(value = 0, message = "Total orders count cannot be negative")
    private Integer totalOrdersCount;
    
    @DecimalMin(value = "0.00", message = "Outstanding balance cannot be negative")
    @Digits(integer = 15, fraction = 2, message = "Outstanding balance format invalid")
    private BigDecimal outstandingBalance;
    
    private LocalDate lastOrderDate;
    
    private LocalDate registrationDate;
    
    private LocalDateTime createdAt;
    
    private LocalDateTime lastUpdated;
    
    @Size(max = 100, message = "Assigned sales rep cannot exceed 100 characters")
    private String assignedSalesRep;
    
    private List<String> tags;
    
    @Size(max = 500, message = "Notes cannot exceed 500 characters")
    private String notes;
    
    // Default constructor
    public CustomerDto() {
        this.status = "PROSPECT";
        this.customerType = "INDIVIDUAL";
        this.totalOrdersCount = 0;
        this.totalOrdersValue = BigDecimal.ZERO;
        this.outstandingBalance = BigDecimal.ZERO;
    }
    
    // Constructor with essential fields
    public CustomerDto(String customerName, String email, String customerType) {
        this.customerName = customerName;
        this.email = email;
        this.customerType = customerType;
        this.status = "PROSPECT";
        this.totalOrdersCount = 0;
        this.totalOrdersValue = BigDecimal.ZERO;
        this.outstandingBalance = BigDecimal.ZERO;
    }
    
    // Getters and Setters
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getCustomerName() {
        return customerName;
    }
    
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getPhoneNumber() {
        return phoneNumber;
    }
    
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    
    public String getAddress() {
        return address;
    }
    
    public void setAddress(String address) {
        this.address = address;
    }
    
    public String getCity() {
        return city;
    }
    
    public void setCity(String city) {
        this.city = city;
    }
    
    public String getState() {
        return state;
    }
    
    public void setState(String state) {
        this.state = state;
    }
    
    public String getPostalCode() {
        return postalCode;
    }
    
    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }
    
    public String getCountry() {
        return country;
    }
    
    public void setCountry(String country) {
        this.country = country;
    }
    
    public String getCompanyName() {
        return companyName;
    }
    
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
    
    public String getIndustry() {
        return industry;
    }
    
    public void setIndustry(String industry) {
        this.industry = industry;
    }
    
    public String getCustomerType() {
        return customerType;
    }
    
    public void setCustomerType(String customerType) {
        this.customerType = customerType;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public BigDecimal getCreditLimit() {
        return creditLimit;
    }
    
    public void setCreditLimit(BigDecimal creditLimit) {
        this.creditLimit = creditLimit;
    }
    
    public BigDecimal getTotalOrdersValue() {
        return totalOrdersValue;
    }
    
    public void setTotalOrdersValue(BigDecimal totalOrdersValue) {
        this.totalOrdersValue = totalOrdersValue;
    }
    
    public Integer getTotalOrdersCount() {
        return totalOrdersCount;
    }
    
    public void setTotalOrdersCount(Integer totalOrdersCount) {
        this.totalOrdersCount = totalOrdersCount;
    }
    
    public BigDecimal getOutstandingBalance() {
        return outstandingBalance;
    }
    
    public void setOutstandingBalance(BigDecimal outstandingBalance) {
        this.outstandingBalance = outstandingBalance;
    }
    
    public LocalDate getLastOrderDate() {
        return lastOrderDate;
    }
    
    public void setLastOrderDate(LocalDate lastOrderDate) {
        this.lastOrderDate = lastOrderDate;
    }
    
    public LocalDate getRegistrationDate() {
        return registrationDate;
    }
    
    public void setRegistrationDate(LocalDate registrationDate) {
        this.registrationDate = registrationDate;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public LocalDateTime getLastUpdated() {
        return lastUpdated;
    }
    
    public void setLastUpdated(LocalDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
    
    public String getAssignedSalesRep() {
        return assignedSalesRep;
    }
    
    public void setAssignedSalesRep(String assignedSalesRep) {
        this.assignedSalesRep = assignedSalesRep;
    }
    
    public List<String> getTags() {
        return tags;
    }
    
    public void setTags(List<String> tags) {
        this.tags = tags;
    }
    
    public String getNotes() {
        return notes;
    }
    
    public void setNotes(String notes) {
        this.notes = notes;
    }
    
    // Business logic methods
    
    /**
     * Checks if customer is a high-value customer
     * @return true if total orders value exceeds $10,000
     */
    public boolean isHighValueCustomer() {
        return totalOrdersValue != null && totalOrdersValue.compareTo(BigDecimal.valueOf(10000)) > 0;
    }
    
    /**
     * Checks if customer has outstanding balance
     * @return true if outstanding balance is greater than zero
     */
    public boolean hasOutstandingBalance() {
        return outstandingBalance != null && outstandingBalance.compareTo(BigDecimal.ZERO) > 0;
    }
    
    /**
     * Calculates average order value
     * @return average order value
     */
    public BigDecimal getAverageOrderValue() {
        if (totalOrdersValue != null && totalOrdersCount != null && totalOrdersCount > 0) {
            return totalOrdersValue.divide(BigDecimal.valueOf(totalOrdersCount), 2, BigDecimal.ROUND_HALF_UP);
        }
        return BigDecimal.ZERO;
    }
}
