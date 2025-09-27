package com.blooddonation.service;

import com.blooddonation.model.Donor;
import com.blooddonation.dto.DonorRegistrationRequest;
import java.util.List;
import java.util.Optional;

public interface DonorService {
    Donor registerDonor(DonorRegistrationRequest request);
    boolean isDonorExists(String nic);
    Donor authenticateDonor(String nic, String password);
    List<Donor> getAllDonors();
    Optional<Donor> getDonorByNic(String nic);
    Donor updateDonor(Donor donor);
    void deleteDonor(String nic);
}