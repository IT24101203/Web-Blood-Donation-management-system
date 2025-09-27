package com.blooddonation.service;

import com.blooddonation.model.HealthAssessment;
import com.blooddonation.repository.HealthAssessmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Service
public class HealthAssessmentServiceImpl implements HealthAssessmentService {

    @Autowired
    private HealthAssessmentRepository healthAssessmentRepository;

    @Override
    public HealthAssessment saveHealthAssessment(HealthAssessment healthAssessment) {
        healthAssessment.setAssessmentDate(LocalDateTime.now());
        return healthAssessmentRepository.save(healthAssessment);
    }

    @Override
    public HealthAssessment getLatestAssessmentByDonorNic(String donorNic) {
        return healthAssessmentRepository.findFirstByDonorNicOrderByAssessmentDateDesc(donorNic)
                .orElse(null);
    }

    @Override
    public boolean hasRecentAssessment(String donorNic) {
        HealthAssessment latest = getLatestAssessmentByDonorNic(donorNic);
        if (latest == null) {
            return false;
        }
        
        // Check if assessment is within last 30 days
        LocalDateTime thirtyDaysAgo = LocalDateTime.now().minus(30, ChronoUnit.DAYS);
        return latest.getAssessmentDate().isAfter(thirtyDaysAgo);
    }

    @Override
    public List<HealthAssessment> getAllAssessments() {
        return healthAssessmentRepository.findAll();
    }

    @Override
    public List<HealthAssessment> getPendingAssessments() {
        // Since we don't have status column, return all assessments as "pending" for demo
        // In a real system, you'd have a separate approval table or status column
        return healthAssessmentRepository.findAll();
    }

    @Override
    public Optional<HealthAssessment> getAssessmentById(Long assessmentId) {
        return healthAssessmentRepository.findById(assessmentId);
    }

    @Override
    public HealthAssessment approveAssessment(Long assessmentId, String doctorNotes, String approvedBy) {
        Optional<HealthAssessment> assessmentOpt = getAssessmentById(assessmentId);
        if (assessmentOpt.isPresent()) {
            HealthAssessment assessment = assessmentOpt.get();
            // Since we can't store approval status in the database,
            // we'll just log the approval and return the assessment
            System.out.println("✅ Health Assessment Approved:");
            System.out.println("   Assessment ID: " + assessmentId);
            System.out.println("   Donor NIC: " + assessment.getDonorNic());
            System.out.println("   Approved By: " + approvedBy);
            System.out.println("   Doctor Notes: " + doctorNotes);
            System.out.println("   Approval Date: " + LocalDateTime.now());
            
            // In a real system, you would:
            // 1. Create a separate Approval table
            // 2. Add approval status to existing table
            // 3. Or use a different approach to track approvals
            
            return assessment;
        }
        return null;
    }

    @Override
    public HealthAssessment rejectAssessment(Long assessmentId, String doctorNotes, String approvedBy) {
        Optional<HealthAssessment> assessmentOpt = getAssessmentById(assessmentId);
        if (assessmentOpt.isPresent()) {
            HealthAssessment assessment = assessmentOpt.get();
            // Since we can't store rejection status in the database,
            // we'll just log the rejection and return the assessment
            System.out.println("❌ Health Assessment Rejected:");
            System.out.println("   Assessment ID: " + assessmentId);
            System.out.println("   Donor NIC: " + assessment.getDonorNic());
            System.out.println("   Rejected By: " + approvedBy);
            System.out.println("   Doctor Notes: " + doctorNotes);
            System.out.println("   Rejection Date: " + LocalDateTime.now());
            
            return assessment;
        }
        return null;
    }

    @Override
    public void deleteAssessment(Long assessmentId) {
        healthAssessmentRepository.deleteById(assessmentId);
    }
}





