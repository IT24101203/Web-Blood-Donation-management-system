package com.blooddonation.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "Appointments")
public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "AppointmentID")
    private Long appointmentId;

    @Column(name = "DoONORNIC")
    private String donorNic;

    @Column(name = "SlotID")
    private Long slotId;

    @Column(name = "BookingDate", insertable = false, updatable = false)
    private LocalDateTime bookingDate;

    @Column(name = "BookingTime", insertable = false, updatable = false)
    private LocalTime bookingTime;

    // Constructors
    public Appointment() {}

    public Appointment(String donorNic, Long slotId) {
        this.donorNic = donorNic;
        this.slotId = slotId;
    }

    // Getters and Setters
    public Long getAppointmentId() { return appointmentId; }
    public void setAppointmentId(Long appointmentId) { this.appointmentId = appointmentId; }

    public String getDonorNic() { return donorNic; }
    public void setDonorNic(String donorNic) { this.donorNic = donorNic; }

    public Long getSlotId() { return slotId; }
    public void setSlotId(Long slotId) { this.slotId = slotId; }

    public LocalDateTime getBookingDate() { return bookingDate; }
    public void setBookingDate(LocalDateTime bookingDate) { this.bookingDate = bookingDate; }

    public LocalTime getBookingTime() { return bookingTime; }
    public void setBookingTime(LocalTime bookingTime) { this.bookingTime = bookingTime; }
}
