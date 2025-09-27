package com.blooddonation.controller;

import com.blooddonation.dto.HealthAssessmentRequest;
import com.blooddonation.model.HealthAssessment;
import com.blooddonation.service.HealthAssessmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import java.math.BigDecimal;

@Controller
public class HealthAssessmentController {

    private final HealthAssessmentService healthAssessmentService;

    @Autowired
    public HealthAssessmentController(HealthAssessmentService healthAssessmentService) {
        this.healthAssessmentService = healthAssessmentService;
    }

    @PostMapping("/health-assessment")
    public String submitHealthAssessment(@Valid @ModelAttribute("healthAssessment") HealthAssessmentRequest request,
                                       BindingResult bindingResult,
                                       HttpSession session,
                                       Model model) {
        if (bindingResult.hasErrors()) {
            System.out.println("Validation errors: " + bindingResult.getAllErrors());
            return "redirect:/dashboard?error=true";
        }

        try {
            System.out.println("=== Health Assessment Form Submission ===");
            System.out.println("Donor NIC: " + request.getDonorNic());
            System.out.println("Weight: " + request.getWeight());
            System.out.println("Height: " + request.getHeight());
            System.out.println("Blood Pressure: " + request.getBloodPressureSystolic() + "/" + request.getBloodPressureDiastolic());
            System.out.println("Hemoglobin: " + request.getHemoglobinLevel());
            System.out.println("Recent Donation: " + request.getRecentDonation());
            System.out.println("Has Major Disease: " + request.getHasMajorDisease());
            System.out.println("Recent Illness: " + request.getRecentIllness());
            
            // Get donor from session for validation
            Object donorObj = session.getAttribute("donor");
            if (donorObj == null) {
                System.out.println("No donor in session, redirecting to home");
                return "redirect:/";
            }

            // Create HealthAssessment entity from DTO (same pattern as Donor registration)
            HealthAssessment healthAssessment = new HealthAssessment();
            healthAssessment.setDonorNic(request.getDonorNic());
            healthAssessment.setWeight(new BigDecimal(request.getWeight()));
            if (request.getHeight() != null && !request.getHeight().isEmpty()) {
                healthAssessment.setHeight(new BigDecimal(request.getHeight()));
            }
            healthAssessment.setBloodPressureSystolic(request.getBloodPressureSystolic());
            healthAssessment.setBloodPressureDiastolic(request.getBloodPressureDiastolic());
            healthAssessment.setHemoglobinLevel(new BigDecimal(request.getHemoglobinLevel()));
            
            // Set dates
            healthAssessment.setLastDonationDate(request.getLastDonationDate());
            healthAssessment.setIllnessDate(request.getIllnessDate());
            
            // Set boolean values (convert string to boolean)
            healthAssessment.setRecentDonation("true".equals(request.getRecentDonation()));
            healthAssessment.setHasMajorDisease("true".equals(request.getHasMajorDisease()));
            healthAssessment.setHasHeartDisease(request.getHasHeartDisease());
            healthAssessment.setHasDiabetes(request.getHasDiabetes());
            healthAssessment.setHasHighBP(request.getHasHighBP());
            healthAssessment.setHasRecentSurgery(request.getHasRecentSurgery());
            healthAssessment.setHasInfectiousDiseases(request.getHasInfectiousDiseases());
            healthAssessment.setHasOtherConditions(request.getHasOtherConditions());
            
            // Set text fields
            healthAssessment.setMajorDiseaseDetails(request.getMajorDiseaseDetails());
            healthAssessment.setOtherConditionsDetails(request.getOtherConditionsDetails());
            healthAssessment.setCurrentMedication(request.getCurrentMedication());
            healthAssessment.setIllnessDetails(request.getIllnessDetails());
            healthAssessment.setRecentIllness("true".equals(request.getRecentIllness()));

            System.out.println("Created HealthAssessment: " + healthAssessment);
            System.out.println("Assessment Date: " + healthAssessment.getAssessmentDate());
            System.out.println("Donor NIC: " + healthAssessment.getDonorNic());

            // Save health assessment (same pattern as Donor registration)
            System.out.println("Saving health assessment to database...");
            HealthAssessment savedAssessment = healthAssessmentService.saveHealthAssessment(healthAssessment);
            System.out.println("✅ SUCCESS: Saved assessment with ID: " + savedAssessment.getHealthAssessmentId());
            System.out.println("✅ Assessment Date: " + savedAssessment.getAssessmentDate());
            System.out.println("✅ Donor NIC: " + savedAssessment.getDonorNic());
            
            return "redirect:/dashboard?success=true";
        } catch (Exception ex) {
            System.out.println("Error saving health assessment: " + ex.getMessage());
            ex.printStackTrace();
            return "redirect:/dashboard?error=true";
        }
    }

    @GetMapping("/test-health-assessment")
    public String testHealthAssessment(HttpSession session, Model model) {
        try {
            System.out.println("🧪 TESTING HEALTH ASSESSMENT SAVING");
            
            Object donorObj = session.getAttribute("donor");
            if (donorObj == null) {
                System.out.println("❌ No donor in session");
                return "redirect:/";
            }

            com.blooddonation.model.Donor donor = (com.blooddonation.model.Donor) donorObj;
            System.out.println("✅ Donor found: " + donor.getNic());
            
            // Create a test health assessment
            HealthAssessment testAssessment = new HealthAssessment();
            testAssessment.setDonorNic(donor.getNic());
            testAssessment.setWeight(new BigDecimal("70.0"));
            testAssessment.setHeight(new BigDecimal("170.0"));
            testAssessment.setBloodPressureSystolic(120);
            testAssessment.setBloodPressureDiastolic(80);
            testAssessment.setHemoglobinLevel(new BigDecimal("14.5"));
            testAssessment.setRecentDonation(false);
            testAssessment.setHasMajorDisease(false);
            testAssessment.setRecentIllness(false);
            
            System.out.println("📊 Test assessment created: " + testAssessment);
            System.out.println("💾 Saving to database...");
            
            HealthAssessment saved = healthAssessmentService.saveHealthAssessment(testAssessment);
            System.out.println("✅ SUCCESS: Test health assessment saved with ID: " + saved.getHealthAssessmentId());
            
            return "redirect:/dashboard?success=true";
        } catch (Exception ex) {
            System.out.println("❌ TEST FAILED: " + ex.getMessage());
            ex.printStackTrace();
            return "redirect:/dashboard?error=true";
        }
    }
}