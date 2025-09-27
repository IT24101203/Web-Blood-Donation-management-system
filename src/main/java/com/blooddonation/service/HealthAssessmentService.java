package com.blooddonation.service;

import com.blooddonation.model.HealthAssessment;
import java.util.List;
import java.util.Optional;

public interface HealthAssessmentService {
    HealthAssessment saveHealthAssessment(HealthAssessment healthAssessment);
    HealthAssessment getLatestAssessmentByDonorNic(String donorNic);
    boolean hasRecentAssessment(String donorNic);
    
    // Doctor approval methods
    List<HealthAssessment> getAllAssessments();
    List<HealthAssessment> getPendingAssessments();
    Optional<HealthAssessment> getAssessmentById(Long assessmentId);
    HealthAssessment approveAssessment(Long assessmentId, String doctorNotes, String approvedBy);
    HealthAssessment rejectAssessment(Long assessmentId, String doctorNotes, String approvedBy);
    void deleteAssessment(Long assessmentId);
}





