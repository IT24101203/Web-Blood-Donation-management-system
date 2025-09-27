package com.blooddonation.controller;

import com.blooddonation.model.Appointment;
import com.blooddonation.model.AppointmentSlot;
import com.blooddonation.model.Donor;
import com.blooddonation.repository.AppointmentRepository;
import com.blooddonation.service.AppointmentSlotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.http.ResponseEntity;
import java.util.HashMap;
import java.util.Map;

import jakarta.servlet.http.HttpSession;
import java.util.List;

@Controller
public class AppointmentController {

    private final AppointmentSlotService appointmentSlotService;
    private final AppointmentRepository appointmentRepository;

    @Autowired
    public AppointmentController(AppointmentSlotService appointmentSlotService, 
                                 AppointmentRepository appointmentRepository) {
        this.appointmentSlotService = appointmentSlotService;
        this.appointmentRepository = appointmentRepository;
    }

    @GetMapping("/book-appointment")
    public String showBookAppointmentPage(HttpSession session, Model model) {
        // Check if donor is logged in (same pattern as dashboard)
        Object donor = session.getAttribute("donor");
        if (donor == null) {
            model.addAttribute("errorMessage", "Please login first to book an appointment.");
            return "redirect:/?error=true";
        }
        
        model.addAttribute("title", "Book Appointment");
        model.addAttribute("pageTitle", "Book Your Appointment");
        model.addAttribute("donor", donor);

        // Read appointment slots (same pattern as donor data reading)
        try {
            System.out.println("🔍 DEBUG: Starting to load appointment slots...");
            List<AppointmentSlot> availableSlots = appointmentSlotService.getAllSlots();
            System.out.println("📊 DEBUG: Loaded " + (availableSlots != null ? availableSlots.size() : "null") + " appointment slots");
            
            if (availableSlots != null && !availableSlots.isEmpty()) {
                System.out.println("📋 DEBUG: First slot details:");
                AppointmentSlot firstSlot = availableSlots.get(0);
                System.out.println("  - SlotID: " + firstSlot.getSlotId());
                System.out.println("  - Date: " + firstSlot.getSlotDate());
                System.out.println("  - Time: " + firstSlot.getSlotTime());
                System.out.println("  - Location: " + firstSlot.getLocation());
                System.out.println("  - Max Donors: " + firstSlot.getMaxDonors());
                System.out.println("  - Available Donors: " + firstSlot.getAvailableDonors());
            }
            
            model.addAttribute("availableSlots", availableSlots);
        } catch (Exception e) {
            System.out.println("❌ ERROR loading appointment slots: " + e.getMessage());
            e.printStackTrace();
            model.addAttribute("availableSlots", List.of());
        }
        
        return "BookAppointment";
    }

    @GetMapping("/test-slots")
    public String testSlots(Model model) {
        System.out.println("🧪 TESTING APPOINTMENT SLOTS DATABASE CONNECTION");
        System.out.println("🔍 Using the SAME database connection as Donor table");
        
        try {
            // Test 1: Count query (same pattern as donor)
            System.out.println("📊 Test 1: Count query...");
            Long count = appointmentSlotService.countSlots();
            System.out.println("📈 Count result: " + count);
            
            // Test 2: Find all (same pattern as donor)
            System.out.println("📊 Test 2: Find all...");
            List<AppointmentSlot> allSlots = appointmentSlotService.getAllSlots();
            System.out.println("📈 Find all result: " + allSlots.size() + " slots");
            
            // Test 3: Check if table exists (same as donor table check)
            System.out.println("📊 Test 3: Repository check...");
            System.out.println("📈 Repository class: " + appointmentSlotService.getClass().getSimpleName());
            
            model.addAttribute("countResult", count);
            model.addAttribute("slotsResult", allSlots.size());
            model.addAttribute("testStatus", "SUCCESS");
            
            if (!allSlots.isEmpty()) {
                model.addAttribute("firstSlot", allSlots.get(0));
                System.out.println("✅ SUCCESS: Found " + allSlots.size() + " appointment slots in database!");
            } else {
                System.out.println("⚠️ WARNING: No appointment slots found in database");
            }
            
        } catch (Exception e) {
            System.out.println("❌ DATABASE TEST FAILED: " + e.getMessage());
            e.printStackTrace();
            model.addAttribute("testStatus", "FAILED: " + e.getMessage());
        }
        
        return "test-slots";
    }

    @PostMapping("/book-appointment")
    public String submitAppointmentBooking(
            @RequestParam("slotId") Long slotId,
            @RequestParam("appointmentDate") String appointmentDate,
            @RequestParam("appointmentTime") String appointmentTime,
            @RequestParam("donationCenter") String donationCenter,
            @RequestParam("donationType") String donationType,
            @RequestParam(value = "additionalNotes", required = false) String additionalNotes,
            @RequestParam("agreeTerms") String agreeTerms,
            HttpSession session,
            Model model) {
        
        try {
            // Check if donor is logged in
            Object donorObj = session.getAttribute("donor");
            if (donorObj == null) {
                return "redirect:/";
            }

            Donor donor = (Donor) donorObj;

            System.out.println("=== Appointment Booking Submission ===");
            System.out.println("Donor NIC: " + donor.getNic());
            System.out.println("Slot ID: " + slotId);
            System.out.println("Appointment Date: " + appointmentDate);
            System.out.println("Appointment Time: " + appointmentTime);
            System.out.println("Donation Center: " + donationCenter);
            System.out.println("Donation Type: " + donationType);
            System.out.println("Additional Notes: " + additionalNotes);
            System.out.println("Agree Terms: " + agreeTerms);

            // Create and save appointment to Appointments table
            Appointment appointment = new Appointment();
            appointment.setDonorNic(donor.getNic());
            appointment.setSlotId(slotId);
            // BookingDate and BookingTime will be set automatically by database (DEFAULT GETDATE())

            Appointment savedAppointment = appointmentRepository.save(appointment);
            System.out.println("✅ Appointment saved with ID: " + savedAppointment.getAppointmentId());
            System.out.println("✅ Booking Date: " + savedAppointment.getBookingDate());
            System.out.println("✅ Booking Time: " + savedAppointment.getBookingTime());

            System.out.println("🎉 Appointment booking completed successfully!");
            
            // Redirect with appointment details for notification
            return "redirect:/dashboard?appointmentSuccess=true&appointmentTime=" + 
                   appointmentTime + "&appointmentLocation=" + donationCenter;
            
        } catch (Exception ex) {
            System.out.println("❌ Error processing appointment booking: " + ex.getMessage());
            ex.printStackTrace();
            return "redirect:/dashboard?appointmentError=true";
        }
    }

    @DeleteMapping("/hospital/appointments/delete/{appointmentId}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> deleteAppointment(@PathVariable Long appointmentId) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            System.out.println("=== Deleting Appointment ===");
            System.out.println("Appointment ID to delete: " + appointmentId);
            
            // Check if appointment exists
            if (!appointmentRepository.existsById(appointmentId)) {
                System.out.println("❌ Appointment not found: " + appointmentId);
                response.put("success", false);
                response.put("message", "Appointment not found");
                return ResponseEntity.badRequest().body(response);
            }
            
            // Delete the appointment
            appointmentRepository.deleteById(appointmentId);
            
            System.out.println("✅ Appointment deleted successfully: " + appointmentId);
            
            response.put("success", true);
            response.put("message", "Appointment deleted successfully");
            response.put("appointmentId", appointmentId);
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            System.err.println("❌ Error deleting appointment: " + e.getMessage());
            e.printStackTrace();
            
            response.put("success", false);
            response.put("message", "Error deleting appointment: " + e.getMessage());
            
            return ResponseEntity.internalServerError().body(response);
        }
    }
}