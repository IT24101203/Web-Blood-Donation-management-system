package com.blooddonation.service;

import com.blooddonation.model.AppointmentSlot;
import com.blooddonation.repository.AppointmentSlotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.ArrayList;

@Service
public class AppointmentSlotServiceImpl implements AppointmentSlotService {

    @Autowired
    private AppointmentSlotRepository appointmentSlotRepository;

    @Override
    public List<AppointmentSlot> getAllSlots() {
        System.out.println("🔍 AppointmentSlotServiceImpl.getAllSlots() called");
        try {
            List<AppointmentSlot> slots = appointmentSlotRepository.findAll();
            System.out.println("📊 Repository returned " + slots.size() + " slots");
            
            // Test count query
            Long count = appointmentSlotRepository.countSlots();
            System.out.println("📈 Count query returned: " + count);
            
            return slots;
        } catch (Exception e) {
            System.out.println("❌ Error in getAllSlots(): " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public List<AppointmentSlot> getAvailableSlotsByDate(LocalDate date) {
        return appointmentSlotRepository.findAvailableSlotsByDate(date);
    }

    @Override
    public List<AppointmentSlot> getAvailableSlotsByDateAndCenter(LocalDate date, String center) {
        return appointmentSlotRepository.findAvailableSlotsByDateAndCenter(date, center);
    }

    @Override
    public List<AppointmentSlot> getSlotsByLocation(String location) {
        return appointmentSlotRepository.findByLocationOrderBySlotDateAscSlotTimeAsc(location);
    }

    @Override
    public Long countSlots() {
        return appointmentSlotRepository.countSlots();
    }

    @Override
    public boolean updateSlotAvailability(Long slotId) {
        try {
            System.out.println("🔍 Checking slot availability for slot ID: " + slotId);
            
            // Get the slot from database
            Optional<AppointmentSlot> slotOpt = appointmentSlotRepository.findById(slotId);
            if (!slotOpt.isPresent()) {
                System.out.println("❌ Slot not found: " + slotId);
                return false;
            }
            
            AppointmentSlot slot = slotOpt.get();
            System.out.println("📊 Current slot status - Max: " + slot.getMaxDonors() + ", Available: " + slot.getAvailableDonors());
            
            // Check if slot is now full
            if (slot.getAvailableDonors() <= 0) {
                System.out.println("⚠️ Slot " + slotId + " is now FULL - marking as unavailable");
                return true; // Slot is full/unavailable
            } else {
                System.out.println("✅ Slot " + slotId + " still has " + slot.getAvailableDonors() + " available spots");
                return false; // Slot still has availability
            }
            
        } catch (Exception e) {
            System.out.println("❌ Error checking slot availability: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<AppointmentSlot> getAvailableSlotsOnly() {
        try {
            System.out.println("🔍 Getting only available slots (availableDonors > 0)");
            List<AppointmentSlot> availableSlots = appointmentSlotRepository.findAvailableSlots();
            System.out.println("📊 Found " + availableSlots.size() + " available slots");
            return availableSlots;
        } catch (Exception e) {
            System.out.println("❌ Error getting available slots: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    @Override
    @Transactional
    public boolean decreaseAvailableDonors(Long slotId) {
        try {
            System.out.println("🔍 Decreasing available donors for slot ID: " + slotId);
            
            // Get the slot from database
            Optional<AppointmentSlot> slotOpt = appointmentSlotRepository.findById(slotId);
            if (!slotOpt.isPresent()) {
                System.out.println("❌ Slot not found: " + slotId);
                return false;
            }
            
            AppointmentSlot slot = slotOpt.get();
            System.out.println("📊 Before decrease - Max: " + slot.getMaxDonors() + ", Available: " + slot.getAvailableDonors());
            
            // Check if slot has available donors
            if (slot.getAvailableDonors() <= 0) {
                System.out.println("❌ Slot " + slotId + " is already full - cannot decrease further");
                return false;
            }
            
            // Decrease available donors by 1
            slot.setAvailableDonors(slot.getAvailableDonors() - 1);
            appointmentSlotRepository.save(slot);
            
            System.out.println("✅ After decrease - Max: " + slot.getMaxDonors() + ", Available: " + slot.getAvailableDonors());
            
            // Check if slot is now full
            if (slot.getAvailableDonors() <= 0) {
                System.out.println("⚠️ Slot " + slotId + " is now FULL!");
            }
            
            return true;
            
        } catch (Exception e) {
            System.out.println("❌ Error decreasing available donors: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}