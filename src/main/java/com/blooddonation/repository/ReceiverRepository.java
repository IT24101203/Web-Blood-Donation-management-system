package com.blooddonation.repository;

import com.blooddonation.model.Receiver;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReceiverRepository extends JpaRepository<Receiver, String> {
    Optional<Receiver> findByNic(String nic);
    boolean existsByNic(String nic);
    boolean existsByEmail(String email);
    Optional<Receiver> findByNicAndPassword(String nic, String password);
    
    // Additional methods that might be needed
    Optional<Receiver> findByEmail(String email);
    List<Receiver> findByBloodTypeNeeded(String bloodTypeNeeded);
    List<Receiver> findByHospitalPreference(String hospitalPreference);
}
