package com.blooddonation.service;

import com.blooddonation.model.BloodInventory;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface BloodInventoryService {
    
    // Basic CRUD operations
    BloodInventory saveBloodInventory(BloodInventory bloodInventory);
    List<BloodInventory> findAll();
    Optional<BloodInventory> findById(Integer id);
    void deleteById(Integer id);
    
    // Find by criteria
    List<BloodInventory> findByBloodType(String bloodType);
    List<BloodInventory> findByStatus(String status);
    List<BloodInventory> findByBloodTypeAndStatus(String bloodType, String status);
    
    // Special queries
    List<BloodInventory> findExpiredBlood();
    List<BloodInventory> findBloodExpiringSoon();
    Integer getTotalQuantityByBloodTypeAndStatus(String bloodType, String status);
    Map<String, Integer> getAvailableBloodTypesWithQuantities();
    
    // Business logic methods
    boolean isBloodAvailable(String bloodType, Integer requiredQuantity);
    BloodInventory updateBloodStatus(Integer inventoryId, String newStatus);
    List<BloodInventory> getBloodInventorySummary();
}
