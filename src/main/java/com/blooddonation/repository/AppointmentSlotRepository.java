package com.blooddonation.repository;

import com.blooddonation.model.AppointmentSlot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AppointmentSlotRepository extends JpaRepository<AppointmentSlot, Long> {
    
    // Find available slots for a specific date
    @Query("SELECT s FROM AppointmentSlot s WHERE s.slotDate = :date AND s.availableDonors > 0 ORDER BY s.slotTime")
    List<AppointmentSlot> findAvailableSlotsByDate(@Param("date") LocalDate date);
    
    // Find available slots for a specific date and center
    @Query("SELECT s FROM AppointmentSlot s WHERE s.slotDate = :date AND s.location = :center AND s.availableDonors > 0 ORDER BY s.slotTime")
    List<AppointmentSlot> findAvailableSlotsByDateAndCenter(@Param("date") LocalDate date, @Param("center") String center);
    
    // Find slots by location
    List<AppointmentSlot> findByLocationOrderBySlotDateAscSlotTimeAsc(String location);
    
    // Simple count query to test table access
    @Query("SELECT COUNT(s) FROM AppointmentSlot s")
    Long countSlots();
    
    // Find only available slots (availableDonors > 0)
    @Query("SELECT s FROM AppointmentSlot s WHERE s.availableDonors > 0 ORDER BY s.slotDate ASC, s.slotTime ASC")
    List<AppointmentSlot> findAvailableSlots();
}
