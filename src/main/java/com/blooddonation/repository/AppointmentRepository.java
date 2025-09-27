package com.blooddonation.repository;

import com.blooddonation.model.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    List<Appointment> findByDonorNic(String donorNic);
    List<Appointment> findBySlotId(Long slotId);
}
