package com.clinic.service;

import com.clinic.model.Appointment;
import com.clinic.repository.AppointmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AppointmentService {

    @Autowired
    private AppointmentRepository repo;

    public java.util.List<Appointment> findAll() {
        return repo.findAll();
    }

    public java.util.Optional<Appointment> findById(Long id) {
        return repo.findById(id);
    }

    public Appointment save(Appointment appointment) {
        return repo.save(appointment);
    }

    public void delete(Long id) {
        repo.deleteById(id);
    }

    public long countAll() {
        return repo.count();
    }

    public long countPending() {
        return repo.findAll().stream().filter(a -> "PENDING".equals(a.getStatus().toString())).count();
    }

    public Appointment updateStatus(Long id, String status) {
        java.util.Optional<Appointment> opt = repo.findById(id);
        if (opt.isPresent()) {
            Appointment a = opt.get();
            a.setStatus(com.clinic.model.enums.AppointmentStatus.valueOf(status));
            return repo.save(a);
        }
        return null;
    }

    public java.util.List<Appointment> findPending() {
        return repo.findAll().stream()
                .filter(a -> com.clinic.model.enums.AppointmentStatus.PENDING.equals(a.getStatus()))
                .collect(java.util.stream.Collectors.toList());
    }
}
