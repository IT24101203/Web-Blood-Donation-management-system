package com.blooddonation.service;

import com.blooddonation.model.AppointmentSlot;
import com.blooddonation.repository.AppointmentSlotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

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
}