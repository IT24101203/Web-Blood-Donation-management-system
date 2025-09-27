package com.blooddonation.controller;

import com.blooddonation.model.Donor;
import com.blooddonation.dto.DonorRegistrationRequest;
import com.blooddonation.dto.ApiResponse;
import com.blooddonation.service.DonorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/donors")
@CrossOrigin(origins = "*")
public class DonorController {

    private final DonorService donorService;

    @Autowired
    public DonorController(DonorService donorService) {
        this.donorService = donorService;
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse> registerDonor(@Valid @RequestBody DonorRegistrationRequest request) {
        try {
            Donor donor = donorService.registerDonor(request);
            return ResponseEntity.ok(
                    new ApiResponse(true, "Registration successful! Your donor ID: " + donor.getNic(), donor)
            );
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    new ApiResponse(false, e.getMessage(), null)
            );
        }
    }

    @GetMapping("/check-nic/{nic}")
    public ResponseEntity<ApiResponse> checkNicExists(@PathVariable String nic) {
        boolean exists = donorService.isDonorExists(nic);
        return ResponseEntity.ok(
                new ApiResponse(true, exists ? "NIC already exists" : "NIC available", exists)
        );
    }
}