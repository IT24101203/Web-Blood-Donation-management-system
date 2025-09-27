package com.blooddonation.repository;

import com.blooddonation.model.DonorSupportMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DonorSupportMessageRepository extends JpaRepository<DonorSupportMessage, Long> {
    
    /**
     * Find all messages ordered by message date (newest first)
     */
    List<DonorSupportMessage> findAllByOrderByMessageDateDesc();
    
    /**
     * Find messages by donor NIC
     */
    List<DonorSupportMessage> findByDonorNicOrderByMessageDateDesc(String donorNic);
    
    /**
     * Find messages by priority
     */
    List<DonorSupportMessage> findByPriorityOrderByMessageDateDesc(DonorSupportMessage.Priority priority);
    
    /**
     * Find messages that don't have replies yet
     */
    List<DonorSupportMessage> findByReplyTextIsNullOrderByMessageDateDesc();
    
    /**
     * Find messages that have replies
     */
    List<DonorSupportMessage> findByReplyTextIsNotNullOrderByMessageDateDesc();
    
    /**
     * Find messages by priority that don't have replies
     */
    List<DonorSupportMessage> findByPriorityAndReplyTextIsNullOrderByMessageDateDesc(DonorSupportMessage.Priority priority);
    
    /**
     * Count messages by priority
     */
    long countByPriority(DonorSupportMessage.Priority priority);
    
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
           "SUM(CASE WHEN m.priority = 'High' AND m.replyText IS NULL THEN 1 ELSE 0 END) as highPriorityMessages " +
           "FROM DonorSupportMessage m")
    Object[] getDashboardStats();
}