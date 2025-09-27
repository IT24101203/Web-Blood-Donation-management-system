package com.blooddonation.repository;

import com.blooddonation.model.ReceiverSupportMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReceiverSupportMessageRepository extends JpaRepository<ReceiverSupportMessage, Long> {
    
    /**
     * Find all messages ordered by message date (newest first)
     */
    List<ReceiverSupportMessage> findAllByOrderByMessageDateDesc();
    
    /**
     * Find messages by receiver CNIC
     */
    List<ReceiverSupportMessage> findByReceiverNicOrderByMessageDateDesc(String receiverNic);
    
    /**
     * Find messages by priority
     */
    List<ReceiverSupportMessage> findByPriorityOrderByMessageDateDesc(ReceiverSupportMessage.Priority priority);
    
    /**
     * Find messages that don't have replies yet
     */
    List<ReceiverSupportMessage> findByReplyTextIsNullOrderByMessageDateDesc();
    
    /**
     * Find messages that have replies
     */
    List<ReceiverSupportMessage> findByReplyTextIsNotNullOrderByMessageDateDesc();
    
    /**
     * Find messages by priority that don't have replies
     */
    List<ReceiverSupportMessage> findByPriorityAndReplyTextIsNullOrderByMessageDateDesc(ReceiverSupportMessage.Priority priority);
    
    /**
     * Count messages by priority
     */
    long countByPriority(ReceiverSupportMessage.Priority priority);
    
    /**
     * Count messages without replies
     */
    long countByReplyTextIsNull();
    
    /**
     * Count messages with replies
     */
    long countByReplyTextIsNotNull();
    
    /**
     * Find messages with custom query for dashboard stats
     */
    @Query("SELECT " +
           "SUM(CASE WHEN m.replyText IS NULL THEN 1 ELSE 0 END) as pendingMessages, " +
           "SUM(CASE WHEN m.replyText IS NOT NULL THEN 1 ELSE 0 END) as resolvedMessages, " +
           "SUM(CASE WHEN m.priority = 'HIGH' AND m.replyText IS NULL THEN 1 ELSE 0 END) as highPriorityPending " +
           "FROM ReceiverSupportMessage m")
    Object[] getDashboardStats();
}
