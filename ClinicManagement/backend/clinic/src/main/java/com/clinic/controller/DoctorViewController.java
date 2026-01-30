package com.clinic.controller;

import com.clinic.model.Doctor;
import com.clinic.model.Appointment;
import com.clinic.service.DoctorService;
import com.clinic.service.AppointmentService;
import com.clinic.service.MedicineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/doctor")
public class DoctorViewController {

    @Autowired
    private DoctorService doctorService;

    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private MedicineService medicineService;

    @GetMapping("/dashboard")
    public String dashboard() {
        return "redirect:/doctor/appointments";
    }

    @GetMapping("/personal")
    public String personalInfo(Model model, Principal principal) {
        // Mock data for display testing
        Doctor doctor = new Doctor();
        doctor.setFullName("Dr. Stephen Strange");
        doctor.setEmail("doctor@clinic.com");
        doctor.setPhone("0999888777");
        doctor.setSpecialty("Thần Kinh");

        model.addAttribute("doctor", doctor);
        model.addAttribute("doctorForm", doctor);

        model.addAttribute("page_title", "Thông tin cá nhân");
        model.addAttribute("contentTemplate", "doctor/personal");
        return "layout/doctor-layout";
    }

    @GetMapping("/appointments")
    public String appointments(Model model) {
        // Get appointments for current doctor
        // Mocking or fetching all for now
        List<Appointment> appointments = appointmentService.findAll(); // Should filter by doctor

        model.addAttribute("appointments", appointments);
        model.addAttribute("page_title", "Lịch khám");
        model.addAttribute("contentTemplate", "doctor/appointment_schedule");
        return "layout/doctor-layout";
    }

    @GetMapping("/prescribe")
    public String prescribe(Model model) {
        model.addAttribute("medicines", medicineService.findAll());
        model.addAttribute("page_title", "Kê đơn thuốc");
        model.addAttribute("contentTemplate", "doctor/prescribe_medicine");
        return "layout/doctor-layout";
    }
}
