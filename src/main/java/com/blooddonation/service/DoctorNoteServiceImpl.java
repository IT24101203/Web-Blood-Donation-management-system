package com.blooddonation.service;

import com.blooddonation.model.DoctorNote;
import com.blooddonation.repository.DoctorNoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DoctorNoteServiceImpl implements DoctorNoteService {

    @Autowired
    private DoctorNoteRepository doctorNoteRepository;

    @Override
    public DoctorNote saveDoctorNote(DoctorNote doctorNote) {
        return doctorNoteRepository.save(doctorNote);
    }

    @Override
    public List<DoctorNote> getAllDoctorNotes() {
        return doctorNoteRepository.findAll();
    }

    @Override
    public List<DoctorNote> getDoctorNotesByDonorNic(String donorNic) {
        return doctorNoteRepository.findByDonorNicOrderByCreatedAtDesc(donorNic);
    }

    @Override
    public List<DoctorNote> getDoctorNotesByOutcome(String outcome) {
        return doctorNoteRepository.findByOutcomeOrderByCreatedAtDesc(outcome);
    }

    // Follow-up methods removed since these fields don't exist in the database

    @Override
    public Optional<DoctorNote> getDoctorNoteById(Long noteId) {
        return doctorNoteRepository.findById(noteId);
    }

    @Override
    public DoctorNote updateDoctorNote(DoctorNote doctorNote) {
        return doctorNoteRepository.save(doctorNote);
    }

    @Override
    public void deleteDoctorNote(Long noteId) {
        doctorNoteRepository.deleteById(noteId);
    }

    @Override
    public long getTotalNotesCount() {
        return doctorNoteRepository.count();
    }

    @Override
    public long getNotesCountByOutcome(String outcome) {
        return doctorNoteRepository.countByOutcome(outcome);
    }

    @Override
    public List<DoctorNote> getRecentNotes() {
        java.time.LocalDateTime thirtyDaysAgo = java.time.LocalDateTime.now().minusDays(30);
        return doctorNoteRepository.findRecentNotes(thirtyDaysAgo);
    }

    // CreatedBy methods removed since this field doesn't exist in the database
}
