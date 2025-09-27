package com.blooddonation.service;

import com.blooddonation.model.DoctorNote;
import java.util.List;
import java.util.Optional;

public interface DoctorNoteService {
    
    /**
     * Save a new doctor note
     * @param doctorNote the doctor note to save
     * @return the saved doctor note
     */
    DoctorNote saveDoctorNote(DoctorNote doctorNote);
    
    /**
     * Get all doctor notes
     * @return list of all doctor notes
     */
    List<DoctorNote> getAllDoctorNotes();
    
    /**
     * Get doctor notes for a specific donor
     * @param donorNic the donor NIC
     * @return list of doctor notes for the donor
     */
    List<DoctorNote> getDoctorNotesByDonorNic(String donorNic);
    
    /**
     * Get doctor notes by outcome
     * @param outcome the outcome to filter by
     * @return list of doctor notes with specific outcome
     */
    List<DoctorNote> getDoctorNotesByOutcome(String outcome);
    
    // Follow-up methods removed since these fields don't exist in the database
    
    /**
     * Get a specific doctor note by ID
     * @param noteId the note ID
     * @return optional doctor note
     */
    Optional<DoctorNote> getDoctorNoteById(Long noteId);
    
    /**
     * Update an existing doctor note
     * @param doctorNote the doctor note to update
     * @return the updated doctor note
     */
    DoctorNote updateDoctorNote(DoctorNote doctorNote);
    
    /**
     * Delete a doctor note
     * @param noteId the note ID to delete
     */
    void deleteDoctorNote(Long noteId);
    
    /**
     * Get total count of doctor notes
     * @return total count
     */
    long getTotalNotesCount();
    
    /**
     * Get count of doctor notes by outcome
     * @param outcome the outcome to count
     * @return count of notes with specific outcome
     */
    long getNotesCountByOutcome(String outcome);
    
    /**
     * Get recent doctor notes (last 30 days)
     * @return list of recent doctor notes
     */
    List<DoctorNote> getRecentNotes();
    
    // CreatedBy methods removed since this field doesn't exist in the database
}
