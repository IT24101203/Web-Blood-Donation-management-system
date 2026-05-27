package com.blooddonation.repository;

import com.blooddonation.model.BloodRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BloodRequestRepository extends JpaRepository<BloodRequest, Long> {
    
    // Find all requests by receiver CNIC
    List<BloodRequest> findByReceiverCnicOrderByRequestDateDesc(String receiverCnic);
    
    // Find requests by priority
    List<BloodRequest> findByPriorityOrderByRequestDateDesc(String priority);
    
    // Find requests by blood type needed
    List<BloodRequest> findByBloodTypeNeededOrderByRequestDateDesc(String bloodTypeNeeded);
    
    // Find requests by hospital
    List<BloodRequest> findByHospitalOrderByRequestDateDesc(String hospital);
    
    // Find requests that need blood by a specific date
    List<BloodRequest> findByNeededByDateLessThanEqualOrderByRequestDateDesc(LocalDate date);
    
    // Find urgent requests (High or Critical priority)
    @Query("SELECT br FROM BloodRequest br WHERE br.priority IN ('High', 'Critical') ORDER BY br.requestDate DESC")
    List<BloodRequest> findUrgentRequests();
    
    // Find requests by date range
    @Query("SELECT br FROM BloodRequest br WHERE br.requestDate BETWEEN :startDate AND :endDate ORDER BY br.requestDate DESC")
    List<BloodRequest> findByRequestDateBetween(@Param("startDate") LocalDateTime startDate, 
                                               @Param("endDate") LocalDateTime endDate);
    
    // Count requests by receiver
    long countByReceiverCnic(String receiverCnic);
    
    // Count requests by priority
    long countByPriority(String priority);
    
    // Count requests by blood type
    long countByBloodTypeNeeded(String bloodTypeNeeded);
    
    // Find pending requests (no approval date)
    @Query("SELECT br FROM BloodRequest br WHERE br.approvedDate IS NULL ORDER BY br.requestDate DESC")
    List<BloodRequest> findPendingRequests();
    
    // Find approved requests
    @Query("SELECT br FROM BloodRequest br WHERE br.approvedDate IS NOT NULL AND br.completionDate IS NULL ORDER BY br.requestDate DESC")
    List<BloodRequest> findApprovedRequests();
    
    // Find completed requests
    @Query("SELECT br FROM BloodRequest br WHERE br.completionDate IS NOT NULL ORDER BY br.requestDate DESC")
    List<BloodRequest> findCompletedRequests();
}

