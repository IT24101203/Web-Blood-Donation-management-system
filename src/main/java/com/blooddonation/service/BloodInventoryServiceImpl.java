package com.blooddonation.service;

import com.blooddonation.model.BloodInventory;
import com.blooddonation.repository.BloodInventoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Transactional
public class BloodInventoryServiceImpl implements BloodInventoryService {

    @Autowired
    private BloodInventoryRepository bloodInventoryRepository;

    @Override
    public BloodInventory saveBloodInventory(BloodInventory bloodInventory) {
        return bloodInventoryRepository.save(bloodInventory);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BloodInventory> findAll() {
        return bloodInventoryRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<BloodInventory> findById(Integer id) {
        return bloodInventoryRepository.findById(id);
    }

    @Override
    public void deleteById(Integer id) {
        bloodInventoryRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BloodInventory> findByBloodType(String bloodType) {
        return bloodInventoryRepository.findByBloodType(bloodType);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BloodInventory> findByStatus(String status) {
        return bloodInventoryRepository.findByStatus(status);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BloodInventory> findByBloodTypeAndStatus(String bloodType, String status) {
        return bloodInventoryRepository.findByBloodTypeAndStatus(bloodType, status);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BloodInventory> findExpiredBlood() {
        return bloodInventoryRepository.findExpiredBlood(LocalDate.now());
    }

    @Override
    @Transactional(readOnly = true)
    public List<BloodInventory> findBloodExpiringSoon() {
        LocalDate currentDate = LocalDate.now();
        LocalDate futureDate = currentDate.plusDays(7);
        return bloodInventoryRepository.findBloodExpiringSoon(currentDate, futureDate);
    }

    @Override
    @Transactional(readOnly = true)
    public Integer getTotalQuantityByBloodTypeAndStatus(String bloodType, String status) {
        Integer total = bloodInventoryRepository.getTotalQuantityByBloodTypeAndStatus(bloodType, status);
        return total != null ? total : 0;
    }

    @Override
    @Transactional(readOnly = true)
    public Map<String, Integer> getAvailableBloodTypesWithQuantities() {
        List<Object[]> results = bloodInventoryRepository.getAvailableBloodTypesWithQuantities();
        Map<String, Integer> bloodTypes = new HashMap<>();
        
        for (Object[] result : results) {
            String bloodType = (String) result[0];
            Integer quantity = ((Number) result[1]).intValue();
            bloodTypes.put(bloodType, quantity);
        }
        
        return bloodTypes;
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isBloodAvailable(String bloodType, Integer requiredQuantity) {
        Integer availableQuantity = getTotalQuantityByBloodTypeAndStatus(bloodType, "Available");
        return availableQuantity >= requiredQuantity;
    }

    @Override
    public BloodInventory updateBloodStatus(Integer inventoryId, String newStatus) {
        Optional<BloodInventory> optional = findById(inventoryId);
        if (optional.isPresent()) {
            BloodInventory bloodInventory = optional.get();
            bloodInventory.setStatus(newStatus);
            return saveBloodInventory(bloodInventory);
        }
        return null;
    }

    @Override
    @Transactional(readOnly = true)
    public List<BloodInventory> getBloodInventorySummary() {
        return findAll();
    }
}
