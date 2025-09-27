package com.blooddonation.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Entity
@Table(name = "SystemUser")
public class SystemUser {
    @Id
    @NotBlank(message = "User ID is required")
    @Column(name = "User_ID", length = 20)
    private String userId;

    @Column(name = "Name", length = 100)
    private String username;

    @NotBlank(message = "Password is required")
    @Column(name = "Password", length = 255)
    private String password;

    @NotBlank(message = "Role is required")
    @Column(name = "Role", length = 50)
    private String role;

    @Column(name = "Email", length = 100)
    private String email;

    @Transient
    private String fullName;

    @Column(name = "Phone", length = 100)
    private String department;

    @Column(name = "Status")
    private String isActive = "Active";
    
    // Method to check if user is active
    public boolean isActive() {
        return "Active".equals(this.isActive);
    }

    @Column(name = "Created_At", insertable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "NIC", length = 20)
    private String nic;

    // Constructors
    public SystemUser() {}

    public SystemUser(String userId, String username, String password, String role, 
                     String email, String fullName, String department) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.role = role;
        this.email = email;
        this.department = department;
        this.isActive = "Active";
    }

    // Getters and Setters
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getFullName() { 
        return this.username; // Use username as fullName since they're the same in database
    }
    
    public void setFullName(String fullName) { 
        this.username = fullName; // Set username when setting fullName
    }

    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }

    public String getIsActive() { return isActive; }
    public void setIsActive(String isActive) { this.isActive = isActive; }
    
    // Helper method to check if user is active
    public boolean isUserActive() {
        return "Active".equals(this.isActive);
    }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public String getNic() { return nic; }
    public void setNic(String nic) { this.nic = nic; }

    @Override
    public String toString() {
        return "SystemUser{" +
                "userId='" + userId + '\'' +
                ", username='" + username + '\'' +
                ", role='" + role + '\'' +
                ", email='" + email + '\'' +
                ", fullName='" + fullName + '\'' +
                ", department='" + department + '\'' +
                ", isActive=" + isActive +
                '}';
    }
}