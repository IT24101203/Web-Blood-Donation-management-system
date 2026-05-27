package com.blooddonation.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "BloodRequest")
public class BloodRequest {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Request_ID")
    private Long requestId;
    
    @Column(name = "Receiver_CNIC", nullable = false, length = 12)
    private String receiverCnic;
    
    @Column(name = "Blood_Type_Needed", nullable = false, length = 5)
    private String bloodTypeNeeded;
    
    @Column(name = "Quantity", nullable = false)
    private Integer quantity;
    
    @Column(name = "Request_Date")
    private LocalDateTime requestDate;
    
    @Column(name = "Needed_By_Date", nullable = false)
    private LocalDate neededByDate;
    
    @Column(name = "Hospital", nullable = false, length = 100)
    private String hospital;
    
    @Column(name = "Priority", length = 20)
    private String priority = "Normal";
    
    @Column(name = "Notes", length = 500)
    private String notes;
    
    @Column(name = "Approved_Date")
    private LocalDateTime approvedDate;
    
    @Column(name = "Completion_Date")
    private LocalDateTime completionDate;
    
    // Transient field for receiver name (not stored in database)
    @Transient
    private String receiverName;
    
    // Constructors
    public BloodRequest() {
        this.requestDate = LocalDateTime.now();
    }
    
    public BloodRequest(String receiverCnic, String bloodTypeNeeded, Integer quantity, 
                       LocalDate neededByDate, String hospital, String priority, String notes) {
        this();
        this.receiverCnic = receiverCnic;
        this.bloodTypeNeeded = bloodTypeNeeded;
        this.quantity = quantity;
        this.neededByDate = neededByDate;
        this.hospital = hospital;
        this.priority = priority;
        this.notes = notes;
    }
    
    // Getters and Setters
    public Long getRequestId() {
        return requestId;
    }
    
    public void setRequestId(Long requestId) {
        this.requestId = requestId;
    }
    
    public String getReceiverCnic() {
        return receiverCnic;
    }
    
    public void setReceiverCnic(String receiverCnic) {
        this.receiverCnic = receiverCnic;
    }
    
    public String getBloodTypeNeeded() {
        return bloodTypeNeeded;
    }
    
    public void setBloodTypeNeeded(String bloodTypeNeeded) {
        this.bloodTypeNeeded = bloodTypeNeeded;
    }
    
    public Integer getQuantity() {
        return quantity;
    }
    
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
    
    public LocalDateTime getRequestDate() {
        return requestDate;
    }
    
    public void setRequestDate(LocalDateTime requestDate) {
        this.requestDate = requestDate;
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
    
    public LocalDateTime getApprovedDate() {
        return approvedDate;
    }
    
    public void setApprovedDate(LocalDateTime approvedDate) {
        this.approvedDate = approvedDate;
    }
    
    public LocalDateTime getCompletionDate() {
        return completionDate;
    }
    
    public void setCompletionDate(LocalDateTime completionDate) {
        this.completionDate = completionDate;
    }
    
    public String getReceiverName() {
        return receiverName;
    }
    
    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }
    
    @Override
    public String toString() {
        return "BloodRequest{" +
                "requestId=" + requestId +
                ", receiverCnic='" + receiverCnic + '\'' +
                ", bloodTypeNeeded='" + bloodTypeNeeded + '\'' +
                ", quantity=" + quantity +
                ", requestDate=" + requestDate +
                ", neededByDate=" + neededByDate +
                ", hospital='" + hospital + '\'' +
                ", priority='" + priority + '\'' +
                ", notes='" + notes + '\'' +
                ", approvedDate=" + approvedDate +
                ", completionDate=" + completionDate +
                '}';
    }
}
