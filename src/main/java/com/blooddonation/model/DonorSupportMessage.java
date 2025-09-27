package com.blooddonation.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Table(name = "DonorSupport_Message")
public class DonorSupportMessage {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Message_ID")
    private Long messageId;
    
    @NotBlank(message = "Donor NIC is required")
    @Column(name = "Donor_NIC", length = 12)
    private String donorNic;
    
    @NotBlank(message = "Donor full name is required")
    @Column(name = "Donor_Full_Name", length = 100)
    private String donorFullName;
    
    @NotBlank(message = "Donor email is required")
    @Column(name = "Donor_Email", length = 100)
    private String donorEmail;
    
    @NotBlank(message = "Subject is required")
    @Column(name = "Subject", length = 200)
    private String subject;
    
    @NotBlank(message = "Message text is required")
    @Column(name = "Message_Text", columnDefinition = "NVARCHAR(MAX)")
    private String messageText;
    
    @NotNull(message = "Priority is required")
    @Enumerated(EnumType.STRING)
    @Column(name = "Priority", length = 10)
    private Priority priority;
    
    @Column(name = "Reply_Text", columnDefinition = "NVARCHAR(MAX)")
    private String replyText;
    
    @Column(name = "Message_Date")
    private LocalDateTime messageDate;
    
    @Column(name = "Reply_Date")
    private LocalDateTime replyDate;
    
    // Enum for Priority
    public enum Priority {
        High, Medium, Low
    }
    
    // Constructors
    public DonorSupportMessage() {
        this.messageDate = LocalDateTime.now();
    }
    
    public DonorSupportMessage(String donorNic, String donorFullName, String donorEmail, 
                              String subject, String messageText, Priority priority) {
        this();
        this.donorNic = donorNic;
        this.donorFullName = donorFullName;
        this.donorEmail = donorEmail;
        this.subject = subject;
        this.messageText = messageText;
        this.priority = priority;
    }
    
    // Getters and Setters
    public Long getMessageId() {
        return messageId;
    }
    
    public void setMessageId(Long messageId) {
        this.messageId = messageId;
    }
    
    public String getDonorNic() {
        return donorNic;
    }
    
    public void setDonorNic(String donorNic) {
        this.donorNic = donorNic;
    }
    
    public String getDonorFullName() {
        return donorFullName;
    }
    
    public void setDonorFullName(String donorFullName) {
        this.donorFullName = donorFullName;
    }
    
    public String getDonorEmail() {
        return donorEmail;
    }
    
    public void setDonorEmail(String donorEmail) {
        this.donorEmail = donorEmail;
    }
    
    public String getSubject() {
        return subject;
    }
    
    public void setSubject(String subject) {
        this.subject = subject;
    }
    
    public String getMessageText() {
        return messageText;
    }
    
    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }
    
    public Priority getPriority() {
        return priority;
    }
    
    public void setPriority(Priority priority) {
        this.priority = priority;
    }
    
    public String getReplyText() {
        return replyText;
    }
    
    public void setReplyText(String replyText) {
        this.replyText = replyText;
        if (replyText != null && !replyText.trim().isEmpty()) {
            this.replyDate = LocalDateTime.now();
        }
    }
    
    public LocalDateTime getMessageDate() {
        return messageDate;
    }
    
    public void setMessageDate(LocalDateTime messageDate) {
        this.messageDate = messageDate;
    }
    
    public LocalDateTime getReplyDate() {
        return replyDate;
    }
    
    public void setReplyDate(LocalDateTime replyDate) {
        this.replyDate = replyDate;
    }
    
    // Helper methods
    public boolean hasReply() {
        return replyText != null && !replyText.trim().isEmpty();
    }
    
    public String getInitials() {
        if (donorFullName != null && !donorFullName.trim().isEmpty()) {
            String[] names = donorFullName.trim().split("\\s+");
            if (names.length >= 2) {
                return (names[0].charAt(0) + "" + names[1].charAt(0)).toUpperCase();
            } else {
                return donorFullName.substring(0, Math.min(2, donorFullName.length())).toUpperCase();
            }
        }
        return "??";
    }
    
    @Override
    public String toString() {
        return "DonorSupportMessage{" +
                "messageId=" + messageId +
                ", donorNic='" + donorNic + '\'' +
                ", donorFullName='" + donorFullName + '\'' +
                ", donorEmail='" + donorEmail + '\'' +
                ", subject='" + subject + '\'' +
                ", priority=" + priority +
                ", hasReply=" + hasReply() +
                ", messageDate=" + messageDate +
                ", replyDate=" + replyDate +
                '}';
    }
}