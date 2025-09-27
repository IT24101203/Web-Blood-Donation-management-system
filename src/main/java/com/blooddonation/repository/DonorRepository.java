package com.blooddonation.repository;

import com.blooddonation.model.Donor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DonorRepository extends JpaRepository<Donor, String> {
    Optional<Donor> findByNic(String nic);
    boolean existsByNic(String nic);
    boolean existsByEmail(String email);
    Optional<Donor> findByNicAndPassword(String nic, String password);
    
    // Additional methods that might be needed
    Optional<Donor> findByEmail(String email);
    List<Donor> findByBloodType(String bloodType);
}