package com.blooddonation.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Table(name = "Doctor_Note")
public class DoctorNote {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Note_ID")
    private Long noteId;

    @NotBlank(message = "Donor NIC is required")
    @Column(name = "Donor_NIC", length = 20)
    private String donorNic;

    @NotNull(message = "Outcome is required")
    @Column(name = "Outcome", length = 50)
    private String outcome;

    @Column(name = "Reason_For_Deferral", length = 500)
    private String reasonForDeferral;

    @Column(name = "Immediate_Reaction", length = 500)
    private String immediateReaction;

    @Column(name = "Recovery_Advice", length = 500)
    private String recoveryAdvice;

    @Column(name = "Additional_Note", length = 1000)
    private String additionalNotes;

    @Column(name = "Created_At")
    private LocalDateTime createdAt;

    // Constructors
    public DoctorNote() {
        this.createdAt = LocalDateTime.now();
    }

    public DoctorNote(String donorNic, String outcome) {
        this();
        this.donorNic = donorNic;
        this.outcome = outcome;
    }

    // Getters and Setters
    public Long getNoteId() { return noteId; }
    public void setNoteId(Long noteId) { this.noteId = noteId; }

    public String getDonorNic() { return donorNic; }
    public void setDonorNic(String donorNic) { this.donorNic = donorNic; }

    public String getOutcome() { return outcome; }
    public void setOutcome(String outcome) { this.outcome = outcome; }

    public String getReasonForDeferral() { return reasonForDeferral; }
    public void setReasonForDeferral(String reasonForDeferral) { this.reasonForDeferral = reasonForDeferral; }

    public String getImmediateReaction() { return immediateReaction; }
    public void setImmediateReaction(String immediateReaction) { this.immediateReaction = immediateReaction; }

    public String getRecoveryAdvice() { return recoveryAdvice; }
    public void setRecoveryAdvice(String recoveryAdvice) { this.recoveryAdvice = recoveryAdvice; }

    public String getAdditionalNotes() { return additionalNotes; }
    public void setAdditionalNotes(String additionalNotes) { this.additionalNotes = additionalNotes; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
