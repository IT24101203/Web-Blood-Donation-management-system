package com.blooddonation.service;

import com.blooddonation.model.AppointmentSlot;
import java.time.LocalDate;
import java.util.List;

public interface AppointmentSlotService {
    List<AppointmentSlot> getAllSlots();
    List<AppointmentSlot> getAvailableSlotsByDate(LocalDate date);
    List<AppointmentSlot> getAvailableSlotsByDateAndCenter(LocalDate date, String center);
    List<AppointmentSlot> getSlotsByLocation(String location);
    Long countSlots();
}
