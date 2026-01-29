package com.clinic.service;

import com.clinic.model.Department;
import com.clinic.repository.DepartmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DepartmentService {

    @Autowired
    private DepartmentRepository departmentRepository;

    public List<Department> findAll() {
        return departmentRepository.findAll();
    }

    public Department save(Department department) {
        return departmentRepository.save(department);
    }

    public java.util.Optional<Department> findById(Long id) {
        return departmentRepository.findById(id);
    }

    public void delete(Long id) {
        departmentRepository.deleteById(id);
    }
}
