package com.blooddonation.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "HealthAssessment")
public class HealthAssessment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "HealthAssessmentID")
    private Long healthAssessmentId;

    @NotNull
    @Column(name = "DonorNIC", length = 12)
    private String donorNic;

    @Column(name = "AssessmentDate")
    private LocalDateTime assessmentDate = LocalDateTime.now();

    // Physical Measurements
    @NotNull
    @Column(name = "Weight", precision = 5, scale = 2)
    private BigDecimal weight;

    @Column(name = "Height", precision = 5, scale = 2)
    private BigDecimal height;

    // Vital Signs
    @NotNull
    @Column(name = "BloodPressureSystolic")
    private Integer bloodPressureSystolic;

    @NotNull
    @Column(name = "BloodPressureDiastolic")
    private Integer bloodPressureDiastolic;

    @NotNull
    @Column(name = "HemoglobinLevel", precision = 4, scale = 2)
    private BigDecimal hemoglobinLevel;

    @Column(name = "LastDonationDate")
    private LocalDate lastDonationDate;

    // Recent Donation History
    @NotNull
    @Column(name = "RecentDonation")
    private Boolean recentDonation;

    // Medical Conditions
    @NotNull
    @Column(name = "HasMajorDisease")
    private Boolean hasMajorDisease;

    @Column(name = "MajorDiseaseDetails", length = 500)
    private String majorDiseaseDetails;

    @Column(name = "HasHeartDisease")
    private Boolean hasHeartDisease = false;

    @Column(name = "HasDiabetes")
    private Boolean hasDiabetes = false;

    @Column(name = "HasHighBP")
    private Boolean hasHighBP = false;

    @Column(name = "HasRecentSurgery")
    private Boolean hasRecentSurgery = false;

    @Column(name = "HasInfectiousDiseases")
    private Boolean hasInfectiousDiseases = false;

    @Column(name = "HasOtherConditions")
    private Boolean hasOtherConditions = false;

    @Column(name = "OtherConditionsDetails", length = 500)
    private String otherConditionsDetails;

    // Current Medication
    @Column(name = "CurrentMedication", length = 1000)
    private String currentMedication;

    // Recent Illness
    @NotNull
    @Column(name = "RecentIllness")
    private Boolean recentIllness;

    @Column(name = "IllnessDate")
    private LocalDate illnessDate;

    @Column(name = "IllnessDetails", length = 500)
    private String illnessDetails;

    // Constructors
    public HealthAssessment() {}

    public HealthAssessment(String donorNic) {
        this.donorNic = donorNic;
    }

    // Getters and Setters
    public Long getHealthAssessmentId() { return healthAssessmentId; }
    public void setHealthAssessmentId(Long healthAssessmentId) { this.healthAssessmentId = healthAssessmentId; }

    public String getDonorNic() { return donorNic; }
    public void setDonorNic(String donorNic) { this.donorNic = donorNic; }

    public LocalDateTime getAssessmentDate() { return assessmentDate; }
    public void setAssessmentDate(LocalDateTime assessmentDate) { this.assessmentDate = assessmentDate; }

    public BigDecimal getWeight() { return weight; }
    public void setWeight(BigDecimal weight) { this.weight = weight; }

    public BigDecimal getHeight() { return height; }
    public void setHeight(BigDecimal height) { this.height = height; }

    public Integer getBloodPressureSystolic() { return bloodPressureSystolic; }
    public void setBloodPressureSystolic(Integer bloodPressureSystolic) { this.bloodPressureSystolic = bloodPressureSystolic; }

    public Integer getBloodPressureDiastolic() { return bloodPressureDiastolic; }
    public void setBloodPressureDiastolic(Integer bloodPressureDiastolic) { this.bloodPressureDiastolic = bloodPressureDiastolic; }

    public BigDecimal getHemoglobinLevel() { return hemoglobinLevel; }
    public void setHemoglobinLevel(BigDecimal hemoglobinLevel) { this.hemoglobinLevel = hemoglobinLevel; }

    public LocalDate getLastDonationDate() { return lastDonationDate; }
    public void setLastDonationDate(LocalDate lastDonationDate) { this.lastDonationDate = lastDonationDate; }

    public Boolean getRecentDonation() { return recentDonation; }
    public void setRecentDonation(Boolean recentDonation) { this.recentDonation = recentDonation; }

    public Boolean getHasMajorDisease() { return hasMajorDisease; }
    public void setHasMajorDisease(Boolean hasMajorDisease) { this.hasMajorDisease = hasMajorDisease; }

    public String getMajorDiseaseDetails() { return majorDiseaseDetails; }
    public void setMajorDiseaseDetails(String majorDiseaseDetails) { this.majorDiseaseDetails = majorDiseaseDetails; }

    public Boolean getHasHeartDisease() { return hasHeartDisease; }
    public void setHasHeartDisease(Boolean hasHeartDisease) { this.hasHeartDisease = hasHeartDisease; }

    public Boolean getHasDiabetes() { return hasDiabetes; }
    public void setHasDiabetes(Boolean hasDiabetes) { this.hasDiabetes = hasDiabetes; }

    public Boolean getHasHighBP() { return hasHighBP; }
    public void setHasHighBP(Boolean hasHighBP) { this.hasHighBP = hasHighBP; }

    public Boolean getHasRecentSurgery() { return hasRecentSurgery; }
    public void setHasRecentSurgery(Boolean hasRecentSurgery) { this.hasRecentSurgery = hasRecentSurgery; }

    public Boolean getHasInfectiousDiseases() { return hasInfectiousDiseases; }
    public void setHasInfectiousDiseases(Boolean hasInfectiousDiseases) { this.hasInfectiousDiseases = hasInfectiousDiseases; }

    public Boolean getHasOtherConditions() { return hasOtherConditions; }
    public void setHasOtherConditions(Boolean hasOtherConditions) { this.hasOtherConditions = hasOtherConditions; }

    public String getOtherConditionsDetails() { return otherConditionsDetails; }
    public void setOtherConditionsDetails(String otherConditionsDetails) { this.otherConditionsDetails = otherConditionsDetails; }

    public String getCurrentMedication() { return currentMedication; }
    public void setCurrentMedication(String currentMedication) { this.currentMedication = currentMedication; }

    public Boolean getRecentIllness() { return recentIllness; }
    public void setRecentIllness(Boolean recentIllness) { this.recentIllness = recentIllness; }

    public LocalDate getIllnessDate() { return illnessDate; }
    public void setIllnessDate(LocalDate illnessDate) { this.illnessDate = illnessDate; }

    public String getIllnessDetails() { return illnessDetails; }
    public void setIllnessDetails(String illnessDetails) { this.illnessDetails = illnessDetails; }
}

