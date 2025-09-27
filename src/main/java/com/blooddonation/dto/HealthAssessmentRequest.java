package com.blooddonation.dto;

import jakarta.validation.constraints.*;
import java.time.LocalDate;

public class HealthAssessmentRequest {
    @NotBlank(message = "Donor NIC is required")
    private String donorNic;

    @NotNull(message = "Weight is required")
    @DecimalMin(value = "45.0", message = "Weight must be at least 45 kg")
    private String weight;

    private String height;

    @NotNull(message = "Systolic blood pressure is required")
    @Min(value = 80, message = "Systolic pressure must be at least 80")
    @Max(value = 200, message = "Systolic pressure must not exceed 200")
    private Integer bloodPressureSystolic;

    @NotNull(message = "Diastolic blood pressure is required")
    @Min(value = 50, message = "Diastolic pressure must be at least 50")
    @Max(value = 120, message = "Diastolic pressure must not exceed 120")
    private Integer bloodPressureDiastolic;

    @NotNull(message = "Hemoglobin level is required")
    @DecimalMin(value = "8.0", message = "Hemoglobin must be at least 8.0 g/dL")
    @DecimalMax(value = "20.0", message = "Hemoglobin must not exceed 20.0 g/dL")
    private String hemoglobinLevel;

    private LocalDate lastDonationDate;

    @NotNull(message = "Recent donation status is required")
    private String recentDonation;

    @NotNull(message = "Major disease status is required")
    private String hasMajorDisease;

    private String majorDiseaseDetails;

    private Boolean hasHeartDisease = false;

    private Boolean hasDiabetes = false;

    private Boolean hasHighBP = false;

    private Boolean hasRecentSurgery = false;

    private Boolean hasInfectiousDiseases = false;

    private Boolean hasOtherConditions = false;

    private String otherConditionsDetails;

    private String currentMedication;

    @NotNull(message = "Recent illness status is required")
    private String recentIllness;

    private LocalDate illnessDate;

    private String illnessDetails;

    // Getters and Setters
    public String getDonorNic() { return donorNic; }
    public void setDonorNic(String donorNic) { this.donorNic = donorNic; }

    public String getWeight() { return weight; }
    public void setWeight(String weight) { this.weight = weight; }

    public String getHeight() { return height; }
    public void setHeight(String height) { this.height = height; }

    public Integer getBloodPressureSystolic() { return bloodPressureSystolic; }
    public void setBloodPressureSystolic(Integer bloodPressureSystolic) { this.bloodPressureSystolic = bloodPressureSystolic; }

    public Integer getBloodPressureDiastolic() { return bloodPressureDiastolic; }
    public void setBloodPressureDiastolic(Integer bloodPressureDiastolic) { this.bloodPressureDiastolic = bloodPressureDiastolic; }

    public String getHemoglobinLevel() { return hemoglobinLevel; }
    public void setHemoglobinLevel(String hemoglobinLevel) { this.hemoglobinLevel = hemoglobinLevel; }

    public LocalDate getLastDonationDate() { return lastDonationDate; }
    public void setLastDonationDate(LocalDate lastDonationDate) { this.lastDonationDate = lastDonationDate; }

    public String getRecentDonation() { return recentDonation; }
    public void setRecentDonation(String recentDonation) { this.recentDonation = recentDonation; }

    public String getHasMajorDisease() { return hasMajorDisease; }
    public void setHasMajorDisease(String hasMajorDisease) { this.hasMajorDisease = hasMajorDisease; }

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

    public String getRecentIllness() { return recentIllness; }
    public void setRecentIllness(String recentIllness) { this.recentIllness = recentIllness; }

    public LocalDate getIllnessDate() { return illnessDate; }
    public void setIllnessDate(LocalDate illnessDate) { this.illnessDate = illnessDate; }

    public String getIllnessDetails() { return illnessDetails; }
    public void setIllnessDetails(String illnessDetails) { this.illnessDetails = illnessDetails; }
}
