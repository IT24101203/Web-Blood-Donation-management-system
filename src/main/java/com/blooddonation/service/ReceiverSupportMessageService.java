package com.blooddonation.service;

import com.blooddonation.model.ReceiverSupportMessage;
import java.util.List;
import java.util.Optional;

public interface ReceiverSupportMessageService {
    
    /**
     * Get all messages ordered by message date (newest first)
     */
    List<ReceiverSupportMessage> getAllMessages();
    
    /**
     * Get messages by receiver NIC
     */
    List<ReceiverSupportMessage> getMessagesByReceiverNic(String receiverNic);
    
    /**
     * Get messages by priority
     */
    List<ReceiverSupportMessage> getMessagesByPriority(ReceiverSupportMessage.Priority priority);
    
    /**
     * Get pending messages (without replies)
     */
    List<ReceiverSupportMessage> getPendingMessages();
    
    /**
     * Get resolved messages (with replies)
     */
    List<ReceiverSupportMessage> getResolvedMessages();
    
    /**
     * Get high priority pending messages
     */
    List<ReceiverSupportMessage> getHighPriorityPendingMessages();
    
    /**
     * Get message by ID
     */
    Optional<ReceiverSupportMessage> getMessageById(Long messageId);
    
    /**
     * Save a new message
     */
    ReceiverSupportMessage saveMessage(ReceiverSupportMessage message);
    
    /**
     * Update message with reply
     */
    ReceiverSupportMessage replyToMessage(Long messageId, String replyText);
    
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
        private long highPriorityPending;
        private long totalMessages;
        
        public DashboardStats(long pendingMessages, long resolvedMessages, long highPriorityPending, long totalMessages) {
            this.pendingMessages = pendingMessages;
            this.resolvedMessages = resolvedMessages;
            this.highPriorityPending = highPriorityPending;
            this.totalMessages = totalMessages;
        }
        
        public long getPendingMessages() {
            return pendingMessages;
        }
        
        public long getResolvedMessages() {
            return resolvedMessages;
        }
        
        public long getHighPriorityPending() {
            return highPriorityPending;
        }
        
        public long getTotalMessages() {
            return totalMessages;
        }
    }
}
