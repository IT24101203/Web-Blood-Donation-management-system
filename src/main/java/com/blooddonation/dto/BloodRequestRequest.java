package com.blooddonation.dto;

import jakarta.validation.constraints.*;
import java.time.LocalDate;

public class BloodRequestRequest {
    
    @NotBlank(message = "Receiver CNIC is required")
    @Pattern(regexp = "^[0-9]{5}-[0-9]{7}-[0-9]{1}$", message = "CNIC must be in format 12345-1234567-1")
    private String receiverCNIC;
    
    @NotBlank(message = "Blood type is required")
    @Pattern(regexp = "^(A\\+|A\\-|B\\+|B\\-|AB\\+|AB\\-|O\\+|O\\-)$", message = "Invalid blood type")
    private String bloodType;
    
    @NotNull(message = "Quantity is required")
    @Min(value = 1, message = "Quantity must be at least 1")
    @Max(value = 10, message = "Quantity cannot exceed 10")
    private Integer quantity;
    
    @NotNull(message = "Needed by date is required")
    @Future(message = "Needed by date must be in the future")
    private LocalDate neededByDate;
    
    @NotBlank(message = "Hospital is required")
    @Size(max = 100, message = "Hospital name cannot exceed 100 characters")
    private String hospital;
    
    @NotBlank(message = "Priority is required")
    @Pattern(regexp = "^(Low|Normal|High|Critical)$", message = "Invalid priority level")
    private String priority;
    
    @Size(max = 500, message = "Notes cannot exceed 500 characters")
    private String notes;
    
    // Constructors
    public BloodRequestRequest() {}
    
    public BloodRequestRequest(String receiverCNIC, String bloodType, Integer quantity, 
                              LocalDate neededByDate, String hospital, String priority, String notes) {
        this.receiverCNIC = receiverCNIC;
        this.bloodType = bloodType;
        this.quantity = quantity;
        this.neededByDate = neededByDate;
        this.hospital = hospital;
        this.priority = priority;
        this.notes = notes;
    }
    
    // Getters and Setters
    public String getReceiverCNIC() {
        return receiverCNIC;
    }
    
    public void setReceiverCNIC(String receiverCNIC) {
        this.receiverCNIC = receiverCNIC;
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
    
    public LocalDate getNeededByDate() {
        return neededByDate;
    }
    
    public void setNeededByDate(LocalDate neededByDate) {
        this.neededByDate = neededByDate;
    }
    
    public String getHospital() {
        return hospital;
    }
    
    public void setHospital(String hospital) {
        this.hospital = hospital;
    }
    
    public String getPriority() {
        return priority;
    }
    
    public void setPriority(String priority) {
        this.priority = priority;
    }
    
    public String getNotes() {
        return notes;
    }
    
    public void setNotes(String notes) {
        this.notes = notes;
    }
    
    @Override
    public String toString() {
        return "BloodRequestRequest{" +
                "receiverCNIC='" + receiverCNIC + '\'' +
                ", bloodType='" + bloodType + '\'' +
                ", quantity=" + quantity +
                ", neededByDate=" + neededByDate +
                ", hospital='" + hospital + '\'' +
                ", priority='" + priority + '\'' +
                ", notes='" + notes + '\'' +
                '}';
    }
}

