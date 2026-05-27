package com.blooddonation.controller;

import com.blooddonation.model.SystemUser;
import com.blooddonation.model.Donor;
import com.blooddonation.model.Receiver;
import com.blooddonation.model.BloodInventory;
import com.blooddonation.model.HealthAssessment;
import com.blooddonation.model.DoctorNote;
import com.blooddonation.model.BloodRequest;
import com.blooddonation.dto.ApiResponse;
import com.blooddonation.dto.BloodRequestRequest;
import com.blooddonation.service.SystemUserService;
import com.blooddonation.service.DonorService;
import com.blooddonation.service.ReceiverService;
import com.blooddonation.service.BloodInventoryService;
import com.blooddonation.service.HealthAssessmentService;
import com.blooddonation.service.DoctorNoteService;
import com.blooddonation.service.BloodRequestService;
import com.blooddonation.service.AppointmentSlotService;
import com.blooddonation.repository.AppointmentSlotRepository;
import com.blooddonation.repository.AppointmentRepository;
import com.blooddonation.repository.ReceiverRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpSession;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Controller
public class HomeController {

    @Autowired
    private SystemUserService systemUserService;
    
    @Autowired
    private DonorService donorService;
    
    @Autowired
    private ReceiverService receiverService;
    
    @Autowired
    private BloodInventoryService bloodInventoryService;
    
    @Autowired
    private HealthAssessmentService healthAssessmentService;

    @Autowired
    private DoctorNoteService doctorNoteService;

    @Autowired
    private BloodRequestService bloodRequestService;
    
    @Autowired
    private ReceiverRepository receiverRepository;

    @Autowired
    private AppointmentSlotService appointmentSlotService;

    @Autowired
    private AppointmentSlotRepository appointmentSlotRepository;
    
    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private com.blooddonation.service.DonorSupportMessageService donorSupportMessageService;

    @Autowired
    private com.blooddonation.service.ReceiverSupportMessageService receiverSupportMessageService;

    @GetMapping("/test")
    public String test(Model model, HttpSession session) {
        model.addAttribute("userFullName", session.getAttribute("userFullName"));
        return "TestTemplate";
    }

    @GetMapping("/admin/view-slots")
    @ResponseBody
    public Map<String, Object> viewAppointmentSlots() {
        Map<String, Object> response = new HashMap<>();
        try {
            System.out.println("🔍 Viewing Appointment Slots Table Contents");
            
            // Get all slots from the table
            List<com.blooddonation.model.AppointmentSlot> slots = appointmentSlotRepository.findAll();
            System.out.println("📊 Found " + slots.size() + " appointment slots in database");
            
            response.put("success", true);
            response.put("totalSlots", slots.size());
            response.put("slots", slots);
            response.put("message", "Successfully retrieved appointment slots from database");
            
            // Log each slot for debugging
            for (com.blooddonation.model.AppointmentSlot slot : slots) {
                System.out.println("📋 Slot: ID=" + slot.getSlotId() + 
                    ", Date=" + slot.getSlotDate() + 
                    ", Time=" + slot.getSlotTime() + 
                    ", Location=" + slot.getLocation() + 
                    ", Max=" + slot.getMaxDonors() + 
                    ", Available=" + slot.getAvailableDonors());
            }
            
        } catch (Exception e) {
            System.out.println("❌ Error viewing appointment slots: " + e.getMessage());
            e.printStackTrace();
            response.put("success", false);
            response.put("error", e.getMessage());
            response.put("errorType", e.getClass().getSimpleName());
        }
        return response;
    }

    @GetMapping("/admin/test-slots")
    @ResponseBody
    public Map<String, Object> testAppointmentSlots() {
        Map<String, Object> response = new HashMap<>();
        try {
            System.out.println("🧪 Testing Appointment Slots API");
            
            // Test 1: Count slots
            Long count = appointmentSlotService.countSlots();
            System.out.println("📊 Count: " + count);
            response.put("count", count);
            
            // Test 2: Get all slots
            List<com.blooddonation.model.AppointmentSlot> slots = appointmentSlotService.getAllSlots();
            System.out.println("📊 All slots: " + slots.size());
            response.put("slotsCount", slots.size());
            response.put("slots", slots);
            
            // Test 3: Direct repository test
            List<com.blooddonation.model.AppointmentSlot> directSlots = appointmentSlotRepository.findAll();
            System.out.println("📊 Direct repository: " + directSlots.size());
            response.put("directCount", directSlots.size());
            
            response.put("success", true);
            response.put("message", "Test completed successfully");
            
        } catch (Exception e) {
            System.out.println("❌ Test failed: " + e.getMessage());
            e.printStackTrace();
            response.put("success", false);
            response.put("error", e.getMessage());
            response.put("errorType", e.getClass().getSimpleName());
        }
        return response;
    }

    @GetMapping("/")
    public String loadPage() {
        return "Loadpage"; // Returns Loadpage.html as the entry point
    }

    @GetMapping("/main")
    public String home(@RequestParam(required = false) String success,
                      @RequestParam(required = false) String error,
                      @RequestParam(required = false) String receiverSuccess,
                      @RequestParam(required = false) String receiverError,
                      Model model, 
                      HttpSession session) {
        model.addAttribute("title", "Blood Donation System");
        model.addAttribute("welcomeMessage", "Welcome to our Life-Saving Community");
        
        if ("true".equals(success)) {
            model.addAttribute("successMessage", "Registration successful! You can now login with your NIC and password.");
        }
        if ("true".equals(error)) {
            model.addAttribute("loginError", "Invalid NIC or password. Please try again.");
        }
        if ("true".equals(receiverSuccess)) {
            model.addAttribute("receiverSuccessMessage", "Healthcare facility registration successful! Your information has been saved.");
        }
        if ("true".equals(receiverError)) {
            model.addAttribute("receiverLoginError", "Invalid CNIC or password. Please try again.");
        }
        
        // Check if donor is logged in
        Object donor = session.getAttribute("donor");
        if (donor != null) {
            model.addAttribute("loggedInDonor", donor);
        }
        
        return "Mainpage"; // Returns Mainpage.html from templates folder
    }

    @GetMapping("/register")
    public String registerForm(Model model) {
        model.addAttribute("title", "Donor Registration");
        model.addAttribute("pageTitle", "Become a Life Saver");
        return "Donorregister"; // Returns Donorregister.html from templates folder
    }

    @GetMapping("/receiver/register")
    public String receiverRegisterForm(Model model) {
        model.addAttribute("title", "Receiver Registration");
        model.addAttribute("pageTitle", "Healthcare Facility Registration");
        return "Receiverregister"; // Returns Receiverregister.html from templates folder
    }

    @PostMapping("/receiver/register")
    public String registerReceiver(@RequestParam String nic,
                                  @RequestParam String fullName,
                                  @RequestParam String dob,
                                  @RequestParam String gender,
                                  @RequestParam String contactNumber,
                                  @RequestParam(required = false) String email,
                                  @RequestParam(required = false) String address,
                                  @RequestParam String bloodTypeNeeded,
                                  @RequestParam(required = false) String hospitalPreference,
                                  @RequestParam(required = false) String medicalCondition,
                                  @RequestParam String password,
                                  Model model) {
        try {
            // Check if receiver already exists
            if (receiverService.existsByNic(nic)) {
                model.addAttribute("error", "A receiver with this NIC already exists.");
                return "Receiverregister";
            }

            // Create new receiver
            Receiver receiver = new Receiver();
            receiver.setNic(nic);
            receiver.setFullName(fullName);
            receiver.setDateOfBirth(LocalDate.parse(dob));
            receiver.setGender(gender);
            receiver.setContactNumber(contactNumber);
            receiver.setEmail(email != null && !email.trim().isEmpty() ? email : null);
            receiver.setAddress(address != null && !address.trim().isEmpty() ? address : null);
            receiver.setBloodTypeNeeded(bloodTypeNeeded);
            receiver.setHospitalPreference(hospitalPreference != null && !hospitalPreference.trim().isEmpty() ? hospitalPreference : null);
            receiver.setMedicalCondition(medicalCondition != null && !medicalCondition.trim().isEmpty() ? medicalCondition : null);
            receiver.setPassword(password);
            
            // Set registration date to current date
            receiver.setRegistrationDate(LocalDate.now());

            // Save receiver
            receiverService.saveReceiver(receiver);

            // Redirect to home page with success message
            return "redirect:/?receiverSuccess=true";

        } catch (Exception e) {
            System.err.println("Receiver registration error: " + e.getMessage());
            e.printStackTrace();
            model.addAttribute("error", "Registration failed: " + e.getMessage() + ". Please try again.");
            return "Receiverregister";
        }
    }

    @PostMapping("/receiver/login")
    public String loginReceiver(@RequestParam String nic,
                               @RequestParam String password,
                               HttpSession session,
                               Model model) {
        try {
            // Find receiver by NIC and password
            Optional<Receiver> receiverOpt = receiverService.findByNicAndPassword(nic, password);
            
            if (receiverOpt.isPresent()) {
                Receiver receiver = receiverOpt.get();
                
                // Store receiver in session
                session.setAttribute("receiver", receiver);
                
                // Redirect to receiver dashboard
                return "redirect:/receiver/dashboard";
            } else {
                // Invalid credentials
                model.addAttribute("receiverLoginError", "Invalid CNIC or password. Please try again.");
                return "redirect:/?receiverError=true";
            }
        } catch (Exception e) {
            System.err.println("Receiver login error: " + e.getMessage());
            e.printStackTrace();
            model.addAttribute("receiverLoginError", "Login failed. Please try again.");
            return "redirect:/?receiverError=true";
        }
    }

    @GetMapping("/receiver/dashboard")
    public String receiverDashboard(@RequestParam(required = false) String success,
                                   @RequestParam(required = false) String error,
                                   Model model, 
                                   HttpSession session) {
        // Check if receiver is logged in
        Object receiver = session.getAttribute("receiver");
        if (receiver == null) {
            return "redirect:/";
        }
        
        if (success != null) {
            model.addAttribute("successMessage", success);
        }
        if (error != null) {
            model.addAttribute("errorMessage", error);
        }
        
        // Check for notifications
        String notificationMessage = (String) session.getAttribute("notificationMessage");
        String notificationType = (String) session.getAttribute("notificationType");
        String notificationReceiverCnic = (String) session.getAttribute("notificationReceiverCnic");
        
        Receiver receiverObj = (Receiver) receiver;
        if (notificationMessage != null && receiverObj.getNic().equals(notificationReceiverCnic)) {
            if ("success".equals(notificationType)) {
                model.addAttribute("successMessage", notificationMessage);
            } else {
                model.addAttribute("errorMessage", notificationMessage);
            }
            
            // Clear the notification after displaying
            session.removeAttribute("notificationMessage");
            session.removeAttribute("notificationType");
            session.removeAttribute("notificationReceiverCnic");
        }
        
        model.addAttribute("title", "Receiver Dashboard");
        model.addAttribute("pageTitle", "Healthcare Facility Dashboard");
        model.addAttribute("receiver", receiver);
        
        return "ReceiverDashboard";
    }

    @GetMapping("/receiver/blood-request-history")
    @ResponseBody
    public Map<String, Object> getReceiverBloodRequestHistory(HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            // Check if receiver is logged in
            Object receiver = session.getAttribute("receiver");
            if (receiver == null) {
                response.put("success", false);
                response.put("message", "Receiver not logged in");
                return response;
            }
            
            Receiver receiverObj = (Receiver) receiver;
            String receiverCnic = receiverObj.getNic();
            
            // Get all blood requests for this receiver
            List<BloodRequest> bloodRequests = bloodRequestService.getBloodRequestsByReceiver(receiverCnic);
            
            // Add receiver names to blood requests
            for (BloodRequest request : bloodRequests) {
                request.setReceiverName(receiverObj.getFullName());
            }
            
            response.put("success", true);
            response.put("bloodRequests", bloodRequests);
            response.put("count", bloodRequests.size());
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error retrieving blood request history: " + e.getMessage());
            e.printStackTrace();
        }
        
        return response;
    }

    @GetMapping("/receiver/logout")
    public String logoutReceiver(HttpSession session) {
        session.removeAttribute("receiver");
        return "redirect:/";
    }

    @GetMapping("/receiver/edit-profile")
    public String showReceiverEditProfileForm(HttpSession session, Model model) {
        Receiver receiver = (Receiver) session.getAttribute("receiver");
        if (receiver == null) {
            return "redirect:/";
        }
        
        model.addAttribute("receiver", receiver);
        model.addAttribute("isEdit", true);
        return "EditReceiverProfile";
    }

    @PostMapping("/receiver/update-profile")
    public String updateReceiverProfile(@RequestParam String fullName,
                                       @RequestParam String dateOfBirth,
                                       @RequestParam String gender,
                                       @RequestParam String contactNumber,
                                       @RequestParam(required = false) String email,
                                       @RequestParam(required = false) String address,
                                       @RequestParam String bloodTypeNeeded,
                                       @RequestParam(required = false) String hospitalPreference,
                                       @RequestParam(required = false) String medicalCondition,
                                       HttpSession session,
                                       Model model) {
        Receiver currentReceiver = (Receiver) session.getAttribute("receiver");
        if (currentReceiver == null) {
            return "redirect:/";
        }

        try {
            // Update the receiver with new information
            currentReceiver.setFullName(fullName);
            currentReceiver.setDateOfBirth(LocalDate.parse(dateOfBirth));
            currentReceiver.setGender(gender);
            currentReceiver.setContactNumber(contactNumber);
            currentReceiver.setEmail(email != null && !email.trim().isEmpty() ? email : null);
            currentReceiver.setAddress(address != null && !address.trim().isEmpty() ? address : null);
            currentReceiver.setBloodTypeNeeded(bloodTypeNeeded);
            currentReceiver.setHospitalPreference(hospitalPreference != null && !hospitalPreference.trim().isEmpty() ? hospitalPreference : null);
            currentReceiver.setMedicalCondition(medicalCondition != null && !medicalCondition.trim().isEmpty() ? medicalCondition : null);

            // Save the updated receiver
            receiverService.saveReceiver(currentReceiver);
            
            // Update session with the updated receiver
            session.setAttribute("receiver", currentReceiver);
            
            return "redirect:/receiver/dashboard?success=Profile updated successfully!";
        } catch (Exception ex) {
            model.addAttribute("error", ex.getMessage());
            model.addAttribute("receiver", currentReceiver);
            model.addAttribute("isEdit", true);
            return "EditReceiverProfile";
        }
    }

    @GetMapping("/receiver/search-donors")
    public String searchDonorsPage(Model model, HttpSession session) {
        // Check if receiver is logged in
        Object receiver = session.getAttribute("receiver");
        if (receiver == null) {
            return "redirect:/";
        }
        
        model.addAttribute("title", "Search Compatible Donors");
        model.addAttribute("pageTitle", "Search Compatible Donors");
        model.addAttribute("receiver", receiver);
        
        return "SearchDonors";
    }

    @GetMapping("/receiver/blood-request")
    public String bloodRequestPage(Model model, HttpSession session) {
        // Check if receiver is logged in
        Object receiver = session.getAttribute("receiver");
        if (receiver == null) {
            return "redirect:/";
        }
        
        Receiver receiverObj = (Receiver) receiver;
        
        // Create BloodRequestRequest with pre-filled CNIC
        BloodRequestRequest bloodRequestRequest = new BloodRequestRequest();
        bloodRequestRequest.setReceiverCNIC(receiverObj.getNic());
        
        model.addAttribute("title", "Blood Request Form");
        model.addAttribute("pageTitle", "Blood Request Form");
        model.addAttribute("receiver", receiver);
        model.addAttribute("bloodRequestRequest", bloodRequestRequest);
        
        return "Bloodrequestpage";
    }

    @PostMapping("/receiver/blood-request")
    public String submitBloodRequest(@ModelAttribute BloodRequestRequest request,
                                   HttpSession session,
                                   RedirectAttributes redirectAttributes) {
        try {
            // Check if receiver is logged in
            Object receiverObj = session.getAttribute("receiver");
            if (receiverObj == null) {
                return "redirect:/";
            }
            
            Receiver receiver = (Receiver) receiverObj;
            
            // Validate that the CNIC matches the logged-in receiver
            if (!receiver.getNic().equals(request.getReceiverCNIC())) {
                redirectAttributes.addFlashAttribute("errorMessage", 
                    "CNIC does not match your logged-in account. Please contact support.");
                return "redirect:/receiver/blood-request";
            }
            
            // Create and save the blood request
            BloodRequest bloodRequest = bloodRequestService.createBloodRequest(request);
            
            // Add success message
            redirectAttributes.addFlashAttribute("successMessage", 
                "Blood request submitted successfully! Request ID: " + bloodRequest.getRequestId() + 
                ". You will be notified once it is reviewed by the hospital.");
            
            return "redirect:/receiver/dashboard";
            
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/receiver/blood-request";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", 
                "An error occurred while submitting your blood request. Please try again.");
            return "redirect:/receiver/blood-request";
        }
    }

    // Hospital Staff Blood Request Management
    @GetMapping("/hospital/blood-requests")
    @ResponseBody
    public Map<String, Object> getBloodRequestsForHospital(HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            // Check if hospital staff is logged in
            Object systemUser = session.getAttribute("systemUser");
            Object userRole = session.getAttribute("userRole");
            
            if (systemUser == null) {
                response.put("success", false);
                response.put("message", "Hospital staff not logged in");
                return response;
            }
            
            // Check if user has hospital staff role
            String role = userRole != null ? userRole.toString().toLowerCase().trim() : "";
            if (!"hospital staff".equals(role) && !"hospitalstaff".equals(role)) {
                response.put("success", false);
                response.put("message", "Access denied. This endpoint is only for hospital staff.");
                return response;
            }
            
            // Get all blood requests (not just pending ones for now)
            List<BloodRequest> allRequests = bloodRequestService.getAllBloodRequests();
            List<BloodRequest> pendingRequests = bloodRequestService.getPendingBloodRequests();
            
            // Add receiver names to blood requests
            for (BloodRequest request : allRequests) {
                try {
                    Optional<Receiver> receiver = receiverRepository.findById(request.getReceiverCnic());
                    if (receiver.isPresent()) {
                        // Add receiver name to the request object for JSON serialization
                        request.setReceiverName(receiver.get().getFullName());
                    }
                } catch (Exception e) {
                    System.err.println("Error fetching receiver name for CNIC " + request.getReceiverCnic() + ": " + e.getMessage());
                }
            }
            
            // Debug logging
            System.out.println("Total blood requests found: " + allRequests.size());
            System.out.println("Pending blood requests found: " + pendingRequests.size());
            
            if (!allRequests.isEmpty()) {
                BloodRequest firstRequest = allRequests.get(0);
                System.out.println("First blood request details:");
                System.out.println("  Request ID: " + firstRequest.getRequestId());
                System.out.println("  Receiver CNIC: " + firstRequest.getReceiverCnic());
                System.out.println("  Blood Type: " + firstRequest.getBloodTypeNeeded());
                System.out.println("  Quantity: " + firstRequest.getQuantity());
                System.out.println("  Priority: " + firstRequest.getPriority());
                System.out.println("  Hospital: " + firstRequest.getHospital());
                System.out.println("  Notes: " + firstRequest.getNotes());
                System.out.println("  Request Date: " + firstRequest.getRequestDate());
                System.out.println("  Needed By Date: " + firstRequest.getNeededByDate());
                System.out.println("  Approved Date: " + firstRequest.getApprovedDate());
                System.out.println("  Completion Date: " + firstRequest.getCompletionDate());
            }
            
            response.put("success", true);
            response.put("bloodRequests", allRequests); // Return all requests for now
            response.put("pendingRequests", pendingRequests);
            response.put("count", allRequests.size());
            response.put("pendingCount", pendingRequests.size());
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error retrieving blood requests: " + e.getMessage());
            e.printStackTrace(); // Add stack trace for debugging
        }
        
        return response;
    }

    @PostMapping("/hospital/blood-requests/{requestId}/approve")
    @ResponseBody
    public Map<String, Object> approveBloodRequest(@PathVariable Long requestId, HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            // Check if hospital staff is logged in
            Object systemUser = session.getAttribute("systemUser");
            Object userRole = session.getAttribute("userRole");
            
            if (systemUser == null) {
                response.put("success", false);
                response.put("message", "Hospital staff not logged in");
                return response;
            }
            
            // Check if user has hospital staff role
            String role = userRole != null ? userRole.toString().toLowerCase().trim() : "";
            if (!"hospital staff".equals(role) && !"hospitalstaff".equals(role)) {
                response.put("success", false);
                response.put("message", "Access denied. This endpoint is only for hospital staff.");
                return response;
            }
            
            // Approve the blood request
            BloodRequest approvedRequest = bloodRequestService.approveBloodRequest(requestId);
            
            // OBSERVER PATTERN: Automatically notify all stakeholders
            com.blooddonation.observer.BloodRequestObserverManager observerManager = 
                new com.blooddonation.observer.BloodRequestObserverManager(approvedRequest);
            observerManager.notifyApproved();
            
            // Send notification to receiver (existing code - still works)
            String receiverCnic = approvedRequest.getReceiverCnic();
            String notificationMessage = "✅ Your blood request has been approved! Our hospital staff will contact you shortly with further details. Thank you for your patience and trust in our service.";
            
            // Store notification for receiver
            sendNotificationToReceiver(receiverCnic, "APPROVED", notificationMessage, approvedRequest.getRequestId());
            
            response.put("success", true);
            response.put("message", "Blood request approved successfully");
            response.put("requestId", approvedRequest.getRequestId());
            response.put("notificationMessage", notificationMessage);
            
        } catch (IllegalArgumentException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error approving blood request: " + e.getMessage());
        }
        
        return response;
    }

    @PostMapping("/hospital/blood-requests/{requestId}/complete")
    @ResponseBody
    public Map<String, Object> completeBloodRequest(@PathVariable Long requestId, HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            // Check if hospital staff is logged in
            Object systemUser = session.getAttribute("systemUser");
            Object userRole = session.getAttribute("userRole");
            
            if (systemUser == null) {
                response.put("success", false);
                response.put("message", "Hospital staff not logged in");
                return response;
            }
            
            // Check if user has hospital staff role
            String role = userRole != null ? userRole.toString().toLowerCase().trim() : "";
            if (!"hospital staff".equals(role) && !"hospitalstaff".equals(role)) {
                response.put("success", false);
                response.put("message", "Access denied. This endpoint is only for hospital staff.");
                return response;
            }
            
            // Complete the blood request
            BloodRequest completedRequest = bloodRequestService.completeBloodRequest(requestId);
            
            // OBSERVER PATTERN: Automatically notify all stakeholders
            com.blooddonation.observer.BloodRequestObserverManager observerManager = 
                new com.blooddonation.observer.BloodRequestObserverManager(completedRequest);
            observerManager.notifyCompleted();
            
            // Send advanced notification to receiver about blood collection
            String receiverCnic = completedRequest.getReceiverCnic();
            String hospitalName = completedRequest.getHospital();
            String bloodType = completedRequest.getBloodTypeNeeded();
            Integer quantity = completedRequest.getQuantity();
            
            String notificationMessage = String.format(
                "🎉 GREAT NEWS! Your blood request has been completed and is ready for collection!\n\n" +
                "📋 Request Details:\n" +
                "• Request ID: %d\n" +
                "• Blood Type: %s\n" +
                "• Quantity: %d units\n" +
                "• Hospital: %s\n\n" +
                "🏥 Please visit %s to collect your blood. Bring a valid ID and this request ID for verification.\n\n" +
                "⏰ Collection Hours: 8:00 AM - 6:00 PM (Monday to Friday)\n" +
                "📞 Contact: Hospital Blood Bank - (123) 456-7890\n\n" +
                "Thank you for using our blood donation system!",
                completedRequest.getRequestId(), bloodType, quantity, hospitalName, hospitalName
            );
            
            // Store advanced notification for receiver
            sendNotificationToReceiver(receiverCnic, "COMPLETED", notificationMessage, completedRequest.getRequestId());
            
            response.put("success", true);
            response.put("message", "Blood request completed successfully");
            response.put("requestId", completedRequest.getRequestId());
            response.put("notificationMessage", notificationMessage);
            
        } catch (IllegalArgumentException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error completing blood request: " + e.getMessage());
        }
        
        return response;
    }

    // Test endpoint to create sample blood request data
    @GetMapping("/test/create-sample-blood-requests")
    @ResponseBody
    public Map<String, Object> createSampleBloodRequests() {
        Map<String, Object> response = new HashMap<>();
        
        try {
            // Create sample blood requests for testing
            BloodRequestRequest request1 = new BloodRequestRequest();
            request1.setReceiverCNIC("12345-1234567-1");
            request1.setBloodType("O+");
            request1.setQuantity(2);
            request1.setNeededByDate(LocalDate.now().plusDays(3));
            request1.setHospital("General Hospital");
            request1.setPriority("High");
            request1.setNotes("Emergency surgery required");
            
            BloodRequestRequest request2 = new BloodRequestRequest();
            request2.setReceiverCNIC("12345-1234567-2");
            request2.setBloodType("A+");
            request2.setQuantity(1);
            request2.setNeededByDate(LocalDate.now().plusDays(7));
            request2.setHospital("City Medical Center");
            request2.setPriority("Normal");
            request2.setNotes("Scheduled operation");
            
            BloodRequest bloodRequest1 = bloodRequestService.createBloodRequest(request1);
            BloodRequest bloodRequest2 = bloodRequestService.createBloodRequest(request2);
            
            response.put("success", true);
            response.put("message", "Sample blood requests created successfully");
            response.put("createdRequests", List.of(bloodRequest1.getRequestId(), bloodRequest2.getRequestId()));
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error creating sample blood requests: " + e.getMessage());
            e.printStackTrace();
        }
        
        return response;
    }

    @PostMapping("/hospital/blood-requests/{requestId}/reject")
    @ResponseBody
    public Map<String, Object> rejectBloodRequest(@PathVariable Long requestId, HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            // Check if hospital staff is logged in
            Object systemUser = session.getAttribute("systemUser");
            Object userRole = session.getAttribute("userRole");
            
            if (systemUser == null) {
                response.put("success", false);
                response.put("message", "Hospital staff not logged in");
                return response;
            }
            
            // Check if user has hospital staff role
            String role = userRole != null ? userRole.toString().toLowerCase().trim() : "";
            if (!"hospital staff".equals(role) && !"hospitalstaff".equals(role)) {
                response.put("success", false);
                response.put("message", "Access denied. This endpoint is only for hospital staff.");
                return response;
            }
            
            // Get receiver CNIC before deletion
            Optional<BloodRequest> requestOpt = bloodRequestService.getBloodRequestById(requestId);
            String receiverCnic = requestOpt.isPresent() ? requestOpt.get().getReceiverCnic() : "Unknown";
            Long requestIdLong = requestOpt.isPresent() ? requestOpt.get().getRequestId() : requestId;
            
            // Delete the blood request
            bloodRequestService.deleteBloodRequest(requestId);
            
            // Send notification to receiver
            String notificationMessage = "❌ We regret to inform you that your blood request could not be processed due to an issue. Please contact our support team for further assistance. We sincerely apologize for any inconvenience caused.";
            
            // Store notification for receiver
            sendNotificationToReceiver(receiverCnic, "REJECTED", notificationMessage, requestIdLong);
            
            response.put("success", true);
            response.put("message", "Blood request rejected and deleted successfully");
            response.put("notificationMessage", notificationMessage);
            
        } catch (IllegalArgumentException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error rejecting blood request: " + e.getMessage());
            e.printStackTrace();
        }
        
        return response;
    }

    @DeleteMapping("/hospital/blood-requests/{requestId}/delete")
    @ResponseBody
    public Map<String, Object> deleteBloodRequest(@PathVariable Long requestId, HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            // Check if hospital staff is logged in
            Object systemUser = session.getAttribute("systemUser");
            Object userRole = session.getAttribute("userRole");
            
            if (systemUser == null) {
                response.put("success", false);
                response.put("message", "Hospital staff not logged in");
                return response;
            }
            
            // Check if user has hospital staff role
            String role = userRole != null ? userRole.toString().toLowerCase().trim() : "";
            if (!"hospital staff".equals(role) && !"hospitalstaff".equals(role)) {
                response.put("success", false);
                response.put("message", "Access denied. This endpoint is only for hospital staff.");
                return response;
            }
            
            // Check if blood request exists and is completed
            Optional<BloodRequest> requestOpt = bloodRequestService.getBloodRequestById(requestId);
            if (requestOpt.isEmpty()) {
                response.put("success", false);
                response.put("message", "Blood request not found");
                return response;
            }
            
            BloodRequest bloodRequest = requestOpt.get();
            
            // Only allow deletion of completed requests
            if (bloodRequest.getCompletionDate() == null) {
                response.put("success", false);
                response.put("message", "Only completed blood requests can be deleted");
                return response;
            }
            
            // Delete the blood request
            bloodRequestService.deleteBloodRequest(requestId);
            
            response.put("success", true);
            response.put("message", "Blood request deleted successfully");
            response.put("requestId", requestId);
            
        } catch (IllegalArgumentException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error deleting blood request: " + e.getMessage());
            e.printStackTrace();
        }
        
        return response;
    }

    // Notification endpoint for receiver dashboard (legacy - redirects to new API)
    @GetMapping("/receiver/notifications-legacy")
    public String getReceiverNotificationsLegacy(HttpSession session, Model model) {
        // Check if receiver is logged in
        Object receiver = session.getAttribute("receiver");
        if (receiver == null) {
            return "redirect:/";
        }
        
        // Redirect to receiver dashboard - notifications will be loaded via AJAX
        return "redirect:/receiver/dashboard";
    }

    // Test endpoint to check specific blood request
    @GetMapping("/test/check-blood-request/{requestId}")
    @ResponseBody
    public Map<String, Object> checkBloodRequest(@PathVariable Long requestId) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            Optional<BloodRequest> bloodRequest = bloodRequestService.getBloodRequestById(requestId);
            
            if (bloodRequest.isPresent()) {
                BloodRequest request = bloodRequest.get();
                response.put("success", true);
                response.put("found", true);
                response.put("bloodRequest", request);
                response.put("requestId", request.getRequestId());
                response.put("receiverCnic", request.getReceiverCnic());
                response.put("bloodType", request.getBloodTypeNeeded());
                response.put("quantity", request.getQuantity());
                response.put("hospital", request.getHospital());
                response.put("priority", request.getPriority());
                response.put("notes", request.getNotes());
                response.put("requestDate", request.getRequestDate());
                response.put("neededByDate", request.getNeededByDate());
            } else {
                response.put("success", true);
                response.put("found", false);
                response.put("message", "Blood request with ID " + requestId + " not found");
            }
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error checking blood request: " + e.getMessage());
            e.printStackTrace();
        }
        
        return response;
    }

    @GetMapping("/receiver/search-donors/api")
    @ResponseBody
    public Map<String, Object> searchCompatibleDonors(@RequestParam(required = false) String bloodType,
                                                    @RequestParam(required = false) String availability,
                                                    HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            // Check if receiver is logged in
            Object receiver = session.getAttribute("receiver");
            if (receiver == null) {
                response.put("success", false);
                response.put("message", "Receiver not logged in");
                return response;
            }
            
            System.out.println("=== Searching Compatible Donors ===");
            System.out.println("Blood Type Filter: " + bloodType);
            System.out.println("Availability Filter: " + availability);
            
            // Get all donors
            List<Donor> allDonors = donorService.getAllDonors();
            System.out.println("Total donors found: " + allDonors.size());
            
            // Filter donors based on criteria
            List<Donor> compatibleDonors = new ArrayList<>();
            
            for (Donor donor : allDonors) {
                boolean isCompatible = true;
                
                // Filter by blood type compatibility
                if (bloodType != null && !bloodType.isEmpty()) {
                    isCompatible = isBloodTypeCompatible(donor.getBloodType(), bloodType);
                }
                
                // Filter by availability
                if (availability != null && !availability.isEmpty() && isCompatible) {
                    switch (availability) {
                        case "available":
                            // For now, include all donors since we don't have last donation date
                            // In a real implementation, you'd check appointment history or donation records
                            break;
                        case "recent":
                            // For now, include all donors since we don't have last donation date
                            // In a real implementation, you'd check recent donation history
                            break;
                        case "eligible":
                            // Include all eligible donors (basic eligibility check)
                            // In a real implementation, you'd check health assessment status
                            break;
                    }
                }
                
                if (isCompatible) {
                    compatibleDonors.add(donor);
                }
            }
            
            System.out.println("Compatible donors found: " + compatibleDonors.size());
            
            response.put("success", true);
            response.put("donors", compatibleDonors);
            response.put("count", compatibleDonors.size());
            response.put("requestedBloodType", bloodType);
            
        } catch (Exception e) {
            System.err.println("Error searching compatible donors: " + e.getMessage());
            e.printStackTrace();
            response.put("success", false);
            response.put("message", "Error searching donors: " + e.getMessage());
        }
        
        return response;
    }

    // Helper method to check blood type compatibility
    private boolean isBloodTypeCompatible(String donorBloodType, String recipientBloodType) {
        if (donorBloodType == null || recipientBloodType == null) {
            return false;
        }
        
        // Blood type compatibility rules
        switch (recipientBloodType) {
            case "A+":
                return donorBloodType.equals("A+") || donorBloodType.equals("A-") || 
                       donorBloodType.equals("O+") || donorBloodType.equals("O-");
            case "A-":
                return donorBloodType.equals("A-") || donorBloodType.equals("O-");
            case "B+":
                return donorBloodType.equals("B+") || donorBloodType.equals("B-") || 
                       donorBloodType.equals("O+") || donorBloodType.equals("O-");
            case "B-":
                return donorBloodType.equals("B-") || donorBloodType.equals("O-");
            case "AB+":
                return true; // Universal recipient
            case "AB-":
                return donorBloodType.equals("AB-") || donorBloodType.equals("A-") || 
                       donorBloodType.equals("B-") || donorBloodType.equals("O-");
            case "O+":
                return donorBloodType.equals("O+") || donorBloodType.equals("O-");
            case "O-":
                return donorBloodType.equals("O-");
            default:
                return false;
        }
    }

    @GetMapping("/dashboard")
    public String dashboard(@RequestParam(required = false) String success,
                           @RequestParam(required = false) String error,
                           @RequestParam(required = false) String appointmentSuccess,
                           @RequestParam(required = false) String appointmentError,
                           @RequestParam(required = false) String appointmentTime,
                           @RequestParam(required = false) String appointmentLocation,
                           Model model, 
                           HttpSession session) {
        Object donor = session.getAttribute("donor");
        if (donor == null) {
            return "redirect:/";
        }
        
        if ("true".equals(success)) {
            model.addAttribute("successMessage", "Health assessment submitted successfully!");
        }
        if (success != null && !"true".equals(success)) {
            model.addAttribute("successMessage", success);
        }
        if ("true".equals(error)) {
            model.addAttribute("errorMessage", "Failed to submit health assessment. Please try again.");
        }
        if ("true".equals(appointmentSuccess)) {
            model.addAttribute("successMessage", "Appointment booking request submitted successfully! We will contact you soon to confirm your appointment.");
            if (appointmentTime != null && appointmentLocation != null) {
                model.addAttribute("appointmentTime", appointmentTime);
                model.addAttribute("appointmentLocation", appointmentLocation);
                model.addAttribute("showAppointmentNotification", true);
            }
        }
        if ("true".equals(appointmentError)) {
            model.addAttribute("errorMessage", "Failed to submit appointment booking. Please try again.");
        }
        
        model.addAttribute("title", "Donor Dashboard");
        model.addAttribute("pageTitle", "Your Dashboard");
        model.addAttribute("donor", donor);
        model.addAttribute("healthAssessment", new com.blooddonation.dto.HealthAssessmentRequest());
        return "Donordashboard"; // Returns Donordashboard.html from templates folder
    }

    @GetMapping("/staff/login")
    public String staffLogin(Model model) {
        model.addAttribute("title", "Staff Login - Blood Donation System");
        model.addAttribute("pageTitle", "Staff Login Portal");
        return "StaffLogin"; // Returns StaffLogin.html from templates folder
    }

    @PostMapping("/staff/login")
    public String processStaffLogin(@RequestParam("staffId") String staffId,
                                   @RequestParam("password") String password,
                                   Model model,
                                   HttpSession session) {
        try {
            System.out.println("=== Staff Login Controller Debug ===");
            System.out.println("Received Staff ID: '" + staffId + "'");
            System.out.println("Received Password: '" + password + "'");
            
            // First, try to find the user
            Optional<SystemUser> userOpt = systemUserService.findByUserId(staffId);
            if (!userOpt.isPresent()) {
                System.out.println("❌ User not found: " + staffId);
                model.addAttribute("errorMessage", "User ID '" + staffId + "' not found. Please check your credentials.");
                return "StaffLogin";
            }
            
            SystemUser user = userOpt.get();
            System.out.println("✅ User found: " + user.getUsername());
            System.out.println("🎭 Role: '" + user.getRole() + "'");
            System.out.println("🔑 Stored Password: '" + user.getPassword() + "'");
            System.out.println("🔑 Input Password: '" + password + "'");
            System.out.println("🟢 Is Active: " + user.getIsActive());
            
            // Check if user is active
            if (!"Active".equals(user.getIsActive())) {
                System.out.println("❌ User account is inactive");
                model.addAttribute("errorMessage", "Your account is inactive. Please contact administrator.");
                return "StaffLogin";
            }
            
            // Check password
            if (user.getPassword() == null || !user.getPassword().equals(password)) {
                System.out.println("❌ Password mismatch");
                model.addAttribute("errorMessage", "Invalid password. Please try again.");
                return "StaffLogin";
            }
            
            System.out.println("🎉 Authentication successful!");
            
            // Store user in session
            session.setAttribute("systemUser", user);
            session.setAttribute("userRole", user.getRole());
            session.setAttribute("userFullName", user.getFullName());
            
            // Redirect based on role
            String role = user.getRole();
            System.out.println("Processing role: '" + role + "'");
            
            // Simple role matching
            if (role != null) {
                String roleLower = role.toLowerCase().trim();
                
                if (roleLower.contains("admin")) {
                    System.out.println("✅ Redirecting to Admin Dashboard");
                    return "redirect:/admin/dashboard";
                } else if (roleLower.contains("doctor")) {
                    System.out.println("✅ Redirecting to Doctor Dashboard");
                    return "redirect:/doctor/dashboard";
                } else if (roleLower.contains("hospital")) {
                    System.out.println("✅ Redirecting to Hospital Staff Dashboard");
                    return "redirect:/hospital/dashboard";
                } else if (roleLower.contains("support") || roleLower.contains("customer")) {
                    System.out.println("✅ Redirecting to Customer Support Dashboard");
                    return "redirect:/support/dashboard";
                }
            }
            
            // If no specific role match, try exact matches
            if ("Admin".equals(role) || "admin".equals(role)) {
                System.out.println("✅ Redirecting to Admin Dashboard (exact match)");
                return "redirect:/admin/dashboard";
            } else if ("Doctor".equals(role) || "doctor".equals(role)) {
                System.out.println("✅ Redirecting to Doctor Dashboard (exact match)");
                return "redirect:/doctor/dashboard";
            } else if ("HospitalStaff".equals(role) || "Hospital Staff".equals(role)) {
                System.out.println("✅ Redirecting to Hospital Staff Dashboard (exact match)");
                return "redirect:/hospital/dashboard";
            }
            
            System.out.println("❌ Unknown role: '" + role + "'");
            model.addAttribute("errorMessage", "Unknown user role: " + role + ". Please contact administrator.");
            return "StaffLogin";
            
        } catch (Exception e) {
            System.err.println("❌ Error during staff login: " + e.getMessage());
            e.printStackTrace();
            model.addAttribute("errorMessage", "Login failed due to system error: " + e.getMessage() + ". Please try again.");
            return "StaffLogin";
        }
    }

    @GetMapping("/admin/dashboard")
    public String adminDashboard(Model model, HttpSession session) {
        // Check if user is logged in and has admin role
        Object systemUser = session.getAttribute("systemUser");
        Object userRole = session.getAttribute("userRole");
        
        // If no user in session, redirect to login
        if (systemUser == null) {
            return "redirect:/staff/login";
        }
        
        // If user exists but not admin, still show dashboard but with limited access
        if (userRole != null && !"admin".equalsIgnoreCase(userRole.toString())) {
            model.addAttribute("warningMessage", "You are logged in as " + userRole + ". Some features may be restricted.");
        }
        
        model.addAttribute("title", "Admin Dashboard - Blood Donation System");
        model.addAttribute("systemUser", systemUser);
        model.addAttribute("userRole", userRole);
        model.addAttribute("userFullName", session.getAttribute("userFullName"));
        
        return "AdminDashboardSimple";
    }

    @GetMapping("/doctor/dashboard")
    public String doctorDashboard(Model model, HttpSession session) {
        // Check if user is logged in
        Object systemUser = session.getAttribute("systemUser");
        Object userRole = session.getAttribute("userRole");
        
        // If no user in session, redirect to login
        if (systemUser == null) {
            return "redirect:/staff/login";
        }
        
        model.addAttribute("title", "Doctor Dashboard - Blood Donation System");
        model.addAttribute("systemUser", systemUser);
        model.addAttribute("userRole", userRole);
        model.addAttribute("userFullName", session.getAttribute("userFullName"));
        
        return "DoctorDashboard";
    }

    @GetMapping("/hospital/dashboard")
    public String hospitalDashboard(Model model, HttpSession session) {
        // Check if user is logged in
        Object systemUser = session.getAttribute("systemUser");
        Object userRole = session.getAttribute("userRole");
        
        // If no user in session, redirect to login
        if (systemUser == null) {
            return "redirect:/staff/login";
        }
        
        // Check if user has hospital staff role
        String role = userRole != null ? userRole.toString().toLowerCase().trim() : "";
        if (!"hospital staff".equals(role) && !"hospitalstaff".equals(role)) {
            model.addAttribute("errorMessage", "Access denied. This dashboard is only for hospital staff.");
            return "StaffLogin";
        }
        
        model.addAttribute("title", "Hospital Staff Dashboard - Blood Donation System");
        model.addAttribute("systemUser", systemUser);
        model.addAttribute("userRole", userRole);
        model.addAttribute("userFullName", session.getAttribute("userFullName"));
        
        return "HospitalStaffDashboard";
    }

    @GetMapping("/support/dashboard")
    public String supportDashboard(Model model, HttpSession session) {
        // Check if user is logged in
        Object systemUser = session.getAttribute("systemUser");
        Object userRole = session.getAttribute("userRole");
        
        // If no user in session, redirect to login
        if (systemUser == null) {
            return "redirect:/staff/login";
        }
        
        // Check if user has support role
        String role = userRole != null ? userRole.toString().toLowerCase().trim() : "";
        if (!role.contains("support") && !role.contains("customer")) {
            model.addAttribute("errorMessage", "Access denied. This dashboard is only for support staff.");
            return "StaffLogin";
        }
        
        model.addAttribute("title", "Customer Support Dashboard - Blood Donation System");
        model.addAttribute("systemUser", systemUser);
        model.addAttribute("userRole", userRole);
        model.addAttribute("userFullName", session.getAttribute("userFullName"));
        
        return "SupportcareDashboard";
    }

    @GetMapping("/support/donor-messages")
    @ResponseBody
    public Map<String, Object> getDonorMessages() {
        Map<String, Object> response = new HashMap<>();
        try {
            System.out.println("=== Fetching Donor Messages for Support Dashboard ===");
            List<com.blooddonation.model.DonorSupportMessage> messages = 
                donorSupportMessageService.getAllMessages();
            
            System.out.println("Messages found: " + messages.size());
            
            // Debug: Check for duplicates
            Set<Long> messageIds = new HashSet<>();
            Set<String> uniqueMessages = new HashSet<>();
            int duplicateCount = 0;
            
            for (com.blooddonation.model.DonorSupportMessage msg : messages) {
                if (!messageIds.add(msg.getMessageId())) {
                    duplicateCount++;
                    System.out.println("❌ Duplicate message ID found: " + msg.getMessageId());
                }
                
                String messageKey = msg.getDonorNic() + "|" + msg.getSubject() + "|" + msg.getMessageDate();
                if (!uniqueMessages.add(messageKey)) {
                    System.out.println("❌ Duplicate message content found: " + messageKey);
                }
            }
            
            if (duplicateCount > 0) {
                System.out.println("⚠️ Found " + duplicateCount + " duplicate messages!");
            }
            
            response.put("success", true);
            response.put("messages", messages);
            response.put("count", messages.size());
            response.put("duplicateCount", duplicateCount);
            
        } catch (Exception e) {
            System.err.println("Error fetching donor messages: " + e.getMessage());
            e.printStackTrace();
            response.put("success", false);
            response.put("message", "Error fetching messages: " + e.getMessage());
            response.put("messages", new java.util.ArrayList<>());
        }
        return response;
    }

    @GetMapping("/support/donor-messages/stats")
    @ResponseBody
    public Map<String, Object> getDonorMessageStats() {
        Map<String, Object> response = new HashMap<>();
        try {
            System.out.println("=== Fetching Donor Message Statistics ===");
            
            long totalMessages = donorSupportMessageService.getAllMessages().size();
            long pendingMessages = donorSupportMessageService.getPendingMessages().size();
            long resolvedMessages = donorSupportMessageService.getResolvedMessages().size();
            long highPriorityMessages = donorSupportMessageService.getHighPriorityPendingMessages().size();
            
            System.out.println("Stats - Total: " + totalMessages + ", Pending: " + pendingMessages + 
                             ", Resolved: " + resolvedMessages + ", High Priority: " + highPriorityMessages);
            
            response.put("success", true);
            response.put("totalMessages", totalMessages);
            response.put("pendingMessages", pendingMessages);
            response.put("resolvedMessages", resolvedMessages);
            response.put("highPriorityMessages", highPriorityMessages);
            
        } catch (Exception e) {
            System.err.println("Error fetching donor message stats: " + e.getMessage());
            e.printStackTrace();
            response.put("success", false);
            response.put("message", "Error fetching statistics: " + e.getMessage());
        }
        return response;
    }

    @PostMapping("/support/donor-messages/{messageId}/reply")
    @ResponseBody
    public Map<String, Object> replyToDonorMessage(@PathVariable Long messageId, 
                                                  @RequestBody Map<String, String> request) {
        Map<String, Object> response = new HashMap<>();
        try {
            System.out.println("=== Replying to Donor Message ===");
            System.out.println("Message ID: " + messageId);
            
            String replyText = request.get("replyText");
            if (replyText == null || replyText.trim().isEmpty()) {
                response.put("success", false);
                response.put("message", "Reply text cannot be empty");
                return response;
            }
            
            System.out.println("Reply text: " + replyText);
            
            // Update the message with reply
            com.blooddonation.model.DonorSupportMessage updatedMessage = 
                donorSupportMessageService.replyToMessage(messageId, replyText);
            
            if (updatedMessage != null) {
                System.out.println("✅ Reply saved successfully");
                response.put("success", true);
                response.put("message", "Reply sent successfully");
                response.put("updatedMessage", updatedMessage);
            } else {
                System.out.println("❌ Failed to save reply");
                response.put("success", false);
                response.put("message", "Failed to save reply");
            }
            
        } catch (Exception e) {
            System.err.println("Error replying to donor message: " + e.getMessage());
            e.printStackTrace();
            response.put("success", false);
            response.put("message", "Error sending reply: " + e.getMessage());
        }
        return response;
    }

    @PostMapping("/support/donor-messages/{messageId}/delete")
    @ResponseBody
    public Map<String, Object> deleteDonorMessage(@PathVariable Long messageId) {
        Map<String, Object> response = new HashMap<>();
        try {
            System.out.println("=== Deleting Donor Message ===");
            System.out.println("Message ID: " + messageId);
            
            // Check if message exists
            Optional<com.blooddonation.model.DonorSupportMessage> messageOpt = 
                donorSupportMessageService.getMessageById(messageId);
            
            if (!messageOpt.isPresent()) {
                response.put("success", false);
                response.put("message", "Message not found");
                return response;
            }
            
            // Delete the message
            donorSupportMessageService.deleteMessage(messageId);
            
            System.out.println("✅ Message deleted successfully");
            response.put("success", true);
            response.put("message", "Message deleted successfully");
            
        } catch (Exception e) {
            System.err.println("Error deleting donor message: " + e.getMessage());
            e.printStackTrace();
            response.put("success", false);
            response.put("message", "Error deleting message: " + e.getMessage());
        }
        return response;
    }

    // Receiver Support Message Endpoints for Support Dashboard
    @GetMapping("/support/receiver-messages")
    @ResponseBody
    public Map<String, Object> getReceiverMessages() {
        Map<String, Object> response = new HashMap<>();
        try {
            System.out.println("=== Fetching Receiver Messages for Support Dashboard ===");
            List<com.blooddonation.model.ReceiverSupportMessage> messages = 
                receiverSupportMessageService.getAllMessages();
            
            System.out.println("Receiver messages found: " + messages.size());
            
            // Debug: Check for duplicates
            Set<Long> messageIds = new HashSet<>();
            Set<String> uniqueMessages = new HashSet<>();
            int duplicateCount = 0;
            
            for (com.blooddonation.model.ReceiverSupportMessage msg : messages) {
                if (!messageIds.add(msg.getMessageId())) {
                    duplicateCount++;
                    System.out.println("❌ Duplicate receiver message ID found: " + msg.getMessageId());
                }
                
                String messageKey = msg.getReceiverNic() + "|" + msg.getSubject() + "|" + msg.getMessageDate();
                if (!uniqueMessages.add(messageKey)) {
                    System.out.println("❌ Duplicate receiver message content found: " + messageKey);
                }
            }
            
            if (duplicateCount > 0) {
                System.out.println("⚠️ Total duplicate receiver message IDs found: " + duplicateCount);
            }
            
            response.put("success", true);
            response.put("messages", messages);
            response.put("count", messages.size());
            
        } catch (Exception e) {
            System.err.println("Error fetching receiver messages: " + e.getMessage());
            e.printStackTrace();
            response.put("success", false);
            response.put("message", "Error fetching receiver messages: " + e.getMessage());
            response.put("messages", new java.util.ArrayList<>());
        }
        return response;
    }

    @GetMapping("/support/receiver-messages/stats")
    @ResponseBody
    public Map<String, Object> getReceiverMessageStats() {
        Map<String, Object> response = new HashMap<>();
        try {
            System.out.println("=== Fetching Receiver Message Stats ===");
            
            long totalMessages = receiverSupportMessageService.getAllMessages().size();
            long pendingMessages = receiverSupportMessageService.getPendingMessages().size();
            long resolvedMessages = receiverSupportMessageService.getResolvedMessages().size();
            long highPriorityMessages = receiverSupportMessageService.getHighPriorityPendingMessages().size();
            
            System.out.println("Total receiver messages: " + totalMessages);
            System.out.println("Pending receiver messages: " + pendingMessages);
            System.out.println("Resolved receiver messages: " + resolvedMessages);
            System.out.println("High priority pending receiver messages: " + highPriorityMessages);
            
            response.put("success", true);
            response.put("totalMessages", totalMessages);
            response.put("pendingMessages", pendingMessages);
            response.put("resolvedMessages", resolvedMessages);
            response.put("highPriorityMessages", highPriorityMessages);
            
        } catch (Exception e) {
            System.err.println("Error fetching receiver message stats: " + e.getMessage());
            e.printStackTrace();
            response.put("success", false);
            response.put("message", "Error fetching receiver message stats: " + e.getMessage());
        }
        return response;
    }

    @PostMapping("/support/receiver-messages/{messageId}/reply")
    @ResponseBody
    public Map<String, Object> replyToReceiverMessage(@PathVariable Long messageId, 
                                                    @RequestParam String replyText) {
        Map<String, Object> response = new HashMap<>();
        try {
            System.out.println("=== Replying to Receiver Message ===");
            System.out.println("Message ID: " + messageId);
            System.out.println("Reply Text: " + replyText);
            
            com.blooddonation.model.ReceiverSupportMessage updatedMessage = 
                receiverSupportMessageService.replyToMessage(messageId, replyText);
            
            System.out.println("✅ Reply added successfully to receiver message ID: " + messageId);
            
            response.put("success", true);
            response.put("message", "Reply sent successfully");
            response.put("updatedMessage", updatedMessage);
            
        } catch (Exception e) {
            System.err.println("Error replying to receiver message: " + e.getMessage());
            e.printStackTrace();
            response.put("success", false);
            response.put("message", "Error sending reply: " + e.getMessage());
        }
        return response;
    }

    @PostMapping("/support/receiver-messages/{messageId}/delete")
    @ResponseBody
    public Map<String, Object> deleteReceiverMessage(@PathVariable Long messageId) {
        Map<String, Object> response = new HashMap<>();
        try {
            System.out.println("=== Deleting Receiver Message ===");
            System.out.println("Message ID to delete: " + messageId);
            
            receiverSupportMessageService.deleteMessage(messageId);
            
            System.out.println("✅ Receiver message deleted successfully");
            response.put("success", true);
            response.put("message", "Message deleted successfully");
            
        } catch (Exception e) {
            System.err.println("Error deleting receiver message: " + e.getMessage());
            e.printStackTrace();
            response.put("success", false);
            response.put("message", "Error deleting message: " + e.getMessage());
        }
        return response;
    }

    @GetMapping("/hospital/appointments")
    @ResponseBody
    public String getHospitalAppointments() {
        StringBuilder result = new StringBuilder();
        result.append("=== Hospital Appointments from Database ===\n\n");
        
        try {
            // Fetch real appointment data from database
            List<com.blooddonation.model.Appointment> appointments = appointmentRepository.findAll();
            
            result.append("📅 Total Appointments: ").append(appointments.size()).append("\n\n");
            
            if (appointments.isEmpty()) {
                result.append("❌ No appointments found in database!\n");
                result.append("💡 The Appointments table exists but is empty.\n");
                return result.toString();
            }
            
            result.append("📋 ALL Appointment Details from Appointments Table:\n");
            result.append("===============================================\n");
            
            for (com.blooddonation.model.Appointment appointment : appointments) {
                result.append("🔹 APPOINTMENT DETAILS:\n");
                result.append("   Appointment ID: ").append(appointment.getAppointmentId()).append("\n");
                result.append("   Donor NIC: ").append(appointment.getDonorNic()).append("\n");
                result.append("   Slot ID: ").append(appointment.getSlotId()).append("\n");
                result.append("   Booking Date: ").append(appointment.getBookingDate()).append("\n");
                result.append("   Booking Time: ").append(appointment.getBookingTime()).append("\n");
                result.append("   -------------------------------------\n\n");
            }
            
            result.append("\n🔍 Database Schema Information:\n");
            result.append("==============================\n");
            result.append("Table Name: Appointments\n");
            result.append("Columns Found:\n");
            result.append("  - AppointmentID (Primary Key)\n");
            result.append("  - DONORNIC (Donor National ID)\n");
            result.append("  - SlotID (Appointment Slot Reference)\n");
            result.append("  - BookingDate (Auto-generated)\n");
            result.append("  - BookingTime (Auto-generated)\n");
            result.append("Total Records: ").append(appointments.size()).append("\n");
            
        } catch (Exception e) {
            result.append("❌ Error fetching appointments from database: ").append(e.getMessage()).append("\n");
            result.append("🔍 Error details: ").append(e.getClass().getSimpleName()).append("\n");
            e.printStackTrace();
        }
        
        return result.toString();
    }

    @GetMapping("/hospital/appointments/json")
    @ResponseBody
    public List<Map<String, Object>> getHospitalAppointmentsJson() {
        try {
            // Fetch real appointment data from database
            List<com.blooddonation.model.Appointment> appointments = appointmentRepository.findAll();
            
            System.out.println("=== Fetching Appointments from Database ===");
            System.out.println("Total appointments found: " + appointments.size());
            
            // Convert to JSON format for frontend
            List<Map<String, Object>> result = new ArrayList<>();
            
            for (com.blooddonation.model.Appointment appointment : appointments) {
                System.out.println("Processing appointment: " + appointment.getAppointmentId());
                System.out.println("  Donor NIC: " + appointment.getDonorNic());
                System.out.println("  Slot ID: " + appointment.getSlotId());
                System.out.println("  Booking Date: " + appointment.getBookingDate());
                System.out.println("  Booking Time: " + appointment.getBookingTime());
                
                Map<String, Object> appointmentData = new HashMap<>();
                appointmentData.put("id", appointment.getAppointmentId()); // Add id field for frontend
                appointmentData.put("appointmentId", appointment.getAppointmentId());
                appointmentData.put("donorNic", appointment.getDonorNic());
                appointmentData.put("slotId", appointment.getSlotId());
                
                // Format dates properly for display
                if (appointment.getBookingDate() != null) {
                    appointmentData.put("bookingDate", appointment.getBookingDate().toString());
                    appointmentData.put("date", appointment.getBookingDate().toLocalDate().toString());
                } else {
                    appointmentData.put("bookingDate", "N/A");
                    appointmentData.put("date", "N/A");
                }
                
                if (appointment.getBookingTime() != null) {
                    appointmentData.put("bookingTime", appointment.getBookingTime().toString());
                    appointmentData.put("time", appointment.getBookingTime().toString());
                } else {
                    appointmentData.put("bookingTime", "N/A");
                    appointmentData.put("time", "N/A");
                }
                
                // Get slot information from Appointment_Slots table
                String slotLocation = "Unknown Location";
                String slotDate = "N/A";
                String slotTime = "N/A";
                try {
                    if (appointment.getSlotId() != null) {
                        Optional<com.blooddonation.model.AppointmentSlot> slot = appointmentSlotRepository.findById(appointment.getSlotId());
                        if (slot.isPresent()) {
                            com.blooddonation.model.AppointmentSlot slotData = slot.get();
                            slotLocation = slotData.getLocation() != null ? slotData.getLocation() : "Unknown Location";
                            slotDate = slotData.getSlotDate() != null ? slotData.getSlotDate().toString() : "N/A";
                            slotTime = slotData.getSlotTime() != null ? slotData.getSlotTime() : "N/A";
                            
                            System.out.println("  Slot Date: " + slotDate);
                            System.out.println("  Slot Time: " + slotTime);
                            System.out.println("  Location: " + slotLocation);
                        }
                    }
                } catch (Exception e) {
                    System.err.println("Error fetching slot information for SlotID " + appointment.getSlotId() + ": " + e.getMessage());
                }
                
                // Add slot date and time to appointment data
                appointmentData.put("slotDate", slotDate);
                appointmentData.put("slotTime", slotTime);
                
                // Add display data with better formatting
                appointmentData.put("donorName", "Donor " + appointment.getDonorNic());
                appointmentData.put("bloodType", "Unknown"); // Would need to join with Donor table
                appointmentData.put("status", "Scheduled");
                appointmentData.put("doctor", "Dr. Staff");
                appointmentData.put("slotLocation", slotLocation);
                
                // Add formatted display fields
                appointmentData.put("formattedDate", appointment.getBookingDate() != null ? 
                    appointment.getBookingDate().toLocalDate().toString() : "N/A");
                appointmentData.put("formattedTime", appointment.getBookingTime() != null ? 
                    appointment.getBookingTime().toString() : "N/A");
                
                result.add(appointmentData);
            }
            
            System.out.println("Returning " + result.size() + " appointments to frontend");
            System.out.println("Sample data: " + (result.isEmpty() ? "No data" : result.get(0)));
            return result;
            
        } catch (Exception e) {
            System.err.println("Error fetching appointments: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    @GetMapping("/staff/logout")
    public String staffLogout(HttpSession session) {
        // Clear session attributes
        session.removeAttribute("systemUser");
        session.removeAttribute("userRole");
        session.removeAttribute("userFullName");
        session.invalidate();
        
        return "redirect:/staff/login";
    }

    @GetMapping("/hospital/create-test-appointments")
    @ResponseBody
    public String createTestAppointments() {
        StringBuilder result = new StringBuilder();
        result.append("=== Creating Test Appointments ===\n\n");
        
        try {
            // Create multiple test appointments
            String[] testNICs = {"991234567V", "992345678V", "993456789V", "994567890V", "995678901V"};
            Long[] testSlotIds = {1L, 2L, 3L, 4L, 5L};
            
            for (int i = 0; i < testNICs.length; i++) {
                com.blooddonation.model.Appointment appointment = new com.blooddonation.model.Appointment();
                appointment.setDonorNic(testNICs[i]);
                appointment.setSlotId(testSlotIds[i]);
                
                com.blooddonation.model.Appointment savedAppointment = appointmentRepository.save(appointment);
                result.append("✅ Created Appointment ").append(i + 1).append(":\n");
                result.append("   ID: ").append(savedAppointment.getAppointmentId()).append("\n");
                result.append("   Donor NIC: ").append(savedAppointment.getDonorNic()).append("\n");
                result.append("   Slot ID: ").append(savedAppointment.getSlotId()).append("\n");
                result.append("   Booking Date: ").append(savedAppointment.getBookingDate()).append("\n");
                result.append("   Booking Time: ").append(savedAppointment.getBookingTime()).append("\n\n");
            }
            
            result.append("🎉 Successfully created ").append(testNICs.length).append(" test appointments!\n");
            result.append("🔄 Now go back to the Hospital Staff Dashboard and click 'Donor Appointments' to see them!\n");
            
        } catch (Exception e) {
            result.append("❌ Error creating test appointments: ").append(e.getMessage()).append("\n");
            e.printStackTrace();
        }
        
        return result.toString();
    }

    @GetMapping("/test/database")
    @ResponseBody
    public String testDatabase() {
        StringBuilder result = new StringBuilder();
        result.append("=== Testing All System Users ===\n\n");
        
        // Test all users
        String[][] testUsers = {
            {"ADM1", "admpw123", "Admin"},
            {"DOC1", "doctor123", "Doctor"},
            {"HOS1", "hospw123", "Hospital Staff"},
            {"SUP1", "support123", "Customer Support"}
        };
        
        for (String[] user : testUsers) {
            String userId = user[0];
            String password = user[1];
            
            try {
                Optional<SystemUser> authUser = systemUserService.authenticateUser(userId, password);
                if (authUser.isPresent()) {
                    SystemUser u = authUser.get();
                    result.append("✅ ").append(userId).append(": SUCCESS\n");
                    result.append("   Username: ").append(u.getUsername()).append("\n");
                    result.append("   Role: ").append(u.getRole()).append("\n");
                    result.append("   Active: ").append(u.getIsActive()).append("\n\n");
                } else {
                    result.append("❌ ").append(userId).append(": FAILED - User not found or inactive\n\n");
                }
            } catch (Exception e) {
                result.append("❌ ").append(userId).append(": ERROR - ").append(e.getMessage()).append("\n\n");
            }
        }
        
        return result.toString();
    }

    @GetMapping("/test/connection")
    @ResponseBody
    public String testDatabaseConnection() {
        try {
            List<SystemUser> users = systemUserService.getAllUsers();
            return "Database connection successful! Found " + users.size() + " users.";
        } catch (Exception e) {
            return "Database connection failed: " + e.getMessage();
        }
    }

    @GetMapping("/test/login")
    @ResponseBody
    public String testLogin() {
        StringBuilder result = new StringBuilder();
        result.append("=== Complete Login Test ===\n\n");
        
        try {
            // First, let's see what users exist in the database
            List<SystemUser> allUsers = systemUserService.getAllUsers();
            result.append("📊 Total users in database: ").append(allUsers.size()).append("\n\n");
            
            if (allUsers.isEmpty()) {
                result.append("❌ No users found in database!\n");
                return result.toString();
            }
            
            // Show all users first
            result.append("👥 All Users in Database:\n");
            result.append("========================\n");
            for (SystemUser user : allUsers) {
                result.append("ID: ").append(user.getUserId())
                      .append(" | Role: '").append(user.getRole()).append("'")
                      .append(" | Active: ").append(user.getIsActive())
                      .append(" | Name: ").append(user.getFullName()).append("\n");
            }
            result.append("\n");
            
            // Test specific logins
            String[][] testUsers = {
                {"ADM1", "admpw123"},
                {"DOC1", "docpw123"},
                {"HOS1", "hospw123"}
            };
            
            for (String[] testUser : testUsers) {
                String userId = testUser[0];
                String password = testUser[1];
                
                result.append("🔍 Testing ").append(userId).append(" login:\n");
                
                // Try to find user first
                Optional<SystemUser> userOpt = systemUserService.findByUserId(userId);
                if (userOpt.isPresent()) {
                    SystemUser foundUser = userOpt.get();
                    result.append("   ✅ User found: ").append(foundUser.getUsername()).append("\n");
                    result.append("   🎭 Role: '").append(foundUser.getRole()).append("'\n");
                    result.append("   🔑 Stored Password: '").append(foundUser.getPassword()).append("'\n");
                    result.append("   🔑 Input Password: '").append(password).append("'\n");
                    result.append("   🟢 Is Active: ").append(foundUser.getIsActive()).append("\n");
                    
                    // Test authentication
                    Optional<SystemUser> authResult = systemUserService.authenticateUser(userId, password);
                    if (authResult.isPresent()) {
                        result.append("   🎉 Authentication: SUCCESS!\n");
                    } else {
                        result.append("   ❌ Authentication: FAILED!\n");
                    }
                } else {
                    result.append("   ❌ User not found!\n");
                }
                result.append("\n");
            }
            
        } catch (Exception e) {
            result.append("❌ Test failed: ").append(e.getMessage()).append("\n");
            e.printStackTrace();
        }
        
        return result.toString();
    }

    @GetMapping("/read/systemusers")
    @ResponseBody
    public String readSystemUserTable() {
        StringBuilder result = new StringBuilder();
        result.append("=== SystemUser Table Contents ===\n\n");
        
        try {
            List<SystemUser> allUsers = systemUserService.getAllUsers();
            result.append("Total Users: ").append(allUsers.size()).append("\n\n");
            
            if (allUsers.isEmpty()) {
                result.append("❌ No users found in SystemUser table!\n");
                return result.toString();
            }
            
            result.append("📋 All Users in SystemUser Table:\n");
            result.append("=====================================\n");
            
            for (SystemUser user : allUsers) {
                result.append("User ID: ").append(user.getUserId()).append("\n");
                result.append("Username: ").append(user.getUsername()).append("\n");
                result.append("Password: ").append(user.getPassword()).append("\n");
                result.append("Role: '").append(user.getRole()).append("'\n");
                result.append("Email: ").append(user.getEmail()).append("\n");
                result.append("Full Name: ").append(user.getFullName()).append("\n");
                result.append("Department: ").append(user.getDepartment()).append("\n");
                result.append("NIC: ").append(user.getNic()).append("\n");
                result.append("Is Active: ").append(user.getIsActive()).append("\n");
                result.append("Created At: ").append(user.getCreatedAt()).append("\n");
                result.append("-------------------------------------\n\n");
            }
            
            result.append("\n🔑 Login Credentials Summary:\n");
            result.append("=============================\n");
            for (SystemUser user : allUsers) {
                if ("Active".equals(user.getIsActive())) {
                    result.append("✅ ").append(user.getUserId()).append(" / ").append(user.getPassword())
                          .append(" → '").append(user.getRole()).append("'")
                          .append(" (").append(user.getFullName()).append(")\n");
                } else {
                    result.append("❌ ").append(user.getUserId()).append(" / ").append(user.getPassword())
                          .append(" → '").append(user.getRole()).append("'")
                          .append(" (").append(user.getFullName()).append(") [INACTIVE]\n");
                }
            }
            
        } catch (Exception e) {
            result.append("❌ Error reading SystemUser table: ").append(e.getMessage()).append("\n");
            e.printStackTrace();
        }
        
        return result.toString();
    }

    // Appointment Slots API endpoints
    @GetMapping("/admin/appointment-slots")
    @ResponseBody
    public List<com.blooddonation.model.AppointmentSlot> getAllAppointmentSlots() {
        try {
            System.out.println("🔍 HomeController.getAllAppointmentSlots() called");
            List<com.blooddonation.model.AppointmentSlot> slots = appointmentSlotService.getAllSlots();
            System.out.println("📊 Service returned " + slots.size() + " slots");
            return slots;
        } catch (Exception e) {
            System.out.println("❌ Error in getAllAppointmentSlots(): " + e.getMessage());
            e.printStackTrace();
            return List.of(); // Return empty list on error
        }
    }

    @PostMapping("/admin/appointment-slots")
    @ResponseBody
    public Map<String, Object> addAppointmentSlot(@RequestBody Map<String, Object> slotData) {
        Map<String, Object> response = new HashMap<>();
        try {
            com.blooddonation.model.AppointmentSlot slot = new com.blooddonation.model.AppointmentSlot();
            slot.setSlotDate(java.time.LocalDate.parse((String) slotData.get("slotDate")));
            
            // Handle time range - combine start and end time into slotTime
            String startTime = (String) slotData.get("startTime");
            String endTime = (String) slotData.get("endTime");
            if (startTime != null && endTime != null) {
                slot.setSlotTime(startTime + " - " + endTime);
            } else {
                // Fallback to single time for backward compatibility
                slot.setSlotTime((String) slotData.get("slotTime"));
            }
            
            slot.setLocation((String) slotData.get("location"));
            slot.setMaxDonors((Integer) slotData.get("maxDonors"));
            slot.setAvailableDonors((Integer) slotData.get("availableDonors"));
            
            appointmentSlotRepository.save(slot);
            
            response.put("success", true);
            response.put("message", "Appointment slot added successfully");
            response.put("slotId", slot.getSlotId());
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error adding appointment slot: " + e.getMessage());
        }
        return response;
    }

    @DeleteMapping("/admin/appointment-slots/{slotId}")
    @ResponseBody
    public Map<String, Object> deleteAppointmentSlot(@PathVariable Long slotId) {
        Map<String, Object> response = new HashMap<>();
        try {
            appointmentSlotRepository.deleteById(slotId);
            response.put("success", true);
            response.put("message", "Appointment slot deleted successfully");
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error deleting appointment slot: " + e.getMessage());
        }
        return response;
    }

    // System Users API endpoints
    @GetMapping("/admin/system-users")
    @ResponseBody
    public List<SystemUser> getAllSystemUsers() {
        return systemUserService.getAllUsers();
    }

    @PostMapping("/admin/system-users")
    @ResponseBody
    public Map<String, Object> addSystemUser(@RequestBody Map<String, Object> userData) {
        Map<String, Object> response = new HashMap<>();
        try {
            SystemUser user = new SystemUser();
            user.setUserId((String) userData.get("userId"));
            user.setUsername((String) userData.get("username"));
            user.setPassword((String) userData.get("password"));
            user.setRole((String) userData.get("role"));
            user.setEmail((String) userData.get("email"));
            user.setDepartment((String) userData.get("department"));
            user.setNic((String) userData.get("nic"));
            user.setIsActive((String) userData.getOrDefault("isActive", "Active"));
            
            SystemUser savedUser = systemUserService.saveUser(user);
            
            response.put("success", true);
            response.put("message", "System user added successfully");
            response.put("userId", savedUser.getUserId());
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error adding system user: " + e.getMessage());
        }
        return response;
    }

    @DeleteMapping("/admin/system-users/{userId}")
    @ResponseBody
    public Map<String, Object> deleteSystemUser(@PathVariable String userId) {
        Map<String, Object> response = new HashMap<>();
        try {
            systemUserService.deleteUser(userId);
            response.put("success", true);
            response.put("message", "System user deleted successfully");
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error deleting system user: " + e.getMessage());
        }
        return response;
    }

    // Donor Management API endpoints
    @GetMapping("/admin/donors")
    @ResponseBody
    public List<Donor> getAllDonors() {
        return donorService.getAllDonors();
    }

    @GetMapping("/admin/donors/{nic}")
    @ResponseBody
    public Map<String, Object> getDonorByNic(@PathVariable String nic) {
        Map<String, Object> response = new HashMap<>();
        try {
            Optional<Donor> donor = donorService.getDonorByNic(nic);
            if (donor.isPresent()) {
                response.put("success", true);
                response.put("donor", donor.get());
            } else {
                response.put("success", false);
                response.put("message", "Donor not found");
            }
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error getting donor: " + e.getMessage());
        }
        return response;
    }

    @PutMapping("/admin/donors/{nic}")
    @ResponseBody
    public Map<String, Object> updateDonor(@PathVariable String nic, @RequestBody Map<String, Object> donorData) {
        Map<String, Object> response = new HashMap<>();
        try {
            Optional<Donor> existingDonorOpt = donorService.getDonorByNic(nic);
            if (!existingDonorOpt.isPresent()) {
                response.put("success", false);
                response.put("message", "Donor not found");
                return response;
            }

            Donor donor = existingDonorOpt.get();
            donor.setFullName((String) donorData.get("fullName"));
            donor.setDateOfBirth(java.time.LocalDate.parse((String) donorData.get("dateOfBirth")));
            donor.setGender((String) donorData.get("gender"));
            donor.setBloodType((String) donorData.get("bloodType"));
            donor.setContactNumber((String) donorData.get("contactNumber"));
            donor.setEmail((String) donorData.get("email"));
            donor.setAddress((String) donorData.get("address"));
            
            // Update age based on date of birth
            donor.setAge(java.time.LocalDate.now().getYear() - donor.getDateOfBirth().getYear());

            Donor updatedDonor = donorService.updateDonor(donor);
            response.put("success", true);
            response.put("message", "Donor updated successfully");
            response.put("donor", updatedDonor);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error updating donor: " + e.getMessage());
        }
        return response;
    }

    @DeleteMapping("/admin/donors/{nic}")
    @ResponseBody
    public Map<String, Object> deleteDonor(@PathVariable String nic) {
        Map<String, Object> response = new HashMap<>();
        try {
            donorService.deleteDonor(nic);
            response.put("success", true);
            response.put("message", "Donor deleted successfully");
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error deleting donor: " + e.getMessage());
        }
        return response;
    }

    // Health Assessment API endpoints for Doctor Dashboard
    @GetMapping("/doctor/health-assessments")
    @ResponseBody
    public List<HealthAssessment> getAllHealthAssessments() {
        try {
            System.out.println("=== Getting all health assessments for doctor ===");
            List<HealthAssessment> assessments = healthAssessmentService.getAllAssessments();
            System.out.println("📊 Found " + assessments.size() + " health assessments");
            return assessments;
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }

    @GetMapping("/doctor/health-assessments/pending")
    @ResponseBody
    public List<HealthAssessment> getPendingHealthAssessments() {
        try {
            System.out.println("=== Getting pending health assessments for doctor ===");
            List<HealthAssessment> assessments = healthAssessmentService.getPendingAssessments();
            System.out.println("📊 Found " + assessments.size() + " pending health assessments");
            return assessments;
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }

    @GetMapping("/doctor/health-assessments/{id}")
    @ResponseBody
    public ApiResponse getHealthAssessmentById(@PathVariable Long id) {
        try {
            System.out.println("=== Getting health assessment by ID: " + id + " ===");
            Optional<HealthAssessment> assessment = healthAssessmentService.getAssessmentById(id);
            
            if (assessment.isPresent()) {
                System.out.println("✅ Found health assessment: " + assessment.get().getDonorNic());
                return new ApiResponse(true, "Health assessment retrieved successfully", assessment.get());
            } else {
                System.out.println("❌ Health assessment not found with ID: " + id);
                return new ApiResponse(false, "Health assessment not found", null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ApiResponse(false, "Error retrieving health assessment: " + e.getMessage(), null);
        }
    }

    @PostMapping("/doctor/health-assessments/{assessmentId}/approve")
    @ResponseBody
    public Map<String, Object> approveHealthAssessment(@PathVariable Long assessmentId, 
                                                       @RequestBody Map<String, Object> requestData,
                                                       HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        try {
            System.out.println("=== Approving health assessment: " + assessmentId + " ===");
            
            String doctorNotes = (String) requestData.get("doctorNotes");
            String approvedBy = (String) session.getAttribute("userFullName");
            
            if (approvedBy == null) {
                approvedBy = "Doctor";
            }
            
            HealthAssessment assessment = healthAssessmentService.approveAssessment(assessmentId, doctorNotes, approvedBy);
            
            if (assessment != null) {
                System.out.println("✅ Health assessment approved successfully");
                
                // Send notification to donor
                sendNotificationToDonor(assessment.getDonorNic(), "APPROVED", doctorNotes);
                
                response.put("success", true);
                response.put("message", "Health assessment approved successfully");
                response.put("assessment", assessment);
            } else {
                response.put("success", false);
                response.put("message", "Health assessment not found");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.put("success", false);
            response.put("message", "Error approving health assessment: " + e.getMessage());
        }
        return response;
    }

    @PostMapping("/doctor/health-assessments/{assessmentId}/reject")
    @ResponseBody
    public Map<String, Object> rejectHealthAssessment(@PathVariable Long assessmentId, 
                                                      @RequestBody Map<String, Object> requestData,
                                                      HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        try {
            System.out.println("=== Rejecting health assessment: " + assessmentId + " ===");
            
            String doctorNotes = (String) requestData.get("doctorNotes");
            String approvedBy = (String) session.getAttribute("userFullName");
            
            if (approvedBy == null) {
                approvedBy = "Doctor";
            }
            
            HealthAssessment assessment = healthAssessmentService.rejectAssessment(assessmentId, doctorNotes, approvedBy);
            
            if (assessment != null) {
                System.out.println("❌ Health assessment rejected");
                
                // Send notification to donor
                sendNotificationToDonor(assessment.getDonorNic(), "REJECTED", doctorNotes);
                
                response.put("success", true);
                response.put("message", "Health assessment rejected");
                response.put("assessment", assessment);
            } else {
                response.put("success", false);
                response.put("message", "Health assessment not found");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.put("success", false);
            response.put("message", "Error rejecting health assessment: " + e.getMessage());
        }
        return response;
    }

    @DeleteMapping("/doctor/health-assessments/{assessmentId}")
    @ResponseBody
    public Map<String, Object> deleteHealthAssessment(@PathVariable Long assessmentId, HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        try {
            System.out.println("=== Deleting health assessment: " + assessmentId + " ===");
            
            Optional<HealthAssessment> assessmentOpt = healthAssessmentService.getAssessmentById(assessmentId);
            if (assessmentOpt.isPresent()) {
                HealthAssessment assessment = assessmentOpt.get();
                
                // Delete the assessment
                healthAssessmentService.deleteAssessment(assessmentId);
                
                System.out.println("🗑️ Health assessment deleted successfully");
                
                // Send notification to donor about deletion
                String deletedBy = (String) session.getAttribute("userFullName");
                if (deletedBy == null) {
                    deletedBy = "Doctor";
                }
                
                sendNotificationToDonor(assessment.getDonorNic(), "DELETED", 
                    "Your health assessment has been removed from the system by " + deletedBy + ".");
                
                response.put("success", true);
                response.put("message", "Health assessment deleted successfully");
            } else {
                response.put("success", false);
                response.put("message", "Health assessment not found");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.put("success", false);
            response.put("message", "Error deleting health assessment: " + e.getMessage());
        }
        return response;
    }

    // API endpoint to get donor count for admin dashboard
    @GetMapping("/admin/donor-count")
    @ResponseBody
    public Map<String, Object> getDonorCount() {
        Map<String, Object> response = new HashMap<>();
        try {
            System.out.println("=== Getting donor count for admin dashboard ===");
            long donorCount = donorService.getAllDonors().size();
            System.out.println("📊 Total donors in database: " + donorCount);
            
            response.put("success", true);
            response.put("count", donorCount);
            return response;
        } catch (Exception e) {
            e.printStackTrace();
            response.put("success", false);
            response.put("message", "Error getting donor count: " + e.getMessage());
            response.put("count", 0);
            return response;
        }
    }

    // Receiver Management API endpoints
    @GetMapping("/admin/receivers")
    @ResponseBody
    public List<Receiver> getAllReceivers() {
        return receiverService.findAll();
    }

    @GetMapping("/admin/receivers/{nic}")
    @ResponseBody
    public Map<String, Object> getReceiverByNic(@PathVariable String nic) {
        Map<String, Object> response = new HashMap<>();
        try {
            Optional<Receiver> receiver = receiverService.findByNic(nic);
            if (receiver.isPresent()) {
                response.put("success", true);
                response.put("receiver", receiver.get());
            } else {
                response.put("success", false);
                response.put("message", "Receiver not found");
            }
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error getting receiver: " + e.getMessage());
        }
        return response;
    }

    @DeleteMapping("/admin/receivers/{nic}")
    @ResponseBody
    public Map<String, Object> deleteReceiver(@PathVariable String nic) {
        Map<String, Object> response = new HashMap<>();
        try {
            receiverService.deleteReceiver(nic);
            response.put("success", true);
            response.put("message", "Receiver deleted successfully");
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error deleting receiver: " + e.getMessage());
        }
        return response;
    }

    // API endpoint to get receiver count for admin dashboard
    @GetMapping("/admin/receiver-count")
    @ResponseBody
    public Map<String, Object> getReceiverCount() {
        Map<String, Object> response = new HashMap<>();
        try {
            System.out.println("=== Getting receiver count for admin dashboard ===");
            long receiverCount = receiverService.findAll().size();
            System.out.println("📊 Total receivers in database: " + receiverCount);
            
            response.put("success", true);
            response.put("count", receiverCount);
            return response;
        } catch (Exception e) {
            e.printStackTrace();
            response.put("success", false);
            response.put("message", "Error getting receiver count: " + e.getMessage());
            response.put("count", 0);
            return response;
        }
    }

    // API endpoint to get admin notifications from Observer Pattern
    @GetMapping("/admin/notifications")
    @ResponseBody
    public List<Map<String, Object>> getAdminNotifications(HttpSession session) {
        System.out.println("=== Getting admin notifications from Observer Pattern ===");
        
        // Check if admin/hospital staff is logged in
        Object systemUser = session.getAttribute("systemUser");
        if (systemUser == null) {
            System.out.println("❌ No user logged in");
            return new ArrayList<>();
        }
        
        // Get notifications from NotificationManager (Singleton)
        List<Map<String, Object>> notifications = 
            com.blooddonation.observer.NotificationManager.getInstance().getAdminNotifications();
        
        System.out.println("📬 Retrieved " + notifications.size() + " admin notifications");
        
        // Mark as read when fetched
        notifications.forEach(notification -> notification.put("read", true));
        
        return notifications;
    }
    
    // API endpoint to clear admin notifications
    @PostMapping("/admin/notifications/clear")
    @ResponseBody
    public Map<String, Object> clearAdminNotifications(HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        
        // Check if admin/hospital staff is logged in
        Object systemUser = session.getAttribute("systemUser");
        if (systemUser == null) {
            response.put("success", false);
            response.put("message", "Unauthorized");
            return response;
        }
        
        try {
            com.blooddonation.observer.NotificationManager.getInstance().clearAdminNotifications();
            response.put("success", true);
            response.put("message", "Notifications cleared successfully");
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error clearing notifications: " + e.getMessage());
        }
        
        return response;
    }

    // Blood Inventory Management endpoints
    @PostMapping("/hospital/blood-inventory")
    @ResponseBody
    public Map<String, Object> addBloodInventory(@RequestParam String bloodType,
                                                @RequestParam Integer quantity,
                                                @RequestParam String collectionDate,
                                                @RequestParam String expiryDate,
                                                @RequestParam String status) {
        Map<String, Object> response = new HashMap<>();
        try {
            System.out.println("=== Adding Blood Inventory ===");
            System.out.println("Blood Type: " + bloodType);
            System.out.println("Quantity: " + quantity);
            System.out.println("Collection Date: " + collectionDate);
            System.out.println("Expiry Date: " + expiryDate);
            System.out.println("Status: " + status);

            // Validate dates
            LocalDate collection = LocalDate.parse(collectionDate);
            LocalDate expiry = LocalDate.parse(expiryDate);
            
            if (expiry.isBefore(collection) || expiry.isEqual(collection)) {
                response.put("success", false);
                response.put("message", "Expiry date must be after collection date");
                return response;
            }

            // Create BloodInventory object
            BloodInventory bloodInventory = new BloodInventory();
            bloodInventory.setBloodType(bloodType);
            bloodInventory.setQuantity(quantity);
            bloodInventory.setCollectionDate(collection);
            bloodInventory.setExpiryDate(expiry);
            bloodInventory.setStatus(status);

            // Save to database
            BloodInventory savedInventory = bloodInventoryService.saveBloodInventory(bloodInventory);
            
            System.out.println("✅ Blood inventory saved successfully with ID: " + savedInventory.getInventoryId());

            response.put("success", true);
            response.put("message", "Blood inventory added successfully");
            response.put("inventoryId", savedInventory.getInventoryId());
            response.put("data", savedInventory);
            
            return response;
        } catch (Exception e) {
            System.err.println("❌ Error adding blood inventory: " + e.getMessage());
            e.printStackTrace();
            response.put("success", false);
            response.put("message", "Error adding blood inventory: " + e.getMessage());
            return response;
        }
    }

    @GetMapping("/hospital/blood-inventory")
    @ResponseBody
    public Map<String, Object> getBloodInventory() {
        Map<String, Object> response = new HashMap<>();
        try {
            List<BloodInventory> inventory = bloodInventoryService.findAll();
            Map<String, Integer> availableBlood = bloodInventoryService.getAvailableBloodTypesWithQuantities();
            
            response.put("success", true);
            response.put("inventory", inventory);
            response.put("availableBlood", availableBlood);
            
            return response;
        } catch (Exception e) {
            System.err.println("❌ Error getting blood inventory: " + e.getMessage());
            e.printStackTrace();
            response.put("success", false);
            response.put("message", "Error getting blood inventory: " + e.getMessage());
            return response;
        }
    }

    @GetMapping("/hospital/blood-inventory/summary")
    @ResponseBody
    public Map<String, Object> getBloodInventorySummary() {
        Map<String, Object> response = new HashMap<>();
        try {
            Map<String, Integer> availableBlood = bloodInventoryService.getAvailableBloodTypesWithQuantities();
            List<BloodInventory> expiringSoon = bloodInventoryService.findBloodExpiringSoon();
            List<BloodInventory> expired = bloodInventoryService.findExpiredBlood();
            
            response.put("success", true);
            response.put("availableBlood", availableBlood);
            response.put("expiringSoon", expiringSoon);
            response.put("expired", expired);
            
            return response;
        } catch (Exception e) {
            System.err.println("❌ Error getting blood inventory summary: " + e.getMessage());
            e.printStackTrace();
            response.put("success", false);
            response.put("message", "Error getting blood inventory summary: " + e.getMessage());
            return response;
        }
    }

    @GetMapping("/receiver/blood-inventory/summary")
    @ResponseBody
    public Map<String, Object> getReceiverBloodInventorySummary(HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        try {
            // Check if receiver is logged in
            Object receiver = session.getAttribute("receiver");
            if (receiver == null) {
                response.put("success", false);
                response.put("message", "Receiver not logged in");
                return response;
            }

            Map<String, Integer> availableBlood = bloodInventoryService.getAvailableBloodTypesWithQuantities();
            List<BloodInventory> expiringSoon = bloodInventoryService.findBloodExpiringSoon();
            List<BloodInventory> expired = bloodInventoryService.findExpiredBlood();
            
            response.put("success", true);
            response.put("availableBlood", availableBlood);
            response.put("expiringSoon", expiringSoon);
            response.put("expired", expired);
            
            return response;
        } catch (Exception e) {
            System.err.println("❌ Error getting receiver blood inventory summary: " + e.getMessage());
            e.printStackTrace();
            response.put("success", false);
            response.put("message", "Error getting blood inventory summary: " + e.getMessage());
            return response;
        }
    }

    @PostMapping("/hospital/blood-inventory/cleanup-expired")
    @ResponseBody
    public Map<String, Object> cleanupExpiredBloodInventory() {
        Map<String, Object> response = new HashMap<>();
        try {
            System.out.println("=== Cleaning up expired blood inventory ===");
            
            List<BloodInventory> expiredRecords = bloodInventoryService.findExpiredBlood();
            int deletedCount = 0;
            
            for (BloodInventory expiredRecord : expiredRecords) {
                bloodInventoryService.deleteById(expiredRecord.getInventoryId());
                deletedCount++;
                System.out.println("🗑️ Deleted expired record ID: " + expiredRecord.getInventoryId() + 
                                 " (Blood Type: " + expiredRecord.getBloodType() + 
                                 ", Expiry Date: " + expiredRecord.getExpiryDate() + ")");
            }
            
            System.out.println("✅ Cleanup completed. Deleted " + deletedCount + " expired records.");
            
            response.put("success", true);
            response.put("message", "Successfully cleaned up expired blood inventory");
            response.put("deletedCount", deletedCount);
            
            return response;
        } catch (Exception e) {
            System.err.println("❌ Error cleaning up expired blood inventory: " + e.getMessage());
            e.printStackTrace();
            response.put("success", false);
            response.put("message", "Error cleaning up expired blood inventory: " + e.getMessage());
            response.put("deletedCount", 0);
            return response;
        }
    }

    // Helper method to send notification to donor
    private void sendNotificationToDonor(String donorNic, String status, String doctorNotes) {
        try {
            System.out.println("📱 Sending notification to donor " + donorNic + " - Status: " + status);
            System.out.println("📝 Doctor Notes: " + doctorNotes);
            
            String message;
            if (status.equals("APPROVED")) {
                message = "✅ Your health assessment has been approved! You are eligible to donate blood.";
            } else if (status.equals("REJECTED")) {
                message = "❌ Your health assessment has been rejected. Please contact the medical team for more information.";
            } else if (status.equals("DELETED")) {
                message = "🗑️ Your health assessment has been removed from the system.";
            } else {
                message = "📋 Health assessment status updated.";
            }
            
            System.out.println("📨 Notification message: " + message);
            
            // Store notification in session for donor to see
            // In a real system, you'd store this in database or send email/SMS
            storeNotificationForDonor(donorNic, message, status, doctorNotes);
            
        } catch (Exception e) {
            System.err.println("❌ Error sending notification: " + e.getMessage());
        }
    }

    // Simple in-memory notification storage (for demo purposes)
    private static Map<String, List<Map<String, Object>>> donorNotifications = new HashMap<>();
    private static Map<String, List<Map<String, Object>>> receiverNotifications = new HashMap<>();
    
    private void storeNotificationForDonor(String donorNic, String message, String status, String doctorNotes) {
        Map<String, Object> notification = new HashMap<>();
        notification.put("message", message);
        notification.put("status", status);
        notification.put("doctorNotes", doctorNotes);
        notification.put("timestamp", LocalDateTime.now().toString());
        notification.put("read", false);
        
        donorNotifications.computeIfAbsent(donorNic, k -> new ArrayList<>()).add(notification);
        
        // Keep only last 10 notifications per donor
        List<Map<String, Object>> notifications = donorNotifications.get(donorNic);
        if (notifications.size() > 10) {
            notifications.remove(0);
        }
    }
    
    private void storeNotificationForReceiver(String receiverCnic, String message, String status, Long requestId) {
        Map<String, Object> notification = new HashMap<>();
        notification.put("message", message);
        notification.put("status", status);
        notification.put("requestId", requestId);
        notification.put("timestamp", LocalDateTime.now().toString());
        notification.put("read", false);
        
        receiverNotifications.computeIfAbsent(receiverCnic, k -> new ArrayList<>()).add(notification);
        
        // Keep only last 10 notifications per receiver
        List<Map<String, Object>> notifications = receiverNotifications.get(receiverCnic);
        if (notifications.size() > 10) {
            notifications.remove(0);
        }
    }
    
    // Helper method to send notification to receiver
    private void sendNotificationToReceiver(String receiverCnic, String status, String message, Long requestId) {
        try {
            System.out.println("📱 Sending notification to receiver " + receiverCnic + " - Status: " + status);
            System.out.println("📝 Message: " + message);
            System.out.println("📋 Request ID: " + requestId);
            
            // Store notification for receiver to see
            storeNotificationForReceiver(receiverCnic, message, status, requestId);
            
        } catch (Exception e) {
            System.err.println("❌ Error sending notification to receiver: " + e.getMessage());
        }
    }

    // API endpoint to get notifications for donor
    @GetMapping("/donor/notifications")
    @ResponseBody
    public List<Map<String, Object>> getDonorNotifications(HttpSession session) {
        Object donor = session.getAttribute("donor");
        if (donor instanceof Donor) {
            Donor donorObj = (Donor) donor;
            List<Map<String, Object>> notifications = donorNotifications.getOrDefault(donorObj.getNic(), new ArrayList<>());
            
            // Mark as read when fetched
            notifications.forEach(notification -> notification.put("read", true));
            
            return notifications;
        }
        return new ArrayList<>();
    }
    
    // API endpoint to get notifications for receiver
    @GetMapping("/receiver/notifications")
    @ResponseBody
    public List<Map<String, Object>> getReceiverNotifications(HttpSession session) {
        Object receiver = session.getAttribute("receiver");
        if (receiver instanceof Receiver) {
            Receiver receiverObj = (Receiver) receiver;
            List<Map<String, Object>> notifications = receiverNotifications.getOrDefault(receiverObj.getNic(), new ArrayList<>());
            
            // Mark as read when fetched
            notifications.forEach(notification -> notification.put("read", true));
            
            return notifications;
        }
        return new ArrayList<>();
    }

    // API endpoint to mark notification as read
    @PostMapping("/donor/notifications/mark-read")
    @ResponseBody
    public Map<String, Object> markNotificationAsRead(@RequestBody Map<String, Object> requestData, HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        try {
            Object donor = session.getAttribute("donor");
            if (donor instanceof Donor) {
                Donor donorObj = (Donor) donor;
                String timestamp = (String) requestData.get("timestamp");
                
                List<Map<String, Object>> notifications = donorNotifications.get(donorObj.getNic());
                if (notifications != null) {
                    notifications.stream()
                        .filter(notification -> timestamp.equals(notification.get("timestamp")))
                        .forEach(notification -> notification.put("read", true));
                }
                
                response.put("success", true);
                response.put("message", "Notification marked as read");
            } else {
                response.put("success", false);
                response.put("message", "Donor not found in session");
            }
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error marking notification as read: " + e.getMessage());
        }
        return response;
    }

    // Doctor Notes API endpoints
    @PostMapping("/api/doctor-notes")
    @ResponseBody
    public Map<String, Object> createDoctorNote(@RequestBody DoctorNote doctorNote, HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        try {
            System.out.println("=== Creating Doctor Note ===");
            System.out.println("Received Doctor Note: " + doctorNote);
            
            doctorNote.setCreatedAt(LocalDateTime.now());
            
            System.out.println("Saving Doctor Note...");
            DoctorNote savedNote = doctorNoteService.saveDoctorNote(doctorNote);
            System.out.println("✅ Doctor Note saved successfully with ID: " + savedNote.getNoteId());
            
            response.put("success", true);
            response.put("message", "Doctor note created successfully");
            response.put("note", savedNote);
        } catch (Exception e) {
            System.err.println("❌ Error creating doctor note: " + e.getMessage());
            e.printStackTrace();
            response.put("success", false);
            response.put("message", "Failed to create doctor note: " + e.getMessage());
        }
        return response;
    }

    @GetMapping("/api/doctor-notes")
    @ResponseBody
    public Map<String, Object> getAllDoctorNotes() {
        Map<String, Object> response = new HashMap<>();
        try {
            List<DoctorNote> notes = doctorNoteService.getAllDoctorNotes();
            response.put("success", true);
            response.put("message", "Doctor notes retrieved successfully");
            response.put("notes", notes);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Failed to retrieve doctor notes: " + e.getMessage());
        }
        return response;
    }

    // Duplicate method removed - using the one below with noteId parameter

    @GetMapping("/api/doctor-notes/donor/{donorNic}")
    @ResponseBody
    public Map<String, Object> getDoctorNotesByDonor(@PathVariable String donorNic) {
        Map<String, Object> response = new HashMap<>();
        try {
            List<DoctorNote> notes = doctorNoteService.getDoctorNotesByDonorNic(donorNic);
            response.put("success", true);
            response.put("message", "Doctor notes retrieved successfully");
            response.put("notes", notes);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Failed to retrieve doctor notes: " + e.getMessage());
        }
        return response;
    }

    @GetMapping("/api/doctor-notes/outcome/{outcome}")
    @ResponseBody
    public Map<String, Object> getDoctorNotesByOutcome(@PathVariable String outcome) {
        Map<String, Object> response = new HashMap<>();
        try {
            List<DoctorNote> notes = doctorNoteService.getDoctorNotesByOutcome(outcome);
            response.put("success", true);
            response.put("message", "Doctor notes retrieved successfully");
            response.put("notes", notes);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Failed to retrieve doctor notes: " + e.getMessage());
        }
        return response;
    }

    // Follow-up endpoint removed since these fields don't exist in the database

    @GetMapping("/api/doctor-notes/stats")
    @ResponseBody
    public Map<String, Object> getDoctorNotesStats() {
        Map<String, Object> response = new HashMap<>();
        try {
            Map<String, Object> stats = new HashMap<>();
            stats.put("totalNotes", doctorNoteService.getTotalNotesCount());
            stats.put("successfulNotes", doctorNoteService.getNotesCountByOutcome("Successful"));
            stats.put("deferredNotes", doctorNoteService.getNotesCountByOutcome("Deferred"));
            stats.put("incompleteNotes", doctorNoteService.getNotesCountByOutcome("Incomplete"));
            stats.put("adverseReactionNotes", doctorNoteService.getNotesCountByOutcome("Adverse Reaction"));
            stats.put("recentNotes", doctorNoteService.getRecentNotes());
            
            response.put("success", true);
            response.put("message", "Doctor notes statistics retrieved successfully");
            response.put("stats", stats);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Failed to retrieve doctor notes statistics: " + e.getMessage());
        }
        return response;
    }

    @GetMapping("/api/doctor-notes/{noteId}")
    @ResponseBody
    public Map<String, Object> getDoctorNoteById(@PathVariable Long noteId) {
        Map<String, Object> response = new HashMap<>();
        try {
            Optional<DoctorNote> note = doctorNoteService.getDoctorNoteById(noteId);
            if (note.isPresent()) {
                response.put("success", true);
                response.put("message", "Doctor note retrieved successfully");
                response.put("note", note.get());
            } else {
                response.put("success", false);
                response.put("message", "Doctor note not found");
            }
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Failed to retrieve doctor note: " + e.getMessage());
        }
        return response;
    }

    @PutMapping("/api/doctor-notes/{noteId}")
    @ResponseBody
    public Map<String, Object> updateDoctorNote(@PathVariable Long noteId, @RequestBody DoctorNote doctorNote) {
        Map<String, Object> response = new HashMap<>();
        try {
            Optional<DoctorNote> existingNote = doctorNoteService.getDoctorNoteById(noteId);
            if (existingNote.isPresent()) {
                doctorNote.setNoteId(noteId);
                DoctorNote updatedNote = doctorNoteService.updateDoctorNote(doctorNote);
                response.put("success", true);
                response.put("message", "Doctor note updated successfully");
                response.put("note", updatedNote);
            } else {
                response.put("success", false);
                response.put("message", "Doctor note not found");
            }
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Failed to update doctor note: " + e.getMessage());
        }
        return response;
    }

    @DeleteMapping("/api/doctor-notes/{noteId}")
    @ResponseBody
    public Map<String, Object> deleteDoctorNote(@PathVariable Long noteId) {
        Map<String, Object> response = new HashMap<>();
        try {
            doctorNoteService.deleteDoctorNote(noteId);
            response.put("success", true);
            response.put("message", "Doctor note deleted successfully");
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Failed to delete doctor note: " + e.getMessage());
        }
        return response;
    }

    @GetMapping("/donor-support")
    public String donorSupportPage(@RequestParam(required = false) String error,
                                 Model model, HttpSession session) {
        // Check if donor is logged in
        Object donor = session.getAttribute("donor");
        if (donor != null) {
            model.addAttribute("donor", donor);
            
            // Fetch message history for logged-in donor
            try {
                String donorNic = ((com.blooddonation.model.Donor) donor).getNic();
                System.out.println("=== Fetching Messages for Donor ===");
                System.out.println("Donor NIC: " + donorNic);
                
                List<com.blooddonation.model.DonorSupportMessage> messages = 
                    donorSupportMessageService.getMessagesByDonorNic(donorNic);
                
                System.out.println("Messages found: " + messages.size());
                for (com.blooddonation.model.DonorSupportMessage msg : messages) {
                    System.out.println("  - Message ID: " + msg.getMessageId() + 
                                     ", Subject: " + msg.getSubject() + 
                                     ", Priority: " + msg.getPriority() + 
                                     ", Has Reply: " + (msg.getReplyText() != null));
                }
                
                // Always show only the donor's own messages
                model.addAttribute("messages", messages);
            } catch (Exception e) {
                System.err.println("Error fetching donor messages: " + e.getMessage());
                e.printStackTrace();
                model.addAttribute("messages", new java.util.ArrayList<>());
            }
        } else {
            model.addAttribute("messages", new java.util.ArrayList<>());
        }
        
        // Handle error messages from redirects
        if ("delete_failed".equals(error)) {
            model.addAttribute("errorMessage", "Failed to delete message. Please try again.");
        }
        
        model.addAttribute("title", "BloodLife Customer Care");
        return "DonorSupportpage";
    }

    @PostMapping("/donor-support")
    public String submitDonorSupport(@RequestParam("nic") String nic,
                                   @RequestParam("fullName") String fullName,
                                   @RequestParam("email") String email,
                                   @RequestParam("priority") String priority,
                                   @RequestParam("subject") String subject,
                                   @RequestParam("message") String message,
                                   Model model,
                                   HttpSession session) {
        try {
            System.out.println("=== Submit Donor Support Message Debug ===");
            System.out.println("NIC: " + nic);
            System.out.println("Full Name: " + fullName);
            System.out.println("Email: " + email);
            System.out.println("Priority: " + priority);
            System.out.println("Subject: " + subject);
            System.out.println("Message: " + message);
            
            // Create and save the support message
            com.blooddonation.model.DonorSupportMessage supportMessage = new com.blooddonation.model.DonorSupportMessage();
            supportMessage.setDonorNic(nic);
            supportMessage.setDonorFullName(fullName);
            supportMessage.setDonorEmail(email);
            supportMessage.setSubject(subject);
            supportMessage.setMessageText(message);
            
            // Convert priority to proper enum format
            String priorityFormatted = priority.substring(0, 1).toUpperCase() + priority.substring(1).toLowerCase();
            System.out.println("Setting priority: " + priorityFormatted);
            
            try {
                supportMessage.setPriority(com.blooddonation.model.DonorSupportMessage.Priority.valueOf(priorityFormatted));
            } catch (IllegalArgumentException e) {
                System.err.println("❌ Invalid priority value: " + priority + " (formatted: " + priorityFormatted + ")");
                System.err.println("Valid priority values are: High, Medium, Low");
                throw new IllegalArgumentException("Invalid priority level: " + priority + ". Valid values are: high, medium, low");
            }
            
            System.out.println("Saving message to database...");
            com.blooddonation.model.DonorSupportMessage savedMessage = donorSupportMessageService.saveMessage(supportMessage);
            System.out.println("✅ Message saved successfully with ID: " + savedMessage.getMessageId());
            
            model.addAttribute("successMessage", "Your message has been received! Our support team will get back to you within 24 hours.");
            model.addAttribute("messageId", "MSG-" + savedMessage.getMessageId());
            
            // Check if donor is logged in to pre-fill form and fetch updated messages
            Object donor = session.getAttribute("donor");
            if (donor != null) {
                model.addAttribute("donor", donor);
                
                // Fetch updated message history
                try {
                    String donorNic = ((com.blooddonation.model.Donor) donor).getNic();
                    List<com.blooddonation.model.DonorSupportMessage> messages = 
                        donorSupportMessageService.getMessagesByDonorNic(donorNic);
                    model.addAttribute("messages", messages);
                } catch (Exception e) {
                    System.err.println("Error fetching donor messages: " + e.getMessage());
                    model.addAttribute("messages", new java.util.ArrayList<>());
                }
            } else {
                model.addAttribute("messages", new java.util.ArrayList<>());
            }
            
            model.addAttribute("title", "BloodLife Customer Care");
            return "DonorSupportpage";
            
        } catch (Exception e) {
            System.err.println("❌ Error saving support message: " + e.getMessage());
            System.err.println("Error type: " + e.getClass().getSimpleName());
            e.printStackTrace();
            
            // Provide more specific error message
            String errorMsg = "Failed to send your message. ";
            if (e.getMessage() != null) {
                if (e.getMessage().contains("Priority")) {
                    errorMsg += "Invalid priority level selected.";
                } else if (e.getMessage().contains("validation")) {
                    errorMsg += "Please check all required fields are filled correctly.";
                } else {
                    errorMsg += "Error: " + e.getMessage();
                }
            } else {
                errorMsg += "Please try again.";
            }
            
            model.addAttribute("errorMessage", errorMsg);
            
            // Check if donor is logged in to pre-fill form
            Object donor = session.getAttribute("donor");
            if (donor != null) {
                model.addAttribute("donor", donor);
                
                // Fetch message history even on error
                try {
                    String donorNic = ((com.blooddonation.model.Donor) donor).getNic();
                    List<com.blooddonation.model.DonorSupportMessage> messages = 
                        donorSupportMessageService.getMessagesByDonorNic(donorNic);
                    model.addAttribute("messages", messages);
                } catch (Exception ex) {
                    System.err.println("Error fetching donor messages: " + ex.getMessage());
                    model.addAttribute("messages", new java.util.ArrayList<>());
                }
            } else {
                model.addAttribute("messages", new java.util.ArrayList<>());
            }
            
            model.addAttribute("title", "BloodLife Customer Care");
            return "DonorSupportpage";
        }
    }

    @PostMapping("/donor-support/delete/{messageId}")
    public String deleteSupportMessage(@PathVariable Long messageId,
                                     Model model,
                                     HttpSession session) {
        try {
            System.out.println("=== Delete Support Message Debug ===");
            System.out.println("Message ID to delete: " + messageId);
            
            // Check if donor is logged in
            Object donor = session.getAttribute("donor");
            if (donor == null) {
                System.out.println("❌ No donor in session, redirecting to home");
                return "redirect:/";
            }
            
            System.out.println("✅ Donor found in session: " + ((com.blooddonation.model.Donor) donor).getNic());
            
            // Get the message to verify ownership
            Optional<com.blooddonation.model.DonorSupportMessage> messageOpt = 
                donorSupportMessageService.getMessageById(messageId);
            
            if (messageOpt.isPresent()) {
                com.blooddonation.model.DonorSupportMessage message = messageOpt.get();
                String donorNic = ((com.blooddonation.model.Donor) donor).getNic();
                
                System.out.println("✅ Message found: " + message.getSubject());
                System.out.println("Message owner NIC: " + message.getDonorNic());
                System.out.println("Logged in donor NIC: " + donorNic);
                
                // Verify the message belongs to the logged-in donor
                if (message.getDonorNic().equals(donorNic)) {
                    System.out.println("✅ Ownership verified, deleting message...");
                    donorSupportMessageService.deleteMessage(messageId);
                    System.out.println("✅ Message deleted successfully");
                    model.addAttribute("successMessage", "Message deleted successfully.");
                } else {
                    System.out.println("❌ Ownership verification failed");
                    model.addAttribute("errorMessage", "You can only delete your own messages.");
                }
            } else {
                System.out.println("❌ Message not found with ID: " + messageId);
                model.addAttribute("errorMessage", "Message not found.");
            }
            
            // Redirect back to support page with updated message list
            return "redirect:/donor-support";
            
        } catch (Exception e) {
            System.err.println("❌ Error deleting support message: " + e.getMessage());
            e.printStackTrace();
            return "redirect:/donor-support?error=delete_failed";
        }
    }

    // Receiver Support Endpoints
    @GetMapping("/receiver-support")
    public String receiverSupportPage(@RequestParam(required = false) String error,
                                    Model model, HttpSession session) {
        // Check if receiver is logged in
        Object receiver = session.getAttribute("receiver");
        if (receiver != null) {
            model.addAttribute("receiver", receiver);
            
            // Fetch receiver's message history
            try {
                String receiverNic = ((com.blooddonation.model.Receiver) receiver).getNic();
                List<com.blooddonation.model.ReceiverSupportMessage> messages = 
                    receiverSupportMessageService.getMessagesByReceiverNic(receiverNic);
                model.addAttribute("messages", messages);
                
                // Debug: Show all messages if no specific messages found (for testing)
                if (messages.isEmpty()) {
                    System.out.println("No messages found for receiver NIC: " + receiverNic);
                    List<com.blooddonation.model.ReceiverSupportMessage> allMessages = 
                        receiverSupportMessageService.getAllMessages();
                    if (!allMessages.isEmpty()) {
                        model.addAttribute("messages", allMessages);
                        model.addAttribute("showAllMessages", true);
                        System.out.println("Showing all messages for testing purposes: " + allMessages.size());
                    }
                }
            } catch (Exception e) {
                System.err.println("Error fetching receiver messages: " + e.getMessage());
                e.printStackTrace();
                model.addAttribute("messages", new java.util.ArrayList<>());
            }
        } else {
            model.addAttribute("messages", new java.util.ArrayList<>());
        }
        
        // Handle error messages from redirects
        if ("delete_failed".equals(error)) {
            model.addAttribute("errorMessage", "Failed to delete message. Please try again.");
        }
        
        model.addAttribute("title", "BloodLife Customer Care");
        return "ReceiverSupportpage";
    }

    @PostMapping("/receiver-support")
    public String submitReceiverSupport(@RequestParam("cnic") String cnic,
                                      @RequestParam("fullName") String fullName,
                                      @RequestParam("email") String email,
                                      @RequestParam("priority") String priority,
                                      @RequestParam("subject") String subject,
                                      @RequestParam("message") String message,
                                      Model model,
                                      HttpSession session) {
        try {
            System.out.println("=== Submit Receiver Support Message Debug ===");
            System.out.println("CNIC: " + cnic);
            System.out.println("Full Name: " + fullName);
            System.out.println("Email: " + email);
            System.out.println("Priority: " + priority);
            System.out.println("Subject: " + subject);
            System.out.println("Message: " + message);
            
            // Create and save the support message
            com.blooddonation.model.ReceiverSupportMessage supportMessage = new com.blooddonation.model.ReceiverSupportMessage();
            supportMessage.setReceiverNic(cnic);
            supportMessage.setReceiverFullName(fullName);
            supportMessage.setReceiverEmail(email);
            supportMessage.setSubject(subject);
            supportMessage.setMessageText(message);
            
            // Set priority
            String priorityFormatted = priority.substring(0, 1).toUpperCase() + priority.substring(1).toLowerCase();
            try {
                supportMessage.setPriority(com.blooddonation.model.ReceiverSupportMessage.Priority.valueOf(priorityFormatted));
            } catch (IllegalArgumentException e) {
                System.err.println("Invalid priority: " + priority + ", defaulting to Medium");
                supportMessage.setPriority(com.blooddonation.model.ReceiverSupportMessage.Priority.Medium);
            }
            
            com.blooddonation.model.ReceiverSupportMessage savedMessage = receiverSupportMessageService.saveMessage(supportMessage);
            System.out.println("✅ Message saved successfully with ID: " + savedMessage.getMessageId());
            
            model.addAttribute("successMessage", "Your message has been received! Our support team will get back to you within 24 hours.");
            model.addAttribute("messageId", "MSG-" + savedMessage.getMessageId());
            
            // Check if receiver is logged in to pre-fill form and fetch updated messages
            Object receiver = session.getAttribute("receiver");
            if (receiver != null) {
                model.addAttribute("receiver", receiver);
                
                // Fetch updated message history
                try {
                    String receiverNic = ((com.blooddonation.model.Receiver) receiver).getNic();
                    List<com.blooddonation.model.ReceiverSupportMessage> messages = 
                        receiverSupportMessageService.getMessagesByReceiverNic(receiverNic);
                    model.addAttribute("messages", messages);
                } catch (Exception e) {
                    System.err.println("Error fetching receiver messages: " + e.getMessage());
                    model.addAttribute("messages", new java.util.ArrayList<>());
                }
            } else {
                model.addAttribute("messages", new java.util.ArrayList<>());
            }
            
            model.addAttribute("title", "BloodLife Customer Care");
            return "ReceiverSupportpage";
            
        } catch (Exception e) {
            System.err.println("❌ Error saving receiver support message: " + e.getMessage());
            e.printStackTrace();
            
            model.addAttribute("errorMessage", "Failed to send your message. Please try again.");
            
            // Try to preserve form data and show messages
            try {
                Object receiver = session.getAttribute("receiver");
                if (receiver != null) {
                    model.addAttribute("receiver", receiver);
                    
                    // Fetch message history
                    String receiverNic = ((com.blooddonation.model.Receiver) receiver).getNic();
                    List<com.blooddonation.model.ReceiverSupportMessage> messages = 
                        receiverSupportMessageService.getMessagesByReceiverNic(receiverNic);
                    model.addAttribute("messages", messages);
                } else {
                    model.addAttribute("messages", new java.util.ArrayList<>());
                }
            } catch (Exception ex) {
                System.err.println("Error fetching receiver messages after error: " + ex.getMessage());
                model.addAttribute("messages", new java.util.ArrayList<>());
            }
            
            model.addAttribute("title", "BloodLife Customer Care");
            return "ReceiverSupportpage";
        }
    }

    @PostMapping("/receiver-support/delete/{messageId}")
    public String deleteReceiverSupportMessage(@PathVariable Long messageId,
                                             Model model,
                                             HttpSession session) {
        try {
            System.out.println("=== Delete Receiver Support Message Debug ===");
            System.out.println("Message ID to delete: " + messageId);
            
            // Check if receiver is logged in
            Object receiver = session.getAttribute("receiver");
            if (receiver == null) {
                System.out.println("❌ No receiver in session, redirecting to login");
                return "redirect:/";
            }
            
            // Verify the message belongs to the logged-in receiver
            Optional<com.blooddonation.model.ReceiverSupportMessage> messageOpt = 
                receiverSupportMessageService.getMessageById(messageId);
            
            if (messageOpt.isPresent()) {
                com.blooddonation.model.ReceiverSupportMessage message = messageOpt.get();
                String receiverNic = ((com.blooddonation.model.Receiver) receiver).getNic();
                
                if (!message.getReceiverNic().equals(receiverNic)) {
                    System.out.println("❌ Message does not belong to logged-in receiver");
                    return "redirect:/receiver-support?error=unauthorized";
                }
                
                System.out.println("✅ Message belongs to receiver, proceeding with deletion");
                receiverSupportMessageService.deleteMessage(messageId);
                System.out.println("✅ Message deleted successfully");
            } else {
                System.out.println("❌ Message not found with ID: " + messageId);
            }
            
            // Redirect back to support page with updated message list
            return "redirect:/receiver-support";
            
        } catch (Exception e) {
            System.err.println("❌ Error deleting receiver support message: " + e.getMessage());
            e.printStackTrace();
            return "redirect:/receiver-support?error=delete_failed";
        }
    }

    @GetMapping("/test/receiver-support-messages")
    @ResponseBody
    public String testReceiverSupportMessages() {
        StringBuilder result = new StringBuilder();
        result.append("=== All Receiver Support Messages ===\n\n");
        
        try {
            List<com.blooddonation.model.ReceiverSupportMessage> allMessages = 
                receiverSupportMessageService.getAllMessages();
            
            result.append("Total receiver support messages in database: ").append(allMessages.size()).append("\n\n");
            
            if (allMessages.isEmpty()) {
                result.append("❌ No receiver support messages found in database!\n");
                result.append("Table exists but is empty. You can test by submitting a message through the form.\n");
            } else {
                for (com.blooddonation.model.ReceiverSupportMessage msg : allMessages) {
                    result.append("Message ID: ").append(msg.getMessageId()).append("\n");
                    result.append("Receiver NIC: ").append(msg.getReceiverNic()).append("\n");
                    result.append("Receiver Name: ").append(msg.getReceiverFullName()).append("\n");
                    result.append("Subject: ").append(msg.getSubject()).append("\n");
                    result.append("Priority: ").append(msg.getPriority()).append("\n");
                    result.append("Message Date: ").append(msg.getMessageDate()).append("\n");
                    result.append("Has Reply: ").append(msg.getReplyText() != null ? "Yes" : "No").append("\n");
                    result.append("---\n");
                }
            }
            
            // Test creating a new message
            result.append("\n=== Testing Message Creation ===\n");
            try {
                com.blooddonation.model.ReceiverSupportMessage testMessage = new com.blooddonation.model.ReceiverSupportMessage();
                testMessage.setReceiverNic("123456789V");
                testMessage.setReceiverFullName("Test Receiver");
                testMessage.setReceiverEmail("test@receiver.com");
                testMessage.setSubject("Test Message");
                testMessage.setMessageText("This is a test message to verify the database connection.");
                testMessage.setPriority(com.blooddonation.model.ReceiverSupportMessage.Priority.Medium);
                
                com.blooddonation.model.ReceiverSupportMessage savedMessage = receiverSupportMessageService.saveMessage(testMessage);
                result.append("✅ Test message created successfully with ID: ").append(savedMessage.getMessageId()).append("\n");
                
                // Clean up test message
                receiverSupportMessageService.deleteMessage(savedMessage.getMessageId());
                result.append("✅ Test message deleted successfully\n");
                
            } catch (Exception e) {
                result.append("❌ Error creating test message: ").append(e.getMessage()).append("\n");
                result.append("This might be due to foreign key constraint issues.\n");
            }
            
        } catch (Exception e) {
            result.append("❌ Error accessing receiver support messages: ").append(e.getMessage()).append("\n");
            result.append("Stack trace: ").append(e.getStackTrace()[0]).append("\n");
        }
        
        return result.toString();
    }

    @GetMapping("/test/support-messages")
    @ResponseBody
    public String testSupportMessages() {
        StringBuilder result = new StringBuilder();
        result.append("=== All Donor Support Messages ===\n\n");
        
        try {
            List<com.blooddonation.model.DonorSupportMessage> allMessages = 
                donorSupportMessageService.getAllMessages();
            
            result.append("Total messages in database: ").append(allMessages.size()).append("\n\n");
            
            if (allMessages.isEmpty()) {
                result.append("❌ No messages found in database!\n");
                result.append("Please run the insert_sample_donor_support_messages.sql script to add test data.\n");
                return result.toString();
            }
            
            result.append("📋 All Messages:\n");
            result.append("================\n");
            
            for (com.blooddonation.model.DonorSupportMessage msg : allMessages) {
                result.append("ID: ").append(msg.getMessageId()).append("\n");
                result.append("Donor NIC: ").append(msg.getDonorNic()).append("\n");
                result.append("Donor Name: ").append(msg.getDonorFullName()).append("\n");
                result.append("Subject: ").append(msg.getSubject()).append("\n");
                result.append("Priority: ").append(msg.getPriority()).append("\n");
                result.append("Message Date: ").append(msg.getMessageDate()).append("\n");
                result.append("Has Reply: ").append(msg.getReplyText() != null ? "Yes" : "No").append("\n");
                if (msg.getReplyText() != null) {
                    result.append("Reply Date: ").append(msg.getReplyDate()).append("\n");
                }
                result.append("Message: ").append(msg.getMessageText().substring(0, Math.min(100, msg.getMessageText().length()))).append("...\n");
                result.append("---\n\n");
            }
            
        } catch (Exception e) {
            result.append("❌ Error fetching messages: ").append(e.getMessage()).append("\n");
            e.printStackTrace();
        }
        
        return result.toString();
    }

    @GetMapping("/test/save-message")
    @ResponseBody
    public String testSaveMessage() {
        StringBuilder result = new StringBuilder();
        result.append("=== Test Save Message ===\n\n");
        
        try {
            // Create a test message
            com.blooddonation.model.DonorSupportMessage testMessage = new com.blooddonation.model.DonorSupportMessage();
            testMessage.setDonorNic("TEST123V");
            testMessage.setDonorFullName("Test User");
            testMessage.setDonorEmail("test@example.com");
            testMessage.setSubject("Test Message");
            testMessage.setMessageText("This is a test message to verify the save functionality.");
            testMessage.setPriority(com.blooddonation.model.DonorSupportMessage.Priority.Medium);
            
            result.append("Creating test message...\n");
            result.append("NIC: ").append(testMessage.getDonorNic()).append("\n");
            result.append("Name: ").append(testMessage.getDonorFullName()).append("\n");
            result.append("Subject: ").append(testMessage.getSubject()).append("\n");
            result.append("Priority: ").append(testMessage.getPriority()).append("\n");
            
            // Try to save
            com.blooddonation.model.DonorSupportMessage savedMessage = donorSupportMessageService.saveMessage(testMessage);
            
            result.append("✅ Message saved successfully!\n");
            result.append("Message ID: ").append(savedMessage.getMessageId()).append("\n");
            result.append("Message Date: ").append(savedMessage.getMessageDate()).append("\n");
            
            // Try to retrieve it
            Optional<com.blooddonation.model.DonorSupportMessage> retrievedMessage = 
                donorSupportMessageService.getMessageById(savedMessage.getMessageId());
            
            if (retrievedMessage.isPresent()) {
                result.append("✅ Message retrieved successfully!\n");
                result.append("Retrieved Subject: ").append(retrievedMessage.get().getSubject()).append("\n");
            } else {
                result.append("❌ Failed to retrieve saved message\n");
            }
            
        } catch (Exception e) {
            result.append("❌ Error: ").append(e.getMessage()).append("\n");
            result.append("Error type: ").append(e.getClass().getSimpleName()).append("\n");
            e.printStackTrace();
        }
        
        return result.toString();
    }

    @GetMapping("/test/table-check")
    @ResponseBody
    public String testTableCheck() {
        StringBuilder result = new StringBuilder();
        result.append("=== DonorSupport_Message Table Check ===\n\n");
        
        try {
            // Test if we can access the repository
            result.append("Testing repository access...\n");
            long messageCount = donorSupportMessageService.getAllMessages().size();
            result.append("✅ Repository accessible\n");
            result.append("Total messages in table: ").append(messageCount).append("\n\n");
            
            // Test if we can create a simple message
            result.append("Testing message creation...\n");
            com.blooddonation.model.DonorSupportMessage testMsg = new com.blooddonation.model.DonorSupportMessage();
            testMsg.setDonorNic("TEST999V");
            testMsg.setDonorFullName("Test User");
            testMsg.setDonorEmail("test@example.com");
            testMsg.setSubject("Table Test");
            testMsg.setMessageText("Testing table access");
            testMsg.setPriority(com.blooddonation.model.DonorSupportMessage.Priority.Low);
            
            com.blooddonation.model.DonorSupportMessage savedMsg = donorSupportMessageService.saveMessage(testMsg);
            result.append("✅ Message created successfully\n");
            result.append("Message ID: ").append(savedMsg.getMessageId()).append("\n");
            result.append("Message Date: ").append(savedMsg.getMessageDate()).append("\n\n");
            
            // Clean up test message
            donorSupportMessageService.deleteMessage(savedMsg.getMessageId());
            result.append("✅ Test message cleaned up\n");
            
            result.append("🎉 Table is working correctly!\n");
            
        } catch (Exception e) {
            result.append("❌ Error: ").append(e.getMessage()).append("\n");
            result.append("Error type: ").append(e.getClass().getSimpleName()).append("\n");
            result.append("Stack trace:\n");
            
            // Add stack trace
            java.io.StringWriter sw = new java.io.StringWriter();
            java.io.PrintWriter pw = new java.io.PrintWriter(sw);
            e.printStackTrace(pw);
            result.append(sw.toString());
            
            result.append("\n\nPossible solutions:\n");
            result.append("1. Run create_donor_support_message_table.sql\n");
            result.append("2. Check database connection\n");
            result.append("3. Verify Donor table exists (for FK constraint)\n");
        }
        
        return result.toString();
    }
}