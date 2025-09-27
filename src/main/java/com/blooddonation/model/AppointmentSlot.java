package com.blooddonation.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "Appointment_Slots")
public class AppointmentSlot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SlotID")
    private Long slotId;

    @Column(name = "SlotDate")
    private LocalDate slotDate;

    @Column(name = "SlotTime")
    private String slotTime;

    @Column(name = "Location")
    private String location;

    @Column(name = "MaxDonors")
    private Integer maxDonors;

    @Column(name = "AvailableDonors")
    private Integer availableDonors;

    // Constructors
    public AppointmentSlot() {}

    public AppointmentSlot(LocalDate slotDate, String slotTime, String location, 
                          Integer maxDonors, Integer availableDonors) {
        this.slotDate = slotDate;
        this.slotTime = slotTime;
        this.location = location;
        this.maxDonors = maxDonors;
        this.availableDonors = availableDonors;
    }


    // Getters and Setters
    public Long getSlotId() { return slotId; }
    public void setSlotId(Long slotId) { this.slotId = slotId; }

    public LocalDate getSlotDate() { return slotDate; }
    public void setSlotDate(LocalDate slotDate) { this.slotDate = slotDate; }

    public String getSlotTime() { return slotTime; }
    public void setSlotTime(String slotTime) { this.slotTime = slotTime; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public Integer getMaxDonors() { return maxDonors; }
    public void setMaxDonors(Integer maxDonors) { this.maxDonors = maxDonors; }

    public Integer getAvailableDonors() { return availableDonors; }
    public void setAvailableDonors(Integer availableDonors) { this.availableDonors = availableDonors; }

    // Helper methods for compatibility with template
    public LocalDate getDate() { return slotDate; }
    public String getDonationCenter() { return location; }
    public Integer getMaxCapacity() { return maxDonors; }
    public Integer getCurrentBookings() { return maxDonors - availableDonors; }
    public Integer getAvailableSlots() { return availableDonors; }
    public boolean isAvailable() { return availableDonors > 0; }
}
