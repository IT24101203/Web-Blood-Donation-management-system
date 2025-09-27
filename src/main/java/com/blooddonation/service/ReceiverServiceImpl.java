package com.blooddonation.service;

import com.blooddonation.model.Receiver;
import com.blooddonation.repository.ReceiverRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ReceiverServiceImpl implements ReceiverService {

    @Autowired
    private ReceiverRepository receiverRepository;

    @Override
    public Receiver saveReceiver(Receiver receiver) {
        return receiverRepository.save(receiver);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Receiver> findByNic(String nic) {
        return receiverRepository.findByNic(nic);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByNic(String nic) {
        return receiverRepository.existsByNic(nic);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByEmail(String email) {
        return receiverRepository.existsByEmail(email);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Receiver> findByNicAndPassword(String nic, String password) {
        return receiverRepository.findByNicAndPassword(nic, password);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Receiver> findAll() {
        return receiverRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Receiver> findByBloodTypeNeeded(String bloodTypeNeeded) {
        return receiverRepository.findByBloodTypeNeeded(bloodTypeNeeded);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Receiver> findByHospitalPreference(String hospitalPreference) {
        return receiverRepository.findByHospitalPreference(hospitalPreference);
    }

    @Override
    public void deleteReceiver(String nic) {
        receiverRepository.deleteById(nic);
    }
}
