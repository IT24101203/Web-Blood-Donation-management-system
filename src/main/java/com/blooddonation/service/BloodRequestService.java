package com.blooddonation.service;

import com.blooddonation.dto.BloodRequestRequest;
import com.blooddonation.model.BloodRequest;
import com.blooddonation.model.Receiver;
import com.blooddonation.repository.BloodRequestRepository;
import com.blooddonation.repository.ReceiverRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class BloodRequestService {
    
    @Autowired
    private BloodRequestRepository bloodRequestRepository;
    
    @Autowired
    private ReceiverRepository receiverRepository;
    
    /**
     * Create a new blood request
     */
    public BloodRequest createBloodRequest(BloodRequestRequest request) {
        // Validate that the receiver exists
        Optional<Receiver> receiverOpt = receiverRepository.findByNic(request.getReceiverCNIC());
        if (receiverOpt.isEmpty()) {
            throw new IllegalArgumentException("Receiver with CNIC " + request.getReceiverCNIC() + " not found");
        }
        
        // Create new blood request
        BloodRequest bloodRequest = new BloodRequest();
        bloodRequest.setReceiverCnic(request.getReceiverCNIC());
        bloodRequest.setBloodTypeNeeded(request.getBloodType());
        bloodRequest.setQuantity(request.getQuantity());
        bloodRequest.setNeededByDate(request.getNeededByDate());
        bloodRequest.setHospital(request.getHospital());
        bloodRequest.setPriority(request.getPriority());
        bloodRequest.setNotes(request.getNotes());
        bloodRequest.setRequestDate(LocalDateTime.now());
        
        return bloodRequestRepository.save(bloodRequest);
    }
    
    /**
     * Get all blood requests
     */
    public List<BloodRequest> getAllBloodRequests() {
        return bloodRequestRepository.findAll();
    }
    
    /**
     * Get all blood requests for a specific receiver
     */
    public List<BloodRequest> getBloodRequestsByReceiver(String receiverCnic) {
        return bloodRequestRepository.findByReceiverCnicOrderByRequestDateDesc(receiverCnic);
    }
    
    /**
     * Get all pending blood requests
     */
    public List<BloodRequest> getPendingBloodRequests() {
        return bloodRequestRepository.findPendingRequests();
    }
    
    /**
     * Get all urgent blood requests (High or Critical priority)
     */
    public List<BloodRequest> getUrgentBloodRequests() {
        return bloodRequestRepository.findUrgentRequests();
    }
    
    /**
     * Get blood requests by priority
     */
    public List<BloodRequest> getBloodRequestsByPriority(String priority) {
        return bloodRequestRepository.findByPriorityOrderByRequestDateDesc(priority);
    }
    
    /**
     * Get blood requests by blood type needed
     */
    public List<BloodRequest> getBloodRequestsByBloodType(String bloodType) {
        return bloodRequestRepository.findByBloodTypeNeededOrderByRequestDateDesc(bloodType);
    }
    
    /**
     * Get blood requests by hospital
     */
    public List<BloodRequest> getBloodRequestsByHospital(String hospital) {
        return bloodRequestRepository.findByHospitalOrderByRequestDateDesc(hospital);
    }
    
    /**
     * Approve a blood request
     */
    public BloodRequest approveBloodRequest(Long requestId) {
        Optional<BloodRequest> requestOpt = bloodRequestRepository.findById(requestId);
        if (requestOpt.isEmpty()) {
            throw new IllegalArgumentException("Blood request with ID " + requestId + " not found");
        }
        
        BloodRequest bloodRequest = requestOpt.get();
        bloodRequest.setApprovedDate(LocalDateTime.now());
        
        return bloodRequestRepository.save(bloodRequest);
    }
    
    /**
     * Mark blood request as completed
     */
    public BloodRequest completeBloodRequest(Long requestId) {
        Optional<BloodRequest> requestOpt = bloodRequestRepository.findById(requestId);
        if (requestOpt.isEmpty()) {
            throw new IllegalArgumentException("Blood request with ID " + requestId + " not found");
        }
        
        BloodRequest bloodRequest = requestOpt.get();
        bloodRequest.setCompletionDate(LocalDateTime.now());
        
        return bloodRequestRepository.save(bloodRequest);
    }
    
    /**
     * Get blood request by ID
     */
    public Optional<BloodRequest> getBloodRequestById(Long requestId) {
        return bloodRequestRepository.findById(requestId);
    }
    
    /**
     * Delete a blood request
     */
    @Transactional
    public void deleteBloodRequest(Long requestId) {
        if (!bloodRequestRepository.existsById(requestId)) {
            throw new IllegalArgumentException("Blood Request not found with ID: " + requestId);
        }
        
        bloodRequestRepository.deleteById(requestId);
    }
    
    /**
     * Get statistics for blood requests
     */
    public BloodRequestStats getBloodRequestStats() {
        long totalRequests = bloodRequestRepository.count();
        long pendingRequests = bloodRequestRepository.findPendingRequests().size();
        long urgentRequests = bloodRequestRepository.findUrgentRequests().size();
        long completedRequests = bloodRequestRepository.findCompletedRequests().size();
        
        return new BloodRequestStats(totalRequests, pendingRequests, urgentRequests, completedRequests);
    }
    
    /**
     * Inner class for blood request statistics
     */
    public static class BloodRequestStats {
        private final long totalRequests;
        private final long pendingRequests;
        private final long urgentRequests;
        private final long completedRequests;
        
        public BloodRequestStats(long totalRequests, long pendingRequests, long urgentRequests, long completedRequests) {
            this.totalRequests = totalRequests;
            this.pendingRequests = pendingRequests;
            this.urgentRequests = urgentRequests;
            this.completedRequests = completedRequests;
        }
        
        public long getTotalRequests() { return totalRequests; }
        public long getPendingRequests() { return pendingRequests; }
        public long getUrgentRequests() { return urgentRequests; }
        public long getCompletedRequests() { return completedRequests; }
    }
}
