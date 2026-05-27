package com.blooddonation.observer;

import com.blooddonation.model.BloodRequest;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * AdminDashboardObserver - Updates admin dashboard
 * Updates admin dashboard statistics when blood request status changes
 * 
 * Design Pattern: Observer Pattern (Concrete Observer)
 * Purpose: Automatically updates admin dashboard when events occur
 */
public class AdminDashboardObserver implements Observer {
    
    @Override
    public void update(String eventType, Object data) {
        if (data instanceof BloodRequest) {
            BloodRequest request = (BloodRequest) data;
            updateAdminDashboard(request, eventType);
            
            // Store notification for admin to see in dashboard
            storeAdminNotification(request, eventType);
            
            System.out.println("✅ [AdminDashboardObserver] Admin dashboard updated for request #" + request.getRequestId());
        }
    }
    
    @Override
    public String getObserverName() {
        return "AdminDashboardObserver";
    }
    
    /**
     * Update admin dashboard with blood request information
     */
    private void updateAdminDashboard(BloodRequest request, String eventType) {
        System.out.println("📊 Admin Dashboard Update:");
        System.out.println("   ├─ Event: " + eventType);
        System.out.println("   ├─ Request ID: #" + request.getRequestId());
        System.out.println("   ├─ Receiver CNIC: " + request.getReceiverCnic());
        System.out.println("   ├─ Blood Type: " + request.getBloodTypeNeeded());
        System.out.println("   ├─ Quantity: " + request.getQuantity() + " units");
        System.out.println("   ├─ Hospital: " + request.getHospital());
        System.out.println("   ├─ Priority: " + request.getPriority());
        System.out.println("   └─ Status: " + getStatus(request));
    }
    
    /**
     * Store notification for admin dashboard
     * Uses NotificationManager singleton to persist notifications
     */
    private void storeAdminNotification(BloodRequest request, String eventType) {
        Map<String, Object> notification = new HashMap<>();
        
        // Build notification message
        String message = buildNotificationMessage(request, eventType);
        String icon = getEventIcon(eventType);
        String type = getEventType(eventType);
        
        notification.put("message", message);
        notification.put("icon", icon);
        notification.put("type", type);
        notification.put("requestId", request.getRequestId());
        notification.put("bloodType", request.getBloodTypeNeeded());
        notification.put("quantity", request.getQuantity());
        notification.put("hospital", request.getHospital());
        notification.put("priority", request.getPriority());
        notification.put("receiverCnic", request.getReceiverCnic());
        notification.put("timestamp", LocalDateTime.now().toString());
        notification.put("read", false);
        
        // Store in NotificationManager (Singleton)
        NotificationManager.getInstance().addAdminNotification(notification);
        
        System.out.println("💾 Notification stored for admin dashboard");
    }
    
    /**
     * Build notification message based on event type
     */
    private String buildNotificationMessage(BloodRequest request, String eventType) {
        switch (eventType) {
            case "BLOOD_REQUEST_APPROVED":
                return String.format("Blood Request #%d has been approved - %s (%d units) at %s",
                        request.getRequestId(), request.getBloodTypeNeeded(), 
                        request.getQuantity(), request.getHospital());
                
            case "BLOOD_REQUEST_COMPLETED":
                return String.format("Blood Request #%d has been completed - %s (%d units) ready for collection",
                        request.getRequestId(), request.getBloodTypeNeeded(), request.getQuantity());
                
            case "BLOOD_REQUEST_REJECTED":
                return String.format("Blood Request #%d has been rejected - %s (%d units)",
                        request.getRequestId(), request.getBloodTypeNeeded(), request.getQuantity());
                
            case "BLOOD_REQUEST_CREATED":
                return String.format("New Blood Request #%d received - %s (%d units) Priority: %s",
                        request.getRequestId(), request.getBloodTypeNeeded(), 
                        request.getQuantity(), request.getPriority());
                
            default:
                return String.format("Blood Request #%d status updated", request.getRequestId());
        }
    }
    
    /**
     * Get icon for event type
     */
    private String getEventIcon(String eventType) {
        switch (eventType) {
            case "BLOOD_REQUEST_APPROVED":
                return "✅";
            case "BLOOD_REQUEST_COMPLETED":
                return "🎉";
            case "BLOOD_REQUEST_REJECTED":
                return "❌";
            case "BLOOD_REQUEST_CREATED":
                return "📋";
            default:
                return "📢";
        }
    }
    
    /**
     * Get CSS type for event
     */
    private String getEventType(String eventType) {
        switch (eventType) {
            case "BLOOD_REQUEST_APPROVED":
                return "success";
            case "BLOOD_REQUEST_COMPLETED":
                return "success";
            case "BLOOD_REQUEST_REJECTED":
                return "danger";
            case "BLOOD_REQUEST_CREATED":
                return "info";
            default:
                return "info";
        }
    }
    
    /**
     * Get current status of blood request based on dates
     */
    private String getStatus(BloodRequest request) {
        if (request.getCompletionDate() != null) {
            return "COMPLETED (" + request.getCompletionDate() + ")";
        } else if (request.getApprovedDate() != null) {
            return "APPROVED (" + request.getApprovedDate() + ")";
        } else {
            return "PENDING";
        }
    }
}

