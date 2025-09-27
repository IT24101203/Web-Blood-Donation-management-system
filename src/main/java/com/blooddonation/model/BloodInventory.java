package com.blooddonation.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDate;

@Entity
@Table(name = "Blood_Inventory")
public class BloodInventory {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Inventory_ID")
    private Integer inventoryId;
    
    @NotBlank(message = "Blood type is required")
    @Column(name = "Blood_Type", length = 5)
    private String bloodType;
    
    @NotNull(message = "Quantity is required")
    @Min(value = 1, message = "Quantity must be at least 1")
    @Column(name = "Quantity")
    private Integer quantity;
    
    @NotNull(message = "Collection date is required")
    @Column(name = "Collection_Date")
    private LocalDate collectionDate;
    
    @NotNull(message = "Expiry date is required")
    @Column(name = "Expiry_Date")
    private LocalDate expiryDate;
    
    @NotBlank(message = "Status is required")
    @Column(name = "Status", length = 20)
    private String status = "Available";
    
    // Constructors
    public BloodInventory() {}
    
    public BloodInventory(String bloodType, Integer quantity, LocalDate collectionDate, 
                         LocalDate expiryDate, String status) {
        this.bloodType = bloodType;
        this.quantity = quantity;
        this.collectionDate = collectionDate;
        this.expiryDate = expiryDate;
        this.status = status;
    }
    
    // Getters and Setters
    public Integer getInventoryId() {
        return inventoryId;
    }
    
    public void setInventoryId(Integer inventoryId) {
        this.inventoryId = inventoryId;
    }
    
    public String getBloodType() {
        return bloodType;
    }
    
    public void setBloodType(String bloodType) {
        this.bloodType = bloodType;
    }
    
    public Integer getQuantity() {
        return quantity;
    }
    
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
    
    public LocalDate getCollectionDate() {
        return collectionDate;
    }
    
    public void setCollectionDate(LocalDate collectionDate) {
        this.collectionDate = collectionDate;
    }
    
    public LocalDate getExpiryDate() {
        return expiryDate;
    }
    
    public void setExpiryDate(LocalDate expiryDate) {
        this.expiryDate = expiryDate;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    @Override
    public String toString() {
        return "BloodInventory{" +
                "inventoryId=" + inventoryId +
                ", bloodType='" + bloodType + '\'' +
                ", quantity=" + quantity +
                ", collectionDate=" + collectionDate +
                ", expiryDate=" + expiryDate +
                ", status='" + status + '\'' +
                '}';
    }
}
