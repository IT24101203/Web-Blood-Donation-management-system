package com.blooddonation.observer;

/**
 * Observer Interface - Defines contract for observers
 * Observers get notified when subject's state changes
 * 
 * Design Pattern: Observer Pattern
 * Purpose: Allows multiple objects to be notified of state changes
 */
public interface Observer {
    /**
     * Called when subject's state changes
     * @param eventType Type of event that occurred
     * @param data Data related to the event
     */
    void update(String eventType, Object data);
    
    /**
     * Get observer name for logging
     * @return Observer name
     */
    String getObserverName();
}

