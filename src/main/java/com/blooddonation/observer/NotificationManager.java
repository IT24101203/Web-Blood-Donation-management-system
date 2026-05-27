package com.blooddonation.observer;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * NotificationManager - Singleton pattern for managing notifications
 * Stores admin notifications from Observer Pattern
 * 
 * Design Pattern: Singleton Pattern
 * Purpose: Single point of access for all notification storage
 */
public class NotificationManager {
    
    // Singleton instance
    private static NotificationManager instance;
    
    // Storage for admin notifications
    private Map<String, List<Map<String, Object>>> adminNotifications;
    
    // Private constructor (Singleton pattern)
    private NotificationManager() {
        this.adminNotifications = new ConcurrentHashMap<>();
    }
    
    /**
     * Get singleton instance
     * @return NotificationManager instance
     */
    public static synchronized NotificationManager getInstance() {
        if (instance == null) {
            instance = new NotificationManager();
            System.out.println("🔔 NotificationManager initialized (Singleton)");
        }
        return instance;
    }
    
    /**
     * Add admin notification
     * @param notification Notification details
     */
    public void addAdminNotification(Map<String, Object> notification) {
        String key = "admin"; // Store all admin notifications under "admin" key
        
        adminNotifications.computeIfAbsent(key, k -> new ArrayList<>()).add(notification);
        
        // Keep only last 50 notifications
        List<Map<String, Object>> notifications = adminNotifications.get(key);
        if (notifications.size() > 50) {
            notifications.remove(0);
        }
        
        System.out.println("✅ Admin notification stored in NotificationManager");
    }
    
    /**
     * Get all admin notifications
     * @return List of admin notifications
     */
    public List<Map<String, Object>> getAdminNotifications() {
        return adminNotifications.getOrDefault("admin", new ArrayList<>());
    }
    
    /**
     * Clear all admin notifications
     */
    public void clearAdminNotifications() {
        adminNotifications.clear();
        System.out.println("🗑️ Admin notifications cleared");
    }
    
    /**
     * Get notification count
     * @return Number of admin notifications
     */
    public int getNotificationCount() {
        return adminNotifications.getOrDefault("admin", new ArrayList<>()).size();
    }
    
    /**
     * Get unread notification count
     * @return Number of unread admin notifications
     */
    public int getUnreadCount() {
        List<Map<String, Object>> notifications = getAdminNotifications();
        return (int) notifications.stream()
                .filter(n -> !Boolean.TRUE.equals(n.get("read")))
                .count();
    }
    
    /**
     * Mark all notifications as read
     */
    public void markAllAsRead() {
        List<Map<String, Object>> notifications = getAdminNotifications();
        notifications.forEach(n -> n.put("read", true));
        System.out.println("📖 All admin notifications marked as read");
    }
}


