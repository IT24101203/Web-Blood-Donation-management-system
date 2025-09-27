package com.blooddonation.service;

import com.blooddonation.model.BloodInventory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ScheduledCleanupService {

    @Autowired
    private BloodInventoryService bloodInventoryService;

    // Run cleanup every day at 2:00 AM
    @Scheduled(cron = "0 0 2 * * ?")
    public void cleanupExpiredBloodInventory() {
        try {
            System.out.println("🕐 Starting scheduled cleanup of expired blood inventory at " + LocalDate.now());
            
            List<BloodInventory> expiredRecords = bloodInventoryService.findExpiredBlood();
            int deletedCount = 0;
            
            if (expiredRecords.isEmpty()) {
                System.out.println("✅ No expired blood inventory records found. Cleanup completed.");
                return;
            }
            
            for (BloodInventory expiredRecord : expiredRecords) {
                try {
                    bloodInventoryService.deleteById(expiredRecord.getInventoryId());
                    deletedCount++;
                    System.out.println("🗑️ Automatically deleted expired record ID: " + expiredRecord.getInventoryId() + 
                                     " (Blood Type: " + expiredRecord.getBloodType() + 
                                     ", Expiry Date: " + expiredRecord.getExpiryDate() + ")");
                } catch (Exception e) {
                    System.err.println("❌ Error deleting expired record ID " + expiredRecord.getInventoryId() + ": " + e.getMessage());
                }
            }
            
            System.out.println("✅ Scheduled cleanup completed. Deleted " + deletedCount + " expired records.");
            
        } catch (Exception e) {
            System.err.println("❌ Error during scheduled cleanup: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Run cleanup check every hour to show expiring blood
    @Scheduled(cron = "0 0 * * * ?")
    public void checkExpiringBlood() {
        try {
            List<BloodInventory> expiringSoon = bloodInventoryService.findBloodExpiringSoon();
            
            if (!expiringSoon.isEmpty()) {
                System.out.println("⚠️ Found " + expiringSoon.size() + " blood units expiring within 7 days:");
                for (BloodInventory record : expiringSoon) {
                    System.out.println("   - ID: " + record.getInventoryId() + 
                                     ", Type: " + record.getBloodType() + 
                                     ", Expiry: " + record.getExpiryDate());
                }
            }
        } catch (Exception e) {
            System.err.println("❌ Error checking expiring blood: " + e.getMessage());
        }
    }
}
