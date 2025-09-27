package com.blooddonation.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Table(name = "ReceiverSupport_Message")
public class ReceiverSupportMessage {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Message_ID")
    private Long messageId;
    
    @NotBlank(message = "Receiver NIC is required")
    @Column(name = "Receiver_CNIC", length = 12)
    private String receiverNic;
    
    @NotBlank(message = "Receiver full name is required")
    @Column(name = "Receiver_Full_Name", length = 100)
    private String receiverFullName;
    
    @NotBlank(message = "Receiver email is required")
    @Column(name = "Receiver_Email", length = 100)
    private String receiverEmail;
    
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
    public ReceiverSupportMessage() {
        this.messageDate = LocalDateTime.now();
    }
    
    public ReceiverSupportMessage(String receiverNic, String receiverFullName, String receiverEmail, 
                                 String subject, String messageText, Priority priority) {
        this();
        this.receiverNic = receiverNic;
        this.receiverFullName = receiverFullName;
        this.receiverEmail = receiverEmail;
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
    
    public String getReceiverNic() {
        return receiverNic;
    }
    
    public void setReceiverNic(String receiverNic) {
        this.receiverNic = receiverNic;
    }
    
    public String getReceiverFullName() {
        return receiverFullName;
    }
    
    public void setReceiverFullName(String receiverFullName) {
        this.receiverFullName = receiverFullName;
    }
    
    public String getReceiverEmail() {
        return receiverEmail;
    }
    
    public void setReceiverEmail(String receiverEmail) {
        this.receiverEmail = receiverEmail;
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
    
    @Override
    public String toString() {
        return "ReceiverSupportMessage{" +
                "messageId=" + messageId +
                ", receiverNic='" + receiverNic + '\'' +
                ", receiverFullName='" + receiverFullName + '\'' +
                ", receiverEmail='" + receiverEmail + '\'' +
                ", subject='" + subject + '\'' +
                ", messageText='" + messageText + '\'' +
                ", priority=" + priority +
                ", replyText='" + replyText + '\'' +
                ", messageDate=" + messageDate +
                ", replyDate=" + replyDate +
                '}';
    }
}
