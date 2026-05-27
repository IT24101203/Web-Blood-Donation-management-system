package com.blooddonation.observer;

import com.blooddonation.model.BloodRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * BloodRequestObserverManager - Manages observers for blood requests
 * This class handles attaching observers and notifying them of changes
 * 
 * Design Pattern: Observer Pattern (Subject/Context)
 * Purpose: Manages the relationship between blood requests and their observers
 */
public class BloodRequestObserverManager implements Subject {
    
    private List<Observer> observers = new ArrayList<>();
    private BloodRequest bloodRequest;
    
    /**
     * Constructor - Creates manager for a blood request
     * Automatically attaches default observers
     * @param bloodRequest The blood request to observe
     */
    public BloodRequestObserverManager(BloodRequest bloodRequest) {
        this.bloodRequest = bloodRequest;
        attachDefaultObservers();
    }
    
    /**
     * Attach default observers automatically
     * These observers are notified for all blood request events
     */
    private void attachDefaultObservers() {
        System.out.println("🔔 Setting up Observer Pattern for Blood Request #" + bloodRequest.getRequestId());
        attach(new ReceiverNotificationObserver());
        attach(new AdminDashboardObserver());
        attach(new StatisticsObserver());
        System.out.println("✅ All observers attached and ready");
    }
    
    @Override
    public void attach(Observer observer) {
        if (!observers.contains(observer)) {
            observers.add(observer);
            System.out.println("   ➕ Observer attached: " + observer.getObserverName());
        }
    }
    
    @Override
    public void detach(Observer observer) {
        if (observers.remove(observer)) {
            System.out.println("   ➖ Observer detached: " + observer.getObserverName());
        }
    }
    
    @Override
    public void notifyObservers(String eventType) {
        System.out.println("\n🔔 ========================================");
        System.out.println("🔔 OBSERVER PATTERN: Notifying " + observers.size() + " observers");
        System.out.println("🔔 Event: " + eventType);
        System.out.println("🔔 Blood Request ID: #" + bloodRequest.getRequestId());
        System.out.println("🔔 ========================================\n");
        
        for (Observer observer : observers) {
            try {
                observer.update(eventType, bloodRequest);
            } catch (Exception e) {
                System.err.println("❌ Error notifying observer " + observer.getObserverName() + ": " + e.getMessage());
                e.printStackTrace();
            }
        }
        
        System.out.println("\n✅ All observers notified successfully\n");
    }
    
    /**
     * Notify when blood request is approved
     * Called after hospital staff approves a request
     */
    public void notifyApproved() {
        notifyObservers("BLOOD_REQUEST_APPROVED");
    }
    
    /**
     * Notify when blood request is completed
     * Called after hospital staff completes a request
     */
    public void notifyCompleted() {
        notifyObservers("BLOOD_REQUEST_COMPLETED");
    }
    
    /**
     * Notify when blood request is rejected
     * Called after hospital staff rejects a request
     */
    public void notifyRejected() {
        notifyObservers("BLOOD_REQUEST_REJECTED");
    }
    
    /**
     * Notify when blood request is created
     * Called when receiver creates a new request
     */
    public void notifyCreated() {
        notifyObservers("BLOOD_REQUEST_CREATED");
    }
    
    /**
     * Get number of attached observers
     * @return Number of observers
     */
    public int getObserverCount() {
        return observers.size();
    }
}


