package com.blooddonation.service;

import com.blooddonation.model.DonorSupportMessage;
import java.util.List;
import java.util.Optional;

public interface DonorSupportMessageService {
    
    /**
     * Get all messages ordered by message date (newest first)
     */
    List<DonorSupportMessage> getAllMessages();
    
    /**
     * Get messages by donor NIC
     */
    List<DonorSupportMessage> getMessagesByDonorNic(String donorNic);
    
    /**
     * Get messages by priority
     */
    List<DonorSupportMessage> getMessagesByPriority(DonorSupportMessage.Priority priority);
    
    /**
     * Get pending messages (without replies)
     */
    List<DonorSupportMessage> getPendingMessages();
    
    /**
     * Get resolved messages (with replies)
     */
    List<DonorSupportMessage> getResolvedMessages();
    
    /**
     * Get high priority pending messages
     */
    List<DonorSupportMessage> getHighPriorityPendingMessages();
    
    /**
     * Get message by ID
     */
    Optional<DonorSupportMessage> getMessageById(Long messageId);
    
    /**
     * Save a new message
     */
    DonorSupportMessage saveMessage(DonorSupportMessage message);
    
    /**
     * Update message with reply
     */
    DonorSupportMessage replyToMessage(Long messageId, String replyText);
    
    /**
     * Delete a message
     */
    void deleteMessage(Long messageId);
    
    /**
     * Get dashboard statistics
     */
    DashboardStats getDashboardStats();
    
    /**
     * Dashboard statistics class
     */
    class DashboardStats {
        private long pendingMessages;
        private long resolvedMessages;
        private long highPriorityMessages;
        
        public DashboardStats(long pendingMessages, long resolvedMessages, long highPriorityMessages) {
            this.pendingMessages = pendingMessages;
            this.resolvedMessages = resolvedMessages;
            this.highPriorityMessages = highPriorityMessages;
        }
        
        // Getters
        public long getPendingMessages() { return pendingMessages; }
        public long getResolvedMessages() { return resolvedMessages; }
        public long getHighPriorityMessages() { return highPriorityMessages; }
        
        // Setters
        public void setPendingMessages(long pendingMessages) { this.pendingMessages = pendingMessages; }
        public void setResolvedMessages(long resolvedMessages) { this.resolvedMessages = resolvedMessages; }
        public void setHighPriorityMessages(long highPriorityMessages) { this.highPriorityMessages = highPriorityMessages; }
    }
}