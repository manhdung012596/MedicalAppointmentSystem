package com.clinic.controller;

import com.clinic.model.Appointment;
import com.clinic.model.Patient;
import com.clinic.model.Department;
import com.clinic.model.enums.AppointmentStatus;
import com.clinic.service.AppointmentService;
import com.clinic.service.DepartmentService;
import com.clinic.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Map;

@RestController
@RequestMapping("/api/appointments")
public class BookingAPIController {

    @Autowired
    private PatientService patientService;
    @Autowired
    private AppointmentService appointmentService;
    @Autowired
    private DepartmentService departmentService;

    @PostMapping("/book")
    public ResponseEntity<?> bookAppointment(@RequestBody BookingRequest request) {
        try {
            // 1. Find or Create Patient
            Patient patient = new Patient();
            patient.setFullName(request.getFullName());
            patient.setPhone(request.getPhone());
            patient.setEmail(request.getEmail());
            patient.setAddress(request.getAddress());

            // Gender
            patient.setGender(request.getGender());

            // DOB Logic

            try {
                if (request.getDob() != null && !request.getDob().isEmpty()) {
                    patient.setDob(LocalDate.parse(request.getDob()));
                } else {
                    patient.setDob(LocalDate.now());
                }
            } catch (Exception e) {
                patient.setDob(LocalDate.now());
            }

            patientService.save(patient);

            // 2. Create Appointment
            Appointment appt = new Appointment();
            appt.setPatient(patient);

            // Find department by name or id (Legacy JS passes Department Name likely)
            // Let's try to map generic "Khoa Nội" if needed, or just pick first one
            // Ideally we need to find department by name
            if (request.getDepartment() != null) {
                Department dept = departmentService.findAll().stream()
                        .filter(d -> d.getName().equalsIgnoreCase(request.getDepartment()))
                        .findFirst().orElse(null);
                appt.setDepartment(dept);
                if (dept != null && !dept.getRooms().isEmpty()) {
                    appt.setRoom(dept.getRooms().get(0));
                }
            }

            // Set Date Time
            if (request.getAppointmentDate() != null) {
                // Legacy JS passes Date (yyyy-MM-dd). Default time to 08:00
                LocalDate date = LocalDate.parse(request.getAppointmentDate());
                appt.setAppointmentTime(LocalDateTime.of(date, LocalTime.of(8, 0)));
            } else {
                appt.setAppointmentTime(LocalDateTime.now().plusDays(1));
            }

            appt.setSymptom(request.getSymptoms());
            appt.setAppointmentType("KHAM_BENH");
            appt.setStatus(AppointmentStatus.PENDING);

            appointmentService.save(appt);

            return ResponseEntity.ok(Map.of("message", "Đặt lịch thành công! Chúng tôi sẽ liên hệ sớm."));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(Map.of("message", "Đặt lịch thất bại: " + e.getMessage()));
        }
    }

    // DTO Helper Class
    public static class BookingRequest {
        private String fullName;
        private String phone;
        private String email;
        private String address;
        private String department;
        private String appointmentDate;
        private String symptoms;
        private String dob;
        private String gender;

        // Getters Setters
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

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getDepartment() {
            return department;
        }

        public void setDepartment(String department) {
            this.department = department;
        }

        public String getAppointmentDate() {
            return appointmentDate;
        }

        public void setAppointmentDate(String appointmentDate) {
            this.appointmentDate = appointmentDate;
        }

        public String getSymptoms() {
            return symptoms;
        }

        public void setSymptoms(String symptoms) {
            this.symptoms = symptoms;
        }

        public String getDob() {
            return dob;
        }

        public void setDob(String dob) {
            this.dob = dob;
        }

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }
    }
}
