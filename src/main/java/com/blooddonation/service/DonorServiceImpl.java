package com.blooddonation.service;

import com.blooddonation.model.Donor;
import com.blooddonation.dto.DonorRegistrationRequest;
import com.blooddonation.repository.DonorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DonorServiceImpl implements DonorService {

    @Autowired
    private DonorRepository donorRepository;

    @Override
    public Donor registerDonor(DonorRegistrationRequest request) {
        // Check if donor already exists
        if (donorRepository.existsByNic(request.getNic())) {
            throw new RuntimeException("Donor with NIC " + request.getNic() + " already exists");
        }

        // Create new donor entity
        Donor donor = new Donor();
        donor.setNic(request.getNic());
        donor.setFullName(request.getFullName());
        donor.setDateOfBirth(request.getDateOfBirth());
        donor.setGender(request.getGender());
        donor.setBloodType(request.getBloodType());
        donor.setContactNumber(request.getContactNumber());
        donor.setEmail(request.getEmail());
        donor.setAddress(request.getAddress());
        donor.setPassword(request.getPassword()); // In production, encrypt this!
        donor.setHealthStatus("PENDING");

        // Save to database
        return donorRepository.save(donor);
    }

    @Override
    public boolean isDonorExists(String nic) {
        return donorRepository.existsByNic(nic);
    }

    @Override
    public Donor authenticateDonor(String nic, String password) {
        return donorRepository.findByNicAndPassword(nic, password)
                .orElseThrow(() -> new RuntimeException("Invalid NIC or password"));
    }

    @Override
    public List<Donor> getAllDonors() {
        try {
            return donorRepository.findAll();
        } catch (Exception e) {
            System.err.println("Error getting all donors: " + e.getMessage());
            return List.of();
        }
    }

    @Override
    public Optional<Donor> getDonorByNic(String nic) {
        try {
            return donorRepository.findByNic(nic);
        } catch (Exception e) {
            System.err.println("Error finding donor by NIC: " + e.getMessage());
            return Optional.empty();
        }
    }

    @Override
    public Donor updateDonor(Donor donor) {
        try {
            return donorRepository.save(donor);
        } catch (Exception e) {
            System.err.println("Error updating donor: " + e.getMessage());
            throw new RuntimeException("Failed to update donor: " + e.getMessage());
        }
    }

    @Override
    public void deleteDonor(String nic) {
        try {
            donorRepository.deleteById(nic);
        } catch (Exception e) {
            System.err.println("Error deleting donor: " + e.getMessage());
            throw new RuntimeException("Failed to delete donor: " + e.getMessage());
        }
    }

}