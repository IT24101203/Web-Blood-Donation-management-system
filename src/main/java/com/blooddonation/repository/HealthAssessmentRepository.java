package com.blooddonation.repository;

import com.blooddonation.model.HealthAssessment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HealthAssessmentRepository extends JpaRepository<HealthAssessment, Long> {
    List<HealthAssessment> findByDonorNic(String donorNic);
    Optional<HealthAssessment> findFirstByDonorNicOrderByAssessmentDateDesc(String donorNic);
    boolean existsByDonorNic(String donorNic);
}





