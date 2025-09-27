package com.blooddonation.service;

import com.blooddonation.model.DonorSupportMessage;
import com.blooddonation.repository.DonorSupportMessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class DonorSupportMessageServiceImpl implements DonorSupportMessageService {

    @Autowired
    private DonorSupportMessageRepository donorSupportMessageRepository;

    @Override
    public List<DonorSupportMessage> getAllMessages() {
        try {
            return donorSupportMessageRepository.findAllByOrderByMessageDateDesc();
        } catch (Exception e) {
            System.err.println("Error getting all messages: " + e.getMessage());
            e.printStackTrace();
            return List.of();
        }
    }

    @Override
    public List<DonorSupportMessage> getMessagesByDonorNic(String donorNic) {
        try {
            return donorSupportMessageRepository.findByDonorNicOrderByMessageDateDesc(donorNic);
        } catch (Exception e) {
            System.err.println("Error getting messages by donor NIC: " + e.getMessage());
            e.printStackTrace();
            return List.of();
        }
    }

    @Override
    public List<DonorSupportMessage> getMessagesByPriority(DonorSupportMessage.Priority priority) {
        try {
            return donorSupportMessageRepository.findByPriorityOrderByMessageDateDesc(priority);
        } catch (Exception e) {
            System.err.println("Error getting messages by priority: " + e.getMessage());
            e.printStackTrace();
            return List.of();
        }
    }

    @Override
    public List<DonorSupportMessage> getPendingMessages() {
        try {
            return donorSupportMessageRepository.findByReplyTextIsNullOrderByMessageDateDesc();
        } catch (Exception e) {
            System.err.println("Error getting pending messages: " + e.getMessage());
            e.printStackTrace();
            return List.of();
        }
    }

    @Override
    public List<DonorSupportMessage> getResolvedMessages() {
        try {
            return donorSupportMessageRepository.findByReplyTextIsNotNullOrderByMessageDateDesc();
        } catch (Exception e) {
            System.err.println("Error getting resolved messages: " + e.getMessage());
            e.printStackTrace();
            return List.of();
        }
    }

    @Override
    public List<DonorSupportMessage> getHighPriorityPendingMessages() {
        try {
            return donorSupportMessageRepository.findByPriorityAndReplyTextIsNullOrderByMessageDateDesc(DonorSupportMessage.Priority.High);
        } catch (Exception e) {
            System.err.println("Error getting high priority pending messages: " + e.getMessage());
            e.printStackTrace();
            return List.of();
        }
    }

    @Override
    public Optional<DonorSupportMessage> getMessageById(Long messageId) {
        try {
            return donorSupportMessageRepository.findById(messageId);
        } catch (Exception e) {
            System.err.println("Error getting message by ID: " + e.getMessage());
            e.printStackTrace();
            return Optional.empty();
        }
    }

    @Override
    @Transactional
    public DonorSupportMessage saveMessage(DonorSupportMessage message) {
        try {
            return donorSupportMessageRepository.save(message);
        } catch (Exception e) {
            System.err.println("Error saving message: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to save message: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public DonorSupportMessage replyToMessage(Long messageId, String replyText) {
        try {
            Optional<DonorSupportMessage> messageOpt = donorSupportMessageRepository.findById(messageId);
            if (messageOpt.isPresent()) {
                DonorSupportMessage message = messageOpt.get();
                message.setReplyText(replyText);
                DonorSupportMessage updatedMessage = donorSupportMessageRepository.save(message);
                
                System.out.println("Reply added to message ID: " + messageId);
                System.out.println("Reply text: " + replyText);
                
                return updatedMessage;
            } else {
                throw new RuntimeException("Message not found with ID: " + messageId);
            }
        } catch (Exception e) {
            System.err.println("Error replying to message: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to reply to message: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public void deleteMessage(Long messageId) {
        try {
            donorSupportMessageRepository.deleteById(messageId);
            System.out.println("Message deleted with ID: " + messageId);
        } catch (Exception e) {
            System.err.println("Error deleting message: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to delete message: " + e.getMessage());
        }
    }

    @Override
    public DashboardStats getDashboardStats() {
        try {
            Object[] stats = donorSupportMessageRepository.getDashboardStats();
            if (stats != null && stats.length >= 3) {
                long pendingMessages = ((Number) stats[0]).longValue();
                long resolvedMessages = ((Number) stats[1]).longValue();
                long highPriorityMessages = ((Number) stats[2]).longValue();
                
                return new DashboardStats(pendingMessages, resolvedMessages, highPriorityMessages);
            } else {
                // Fallback to individual queries if custom query fails
                long pendingMessages = donorSupportMessageRepository.countByReplyTextIsNull();
                long resolvedMessages = donorSupportMessageRepository.countByReplyTextIsNotNull();
                long highPriorityMessages = donorSupportMessageRepository.countByPriority(DonorSupportMessage.Priority.High);
                
                return new DashboardStats(pendingMessages, resolvedMessages, highPriorityMessages);
            }
        } catch (Exception e) {
            System.err.println("Error getting dashboard stats: " + e.getMessage());
            e.printStackTrace();
            // Return default stats on error
            return new DashboardStats(0, 0, 0);
        }
    }
}

