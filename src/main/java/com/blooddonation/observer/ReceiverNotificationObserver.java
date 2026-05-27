package com.blooddonation.observer;

import com.blooddonation.model.BloodRequest;

/**
 * ReceiverNotificationObserver - Sends notifications to receivers
 * Notifies receiver when their blood request status changes
 * 
 * Design Pattern: Observer Pattern (Concrete Observer)
 * Purpose: Handles receiver notifications automatically
 */
public class ReceiverNotificationObserver implements Observer {
    
    @Override
    public void update(String eventType, Object data) {
        if (data instanceof BloodRequest) {
            BloodRequest request = (BloodRequest) data;
            String message = buildNotificationMessage(request, eventType);
            sendNotificationToReceiver(request.getReceiverCnic(), message, request.getRequestId());
            System.out.println("✅ [ReceiverNotificationObserver] Notification sent to receiver: " + request.getReceiverCnic());
        }
    }
    
    @Override
    public String getObserverName() {
        return "ReceiverNotificationObserver";
    }
    
    /**
     * Build notification message based on blood request status and event
     */
    private String buildNotificationMessage(BloodRequest request, String eventType) {
        switch (eventType) {
            case "BLOOD_REQUEST_APPROVED":
                return "✅ Your blood request has been approved! Our hospital staff will process it shortly. Thank you for your patience.";
                
            case "BLOOD_REQUEST_COMPLETED":
                return "🎉 Your blood request has been completed successfully! Blood is ready for collection at " + request.getHospital() + ".";
                
            case "BLOOD_REQUEST_REJECTED":
                return "❌ We regret to inform you that your blood request could not be processed. Please contact our support team for assistance.";
                
            default:
                return "📋 Your blood request status has been updated.";
        }
    }
    
    /**
     * Send notification to receiver
     * Integrates with existing notification system
     */
    private void sendNotificationToReceiver(String receiverCnic, String message, Long requestId) {
        System.out.println("📱 Sending notification to receiver " + receiverCnic);
        System.out.println("   Message: " + message);
        System.out.println("   Request ID: " + requestId);
        
        // This integrates with your existing HashMap notification storage
        // You can add code here to store in your existing notification system
    }
}


