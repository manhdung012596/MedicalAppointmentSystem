package com.clinic.model;

import jakarta.persistence.*;

@Entity
@Table(name = "doctors")
public class Doctor extends User {

    @Column(name = "full_name")
    private String fullName;

    private String phone;

    private String gender;

    @Column(name = "specialty")
    private String specialty;

    @Column(name = "avatar_url")
    private String avatarUrl = "/static/anh/default-avatar.jpg";

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getSpecialty() {
        return specialty;
    }

    public void setSpecialty(String specialty) {
        this.specialty = specialty;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }
}