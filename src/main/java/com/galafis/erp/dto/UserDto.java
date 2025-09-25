/**
 * Enterprise Resource Planning System
 * User Data Transfer Object
 * 
 * This DTO represents user information for data transfer between layers
 * supporting REST API operations and data validation.
 * 
 * @author Gabriel Demetrios Lafis
 * @version 1.0
 * @since 2025-09-25
 */
package com.galafis.erp.dto;

import jakarta.validation.constraints.*;
import java.time.LocalDateTime;
import java.util.Set;

public class UserDto {
    
    private Long id;
    
    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
    private String username;
    
    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    private String email;
    
    @NotBlank(message = "First name is required")
    @Size(min = 2, max = 50, message = "First name must be between 2 and 50 characters")
    private String firstName;
    
    @NotBlank(message = "Last name is required")
    @Size(min = 2, max = 50, message = "Last name must be between 2 and 50 characters")
    private String lastName;
    
    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters")
    private String password;
    
    @NotNull(message = "Active status is required")
    private Boolean active;
    
    private Set<String> roles;
    
    private String department;
    
    private String position;
    
    @Pattern(regexp = "^[+]?[1-9]\\d{1,14}$", message = "Phone number must be valid")
    private String phoneNumber;
    
    private LocalDateTime createdAt;
    
    private LocalDateTime lastLogin;
    
    // Default constructor
    public UserDto() {}
    
    // Constructor with essential fields
    public UserDto(String username, String email, String firstName, String lastName) {
        this.username = username;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
    }
    
    // Getters and Setters
    
    /**
     * Gets the user ID
     * @return user ID
     */
    public Long getId() {
        return id;
    }
    
    /**
     * Sets the user ID
     * @param id user ID
     */
    public void setId(Long id) {
        this.id = id;
    }
    
    /**
     * Gets the username
     * @return username
     */
    public String getUsername() {
        return username;
    }
    
    /**
     * Sets the username
     * @param username username
     */
    public void setUsername(String username) {
        this.username = username;
    }
    
    /**
     * Gets the email
     * @return email
     */
    public String getEmail() {
        return email;
    }
    
    /**
     * Sets the email
     * @param email email
     */
    public void setEmail(String email) {
        this.email = email;
    }
    
    /**
     * Gets the first name
     * @return first name
     */
    public String getFirstName() {
        return firstName;
    }
    
    /**
     * Sets the first name
     * @param firstName first name
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    
    /**
     * Gets the last name
     * @return last name
     */
    public String getLastName() {
        return lastName;
    }
    
    /**
     * Sets the last name
     * @param lastName last name
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    
    /**
     * Gets the password
     * @return password
     */
    public String getPassword() {
        return password;
    }
    
    /**
     * Sets the password
     * @param password password
     */
    public void setPassword(String password) {
        this.password = password;
    }
    
    /**
     * Gets the active status
     * @return true if user is active
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
     * Gets the user roles
     * @return set of roles
     */
    public Set<String> getRoles() {
        return roles;
    }
    
    /**
     * Sets the user roles
     * @param roles set of roles
     */
    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }
    
    /**
     * Gets the department
     * @return department
     */
    public String getDepartment() {
        return department;
    }
    
    /**
     * Sets the department
     * @param department department
     */
    public void setDepartment(String department) {
        this.department = department;
    }
    
    /**
     * Gets the position
     * @return position
     */
    public String getPosition() {
        return position;
    }
    
    /**
     * Sets the position
     * @param position position
     */
    public void setPosition(String position) {
        this.position = position;
    }
    
    /**
     * Gets the phone number
     * @return phone number
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }
    
    /**
     * Sets the phone number
     * @param phoneNumber phone number
     */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
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
     * Gets the last login timestamp
     * @return last login timestamp
     */
    public LocalDateTime getLastLogin() {
        return lastLogin;
    }
    
    /**
     * Sets the last login timestamp
     * @param lastLogin last login timestamp
     */
    public void setLastLogin(LocalDateTime lastLogin) {
        this.lastLogin = lastLogin;
    }
}
