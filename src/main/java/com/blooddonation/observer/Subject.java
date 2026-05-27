package com.blooddonation.observer;

/**
 * Subject Interface - Defines contract for observable objects
 * Subjects maintain list of observers and notify them of changes
 * 
 * Design Pattern: Observer Pattern
 * Purpose: Defines how subjects manage and notify observers
 */
public interface Subject {
    /**
     * Attach an observer to receive notifications
     * @param observer Observer to attach
     */
    void attach(Observer observer);
    
    /**
     * Detach an observer from notifications
     * @param observer Observer to detach
     */
    void detach(Observer observer);
    
    /**
     * Notify all attached observers
     * @param eventType Type of event that occurred
     */
    void notifyObservers(String eventType);
}


