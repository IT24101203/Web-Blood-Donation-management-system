package com.blooddonation.service;

import com.blooddonation.model.Receiver;

import java.util.List;
import java.util.Optional;

public interface ReceiverService {
    Receiver saveReceiver(Receiver receiver);
    Optional<Receiver> findByNic(String nic);
    boolean existsByNic(String nic);
    boolean existsByEmail(String email);
    Optional<Receiver> findByNicAndPassword(String nic, String password);
    List<Receiver> findAll();
    List<Receiver> findByBloodTypeNeeded(String bloodTypeNeeded);
    List<Receiver> findByHospitalPreference(String hospitalPreference);
    void deleteReceiver(String nic);
}
