package com.clinic.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@PrimaryKeyJoinColumn(name = "id")
public class Patient extends User {
    // id is inherited from User
    // email is inherited from User

    private String fullName;
    private String phone;
    private LocalDate dob;
    private String gender;
    private String symptom;
    private String address;

    public Patient() {
        super();
        this.setRole(com.clinic.model.Role.PATIENT);
    }

    public Patient(String fullName, String email, String phone, LocalDate dob, String symptom) {
        super();
        this.fullName = fullName;
        this.setEmail(email); // Set email in User
        this.phone = phone;
        this.dob = dob;
        this.symptom = symptom;
        this.setRole(com.clinic.model.Role.PATIENT);
    }

    // Getters/Setters for inherited fields (id, email, password, role) are in User

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    // getEmail/setEmail are inherited

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public LocalDate getDob() {
        return dob;
    }

    public void setDob(LocalDate dob) {
        this.dob = dob;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getSymptom() {
        return symptom;
    }

    public void setSymptom(String symptom) {
        this.symptom = symptom;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
