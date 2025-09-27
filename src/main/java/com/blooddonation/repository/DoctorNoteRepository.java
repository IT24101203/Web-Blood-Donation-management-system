package com.blooddonation.repository;

import com.blooddonation.model.DoctorNote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DoctorNoteRepository extends JpaRepository<DoctorNote, Long> {
    
    /**
     * Find all doctor notes for a specific donor
     * @param donorNic the donor NIC
     * @return List of doctor notes ordered by creation date (newest first)
     */
    List<DoctorNote> findByDonorNicOrderByCreatedAtDesc(String donorNic);
    
    /**
     * Find doctor notes by outcome
     * @param outcome the outcome (Successful, Deferred, Incomplete, Adverse Reaction)
     * @return List of doctor notes ordered by creation date (newest first)
     */
    List<DoctorNote> findByOutcomeOrderByCreatedAtDesc(String outcome);
    
    // Follow-up methods removed since these fields don't exist in the database
    
    /**
     * Count total doctor notes
     * @return total count of doctor notes
     */
    long count();
    
    /**
     * Count doctor notes by outcome
     * @param outcome the outcome to count
     * @return count of doctor notes with specific outcome
     */
    long countByOutcome(String outcome);
    
    /**
     * Find recent doctor notes (last 30 days)
     * @return List of recent doctor notes ordered by creation date (newest first)
     */
    @Query("SELECT dn FROM DoctorNote dn WHERE dn.createdAt >= :thirtyDaysAgo ORDER BY dn.createdAt DESC")
    List<DoctorNote> findRecentNotes(@Param("thirtyDaysAgo") java.time.LocalDateTime thirtyDaysAgo);
    
    // CreatedBy methods removed since this field doesn't exist in the database
}
