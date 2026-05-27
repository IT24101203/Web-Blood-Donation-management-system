package com.blooddonation.observer;

import com.blooddonation.model.BloodRequest;

/**
 * StatisticsObserver - Updates system statistics
 * Tracks blood request statistics for reporting and analytics
 * 
 * Design Pattern: Observer Pattern (Concrete Observer)
 * Purpose: Automatically updates statistics when events occur
 */
public class StatisticsObserver implements Observer {
    
    // Simple in-memory counters for demonstration
    // In production, these would be stored in database
    private static int totalRequests = 0;
    private static int approvedRequests = 0;
    private static int completedRequests = 0;
    private static int rejectedRequests = 0;
    
    @Override
    public void update(String eventType, Object data) {
        if (data instanceof BloodRequest) {
            BloodRequest request = (BloodRequest) data;
            updateStatistics(request, eventType);
            System.out.println("✅ [StatisticsObserver] Statistics updated");
        }
    }
    
    @Override
    public String getObserverName() {
        return "StatisticsObserver";
    }
    
    /**
     * Update statistics based on blood request status and event
     */
    private void updateStatistics(BloodRequest request, String eventType) {
        switch (eventType) {
            case "BLOOD_REQUEST_CREATED":
                totalRequests++;
                logStatistic("Total Requests", totalRequests);
                break;
                
            case "BLOOD_REQUEST_APPROVED":
                approvedRequests++;
                logStatistic("Approved Requests", approvedRequests);
                break;
                
            case "BLOOD_REQUEST_COMPLETED":
                completedRequests++;
                logStatistic("Completed Requests", completedRequests);
                break;
                
            case "BLOOD_REQUEST_REJECTED":
                rejectedRequests++;
                logStatistic("Rejected Requests", rejectedRequests);
                break;
        }
        
        // Display current statistics summary
        displayStatisticsSummary();
    }
    
    /**
     * Log individual statistic update
     */
    private void logStatistic(String statisticName, int value) {
        System.out.println("📈 " + statisticName + ": " + value);
    }
    
    /**
     * Display complete statistics summary
     */
    private void displayStatisticsSummary() {
        System.out.println("📊 Current Blood Request Statistics:");
        System.out.println("   ├─ Total Requests: " + totalRequests);
        System.out.println("   ├─ Approved: " + approvedRequests);
        System.out.println("   ├─ Completed: " + completedRequests);
        System.out.println("   ├─ Rejected: " + rejectedRequests);
        System.out.println("   └─ Pending: " + (totalRequests - approvedRequests - completedRequests - rejectedRequests));
    }
    
    /**
     * Get current statistics summary as string
     * Can be called from dashboard controllers
     */
    public static String getStatisticsSummary() {
        return String.format("Total: %d | Approved: %d | Completed: %d | Rejected: %d", 
                           totalRequests, approvedRequests, completedRequests, rejectedRequests);
    }
    
    /**
     * Reset statistics (for testing purposes)
     */
    public static void resetStatistics() {
        totalRequests = 0;
        approvedRequests = 0;
        completedRequests = 0;
        rejectedRequests = 0;
        System.out.println("🔄 Statistics reset");
    }
}


