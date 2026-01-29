package com.clinic.service;

import com.clinic.model.Medicine;
import com.clinic.repository.MedicineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MedicineService {

    @Autowired
    private MedicineRepository medicineRepository;

    public List<Medicine> findAll() {
        return medicineRepository.findAll();
    }

    public Medicine save(Medicine medicine) {
        return medicineRepository.save(medicine);
    }

    public java.util.Optional<Medicine> findById(Long id) {
        return medicineRepository.findById(id);
    }

    public void delete(Long id) {
        medicineRepository.deleteById(id);
    }
}
