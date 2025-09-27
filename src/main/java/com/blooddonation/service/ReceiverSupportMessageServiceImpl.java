package com.blooddonation.service;

import com.blooddonation.model.ReceiverSupportMessage;
import com.blooddonation.repository.ReceiverSupportMessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class ReceiverSupportMessageServiceImpl implements ReceiverSupportMessageService {

    @Autowired
    private ReceiverSupportMessageRepository receiverSupportMessageRepository;

    @Override
    public List<ReceiverSupportMessage> getAllMessages() {
        try {
            return receiverSupportMessageRepository.findAllByOrderByMessageDateDesc();
        } catch (Exception e) {
            System.err.println("Error getting all receiver support messages: " + e.getMessage());
            e.printStackTrace();
            return List.of();
        }
    }

    @Override
    public List<ReceiverSupportMessage> getMessagesByReceiverNic(String receiverNic) {
        try {
            return receiverSupportMessageRepository.findByReceiverNicOrderByMessageDateDesc(receiverNic);
        } catch (Exception e) {
            System.err.println("Error getting receiver support messages by NIC: " + e.getMessage());
            e.printStackTrace();
            return List.of();
        }
    }

    @Override
    public List<ReceiverSupportMessage> getMessagesByPriority(ReceiverSupportMessage.Priority priority) {
        try {
            return receiverSupportMessageRepository.findByPriorityOrderByMessageDateDesc(priority);
        } catch (Exception e) {
            System.err.println("Error getting receiver support messages by priority: " + e.getMessage());
            e.printStackTrace();
            return List.of();
        }
    }

    @Override
    public List<ReceiverSupportMessage> getPendingMessages() {
        try {
            return receiverSupportMessageRepository.findByReplyTextIsNullOrderByMessageDateDesc();
        } catch (Exception e) {
            System.err.println("Error getting pending receiver support messages: " + e.getMessage());
            e.printStackTrace();
            return List.of();
        }
    }

    @Override
    public List<ReceiverSupportMessage> getResolvedMessages() {
        try {
            return receiverSupportMessageRepository.findByReplyTextIsNotNullOrderByMessageDateDesc();
        } catch (Exception e) {
            System.err.println("Error getting resolved receiver support messages: " + e.getMessage());
            e.printStackTrace();
            return List.of();
        }
    }

    @Override
    public List<ReceiverSupportMessage> getHighPriorityPendingMessages() {
        try {
            return receiverSupportMessageRepository.findByPriorityAndReplyTextIsNullOrderByMessageDateDesc(ReceiverSupportMessage.Priority.High);
        } catch (Exception e) {
            System.err.println("Error getting high priority pending receiver support messages: " + e.getMessage());
            e.printStackTrace();
            return List.of();
        }
    }

    @Override
    public Optional<ReceiverSupportMessage> getMessageById(Long messageId) {
        try {
            return receiverSupportMessageRepository.findById(messageId);
        } catch (Exception e) {
            System.err.println("Error getting receiver support message by ID: " + e.getMessage());
            e.printStackTrace();
            return Optional.empty();
        }
    }

    @Override
    @Transactional
    public ReceiverSupportMessage saveMessage(ReceiverSupportMessage message) {
        try {
            if (message.getMessageDate() == null) {
                message.setMessageDate(LocalDateTime.now());
            }
            return receiverSupportMessageRepository.save(message);
        } catch (Exception e) {
            System.err.println("Error saving receiver support message: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to save receiver support message", e);
        }
    }

    @Override
    @Transactional
    public ReceiverSupportMessage replyToMessage(Long messageId, String replyText) {
        try {
            Optional<ReceiverSupportMessage> messageOpt = receiverSupportMessageRepository.findById(messageId);
            if (messageOpt.isPresent()) {
                ReceiverSupportMessage message = messageOpt.get();
                message.setReplyText(replyText);
                message.setReplyDate(LocalDateTime.now());
                return receiverSupportMessageRepository.save(message);
            } else {
                throw new RuntimeException("Receiver support message not found with ID: " + messageId);
            }
        } catch (Exception e) {
            System.err.println("Error replying to receiver support message: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to reply to receiver support message", e);
        }
    }

    @Override
    @Transactional
    public void deleteMessage(Long messageId) {
        try {
            receiverSupportMessageRepository.deleteById(messageId);
        } catch (Exception e) {
            System.err.println("Error deleting receiver support message: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to delete receiver support message", e);
        }
    }

    @Override
    public DashboardStats getDashboardStats() {
        try {
            Object[] stats = receiverSupportMessageRepository.getDashboardStats();
            long pendingMessages = stats[0] != null ? ((Number) stats[0]).longValue() : 0;
            long resolvedMessages = stats[1] != null ? ((Number) stats[1]).longValue() : 0;
            long highPriorityPending = stats[2] != null ? ((Number) stats[2]).longValue() : 0;
            long totalMessages = pendingMessages + resolvedMessages;
            
            return new DashboardStats(pendingMessages, resolvedMessages, highPriorityPending, totalMessages);
        } catch (Exception e) {
            System.err.println("Error getting receiver support dashboard stats: " + e.getMessage());
            e.printStackTrace();
            return new DashboardStats(0, 0, 0, 0);
        }
    }
}
