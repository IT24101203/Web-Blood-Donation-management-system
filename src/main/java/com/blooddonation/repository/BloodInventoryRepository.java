package com.blooddonation.repository;

import com.blooddonation.model.BloodInventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface BloodInventoryRepository extends JpaRepository<BloodInventory, Integer> {
    
    // Find by blood type
    List<BloodInventory> findByBloodType(String bloodType);
    
    // Find by status
    List<BloodInventory> findByStatus(String status);
    
    // Find available blood by type
    List<BloodInventory> findByBloodTypeAndStatus(String bloodType, String status);
    
    // Find expired blood
    @Query("SELECT b FROM BloodInventory b WHERE b.expiryDate < :currentDate")
    List<BloodInventory> findExpiredBlood(LocalDate currentDate);
    
    // Find blood expiring soon (within next 7 days)
    @Query("SELECT b FROM BloodInventory b WHERE b.expiryDate BETWEEN :currentDate AND :futureDate")
    List<BloodInventory> findBloodExpiringSoon(LocalDate currentDate, LocalDate futureDate);
    
    // Get total quantity by blood type and status
    @Query("SELECT SUM(b.quantity) FROM BloodInventory b WHERE b.bloodType = :bloodType AND b.status = :status")
    Integer getTotalQuantityByBloodTypeAndStatus(String bloodType, String status);
    
    // Get all available blood types with quantities
    @Query("SELECT b.bloodType, SUM(b.quantity) FROM BloodInventory b WHERE b.status = 'Available' GROUP BY b.bloodType")
    List<Object[]> getAvailableBloodTypesWithQuantities();
}
