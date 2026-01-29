package com.clinic.controller;

import com.clinic.model.*; // import các entity: Doctor, Patient, Appointment, Medicine, Department, Room...
import com.clinic.service.*; // import các service
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class AdminController {

    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private DoctorService doctorService;

    @Autowired
    private PatientService patientService;

    @Autowired
    private MedicineService medicineService;

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private RoomService roomService;

    // Dashboard (Tổng quan)
    @GetMapping("/admin/dashboard")
    public String dashboard(Model model) {
        // Dữ liệu từ service (sau này)
        // Dữ liệu từ service (sau này)
        model.addAttribute("totalAppointments", appointmentService.countAll());
        model.addAttribute("activeDoctors", doctorService.countActive());
        model.addAttribute("totalPatients", patientService.countAll());
        model.addAttribute("pendingAppointments", appointmentService.countPending());

        // Truyền tên fragment content
        model.addAttribute("content", "admin/dashboard");

        model.addAttribute("page_title", "Tổng Quan");

        return "layout/admin-layout";
    }

    // Quản lý lịch hẹn
    @GetMapping("/admin/appointments")
    public String showAppointmentSchedule(Model model) {
        // ... (data loading code remains same) ...
        model.addAttribute("patients", patientService.findAll());
        model.addAttribute("doctors", doctorService.findAll());
        model.addAttribute("departments", departmentService.findAll());
        model.addAttribute("rooms", roomService.findAll());

        model.addAttribute("appointments", appointmentService.findAll());
        if (!model.containsAttribute("appointmentForm")) {
            model.addAttribute("appointmentForm", new AppointmentForm());
        }

        model.addAttribute("page_title", "Quản Lý Lịch Khám");
        model.addAttribute("content", "admin/appointment_schedule");
        return "layout/admin-layout";
    }

    @PostMapping("/admin/appointments/save")
    public String saveAppointment(@org.springframework.web.bind.annotation.ModelAttribute AppointmentForm form,
            org.springframework.web.servlet.mvc.support.RedirectAttributes ra) {
        try {
            Appointment appointment = new Appointment();
            if (form.getId() != null) {
                appointment = appointmentService.findById(form.getId()).orElse(new Appointment());
            }

            // Map fields from Form to Entity
            if (form.getPatientId() != null)
                appointment.setPatient(patientService.findById(form.getPatientId()).orElse(null));
            if (form.getDoctorId() != null)
                appointment.setDoctor(doctorService.findById(form.getDoctorId()).orElse(null));
            if (form.getDepartmentId() != null)
                appointment.setDepartment(departmentService.findById(form.getDepartmentId()).orElse(null));
            if (form.getRoomId() != null)
                appointment.setRoom(roomService.findById(form.getRoomId()).orElse(null));

            if (form.getAppointmentDate() != null) {
                java.time.LocalTime time = form.getAppointmentTime() != null ? form.getAppointmentTime()
                        : java.time.LocalTime.of(8, 0);
                appointment.setAppointmentTime(java.time.LocalDateTime.of(form.getAppointmentDate(), time));
            }

            appointment.setAppointmentType(form.getAppointmentType()); // String vs Enum check? Assuming String
                                                                       // compatible
            appointment.setSymptom(form.getSymptom());

            if (form.getStatus() != null) {
                appointment.setStatus(form.getStatus());
            } else {
                appointment.setStatus(com.clinic.model.enums.AppointmentStatus.PENDING);
            }

            appointmentService.save(appointment);
            ra.addFlashAttribute("successMessage", "Lưu lịch khám thành công!");
        } catch (Exception e) {
            ra.addFlashAttribute("errorMessage", "Lỗi lưu lịch khám: " + e.getMessage());
        }
        return "redirect:/admin/appointments";
    }

    @GetMapping("/admin/appointments/{id}/edit")
    public String editAppointment(@org.springframework.web.bind.annotation.PathVariable Long id,
            org.springframework.web.servlet.mvc.support.RedirectAttributes ra) {
        Appointment apt = appointmentService.findById(id).orElse(null);
        if (apt != null) {
            AppointmentForm form = new AppointmentForm();
            form.setId(apt.getId());
            if (apt.getPatient() != null)
                form.setPatientId(apt.getPatient().getId());
            if (apt.getDoctor() != null)
                form.setDoctorId(apt.getDoctor().getId());
            if (apt.getDepartment() != null)
                form.setDepartmentId(apt.getDepartment().getId());
            if (apt.getRoom() != null)
                form.setRoomId(apt.getRoom().getId());

            if (apt.getAppointmentTime() != null) {
                form.setAppointmentDate(apt.getAppointmentTime().toLocalDate());
                form.setAppointmentTime(apt.getAppointmentTime().toLocalTime());
            }
            form.setAppointmentType(apt.getAppointmentType());
            form.setSymptom(apt.getSymptom());
            form.setStatus(apt.getStatus());

            ra.addFlashAttribute("appointmentForm", form);
            // No editMode needed because valid form populates the fields directly?
            // Actually, we reuse the same form on the page. Use editMode flag if modal
            // logic exists?
            // checking template: no modal logic seen for edits, it seems to just populate
            // the form at the top?
            // Re-checking template: The form IS in a card at the top, not a modal.
            // So simply populating the 'appointmentForm' model attribute in the redirect
            // (flash attribute) works!
        } else {
            ra.addFlashAttribute("errorMessage", "Không tìm thấy lịch khám!");
        }
        return "redirect:/admin/appointments";
    }

    @GetMapping("/admin/appointments/{id}/delete")
    public String deleteAppointment(@org.springframework.web.bind.annotation.PathVariable Long id,
            org.springframework.web.servlet.mvc.support.RedirectAttributes ra) {
        try {
            appointmentService.delete(id);
            ra.addFlashAttribute("successMessage", "Xóa lịch khám thành công!");
        } catch (Exception e) {
            ra.addFlashAttribute("errorMessage", "Không thể xóa: " + e.getMessage());
        }
        return "redirect:/admin/appointments";
    }

    // Quản lý bác sĩ
    @GetMapping("/admin/doctors")
    public String showDoctors(Model model) {
        // Lấy danh sách bác sĩ từ database (khi có service)
        List<Doctor> doctors = doctorService.findAll();
        model.addAttribute("doctors", doctors);

        // Form backing object cho modal thêm/sửa
        if (!model.containsAttribute("doctorForm")) {
            model.addAttribute("doctorForm", new Doctor());
        }

        model.addAttribute("page_title", "Quản Lý Bác Sĩ");
        model.addAttribute("content", "admin/doctor");

        return "layout/admin-layout";
    }

    @PostMapping("/admin/doctors/save")
    public String saveDoctor(@org.springframework.web.bind.annotation.ModelAttribute Doctor doctor,
            org.springframework.web.servlet.mvc.support.RedirectAttributes ra) {
        try {
            doctor.setRole(Role.DOCTOR);
            if (doctor.getId() == null) {
                // Thêm mới
                doctor.setPassword("123456"); // Mặc định
                doctor.setName(doctor.getFullName()); // Sync name
                // Auto-generate email if not provided
                if (doctor.getEmail() == null || doctor.getEmail().trim().isEmpty()) {
                    doctor.setEmail("doctor_" + System.currentTimeMillis() + "@clinic.com");
                }
            } else {
                // Cập nhật
                Doctor existing = doctorService.findById(doctor.getId()).orElse(null);
                if (existing != null) {
                    doctor.setPassword(existing.getPassword());
                    doctor.setAvatarUrl(existing.getAvatarUrl()); // Giữ nguyên avatar
                    doctor.setName(doctor.getFullName()); // Sync name
                    // Keep existing email if new email is empty
                    if (doctor.getEmail() == null || doctor.getEmail().trim().isEmpty()) {
                        doctor.setEmail(existing.getEmail());
                    }
                }
            }
            doctorService.save(doctor);
            ra.addFlashAttribute("successMessage", "Lưu bác sĩ thành công!");
        } catch (Exception e) {
            ra.addFlashAttribute("errorMessage", "Lỗi: " + e.getMessage());
        }
        return "redirect:/admin/doctors";
    }

    @GetMapping("/admin/doctors/{id}/edit")
    public String editDoctor(@org.springframework.web.bind.annotation.PathVariable Long id,
            org.springframework.web.servlet.mvc.support.RedirectAttributes ra) {
        Doctor doctor = doctorService.findById(id).orElse(null);
        if (doctor != null) {
            ra.addFlashAttribute("doctorForm", doctor);
            ra.addFlashAttribute("editMode", true);
        } else {
            ra.addFlashAttribute("errorMessage", "Không tìm thấy bác sĩ!");
        }
        return "redirect:/admin/doctors";
    }

    @GetMapping("/admin/doctors/{id}/delete")
    public String deleteDoctor(@org.springframework.web.bind.annotation.PathVariable Long id,
            org.springframework.web.servlet.mvc.support.RedirectAttributes ra) {
        try {
            doctorService.delete(id);
            ra.addFlashAttribute("successMessage", "Xóa bác sĩ thành công!");
        } catch (Exception e) {
            ra.addFlashAttribute("errorMessage", "Không thể xóa bác sĩ này (có thể đã có lịch khám)!");
        }
        return "redirect:/admin/doctors";
    }

    // Quản lý bệnh nhân
    @GetMapping("/admin/patients")
    public String showPatients(Model model) {
        model.addAttribute("patients", patientService.findAll());
        if (!model.containsAttribute("patientForm")) {
            model.addAttribute("patientForm", new Patient());
        }
        model.addAttribute("page_title", "Quản Lý Bệnh Nhân");
        model.addAttribute("content", "admin/patient");
        return "layout/admin-layout";
    }

    @PostMapping("/admin/patients/save")
    public String savePatient(@org.springframework.web.bind.annotation.ModelAttribute Patient patient,
            org.springframework.web.servlet.mvc.support.RedirectAttributes ra) {
        try {
            patient.setRole(Role.PATIENT);
            if (patient.getId() == null) {
                // Thêm mới từ Admin (mật khẩu mặc định?)
                patient.setPassword("123456");
                patient.setName(patient.getFullName());
                // Auto-generate email if not provided
                if (patient.getEmail() == null || patient.getEmail().trim().isEmpty()) {
                    patient.setEmail("patient_" + System.currentTimeMillis() + "@clinic.com");
                }
            } else {
                Patient existing = patientService.findById(patient.getId()).orElse(null);
                if (existing != null) {
                    patient.setPassword(existing.getPassword());
                    patient.setName(patient.getFullName());
                    // Keep existing email if new email is empty
                    if (patient.getEmail() == null || patient.getEmail().trim().isEmpty()) {
                        patient.setEmail(existing.getEmail());
                    }
                }
            }
            patientService.save(patient);
            ra.addFlashAttribute("successMessage", "Lưu bệnh nhân thành công!");
        } catch (Exception e) {
            ra.addFlashAttribute("errorMessage", "Lỗi: " + e.getMessage());
        }
        return "redirect:/admin/patients";
    }

    @GetMapping("/admin/patients/{id}/edit")
    public String editPatient(@org.springframework.web.bind.annotation.PathVariable Long id,
            org.springframework.web.servlet.mvc.support.RedirectAttributes ra) {
        Patient patient = patientService.findById(id).orElse(null);
        if (patient != null) {
            ra.addFlashAttribute("patientForm", patient);
            ra.addFlashAttribute("editMode", true);
        } else {
            ra.addFlashAttribute("errorMessage", "Không tìm thấy bệnh nhân!");
        }
        return "redirect:/admin/patients";
    }

    @GetMapping("/admin/patients/{id}/delete")
    public String deletePatient(@org.springframework.web.bind.annotation.PathVariable Long id,
            org.springframework.web.servlet.mvc.support.RedirectAttributes ra) {
        try {
            patientService.delete(id);
            ra.addFlashAttribute("successMessage", "Xóa bệnh nhân thành công!");
        } catch (Exception e) {
            ra.addFlashAttribute("errorMessage", "Không thể xóa bệnh nhân này: " + e.getMessage());
        }
        return "redirect:/admin/patients";
    }

    // Quản lý đơn thuốc
    @GetMapping("/admin/medicines")
    public String showMedicines(Model model) {
        model.addAttribute("medicines", medicineService.findAll());
        if (!model.containsAttribute("medicineForm")) {
            model.addAttribute("medicineForm", new Medicine());
        }
        model.addAttribute("page_title", "Quản Lý Thuốc");
        model.addAttribute("content", "admin/medicine");
        return "layout/admin-layout";
    }

    @PostMapping("/admin/medicines/save")
    public String saveMedicine(@org.springframework.web.bind.annotation.ModelAttribute Medicine medicine,
            org.springframework.web.servlet.mvc.support.RedirectAttributes ra) {
        try {
            medicineService.save(medicine);
            ra.addFlashAttribute("successMessage", "Lưu thuốc thành công!");
        } catch (Exception e) {
            ra.addFlashAttribute("errorMessage", "Lỗi: " + e.getMessage());
        }
        return "redirect:/admin/medicines";
    }

    @GetMapping("/admin/medicines/{id}/edit")
    public String editMedicine(@org.springframework.web.bind.annotation.PathVariable Long id,
            org.springframework.web.servlet.mvc.support.RedirectAttributes ra) {
        Medicine medicine = medicineService.findById(id).orElse(null);
        if (medicine != null) {
            ra.addFlashAttribute("medicineForm", medicine);
            ra.addFlashAttribute("editMode", true);
        } else {
            ra.addFlashAttribute("errorMessage", "Không tìm thấy thuốc!");
        }
        return "redirect:/admin/medicines";
    }

    @GetMapping("/admin/medicines/{id}/delete")
    public String deleteMedicine(@org.springframework.web.bind.annotation.PathVariable Long id,
            org.springframework.web.servlet.mvc.support.RedirectAttributes ra) {
        try {
            medicineService.delete(id);
            ra.addFlashAttribute("successMessage", "Xóa thuốc thành công!");
        } catch (Exception e) {
            ra.addFlashAttribute("errorMessage", "Không thể xóa thuốc này: " + e.getMessage());
        }
        return "redirect:/admin/medicines";
    }

    // Quản lý khoa & phòng khám
    @GetMapping("/admin/departments")
    public String showDepartmentsAndRooms(Model model) {
        model.addAttribute("departments", departmentService.findAll());
        model.addAttribute("rooms", roomService.findAll());

        if (!model.containsAttribute("departmentForm")) {
            model.addAttribute("departmentForm", new Department());
        }
        if (!model.containsAttribute("roomForm")) {
            model.addAttribute("roomForm", new Room());
        }

        model.addAttribute("page_title", "Quản Lý Khoa và Phòng Khám");
        model.addAttribute("content", "admin/department-clinic");
        return "layout/admin-layout";
    }

    // --- Department CRUD ---
    @PostMapping("/admin/departments/save")
    public String saveDepartment(@org.springframework.web.bind.annotation.ModelAttribute Department department,
            org.springframework.web.servlet.mvc.support.RedirectAttributes ra) {
        try {
            departmentService.save(department);
            ra.addFlashAttribute("successMessage", "Lưu khoa thành công!");
        } catch (Exception e) {
            ra.addFlashAttribute("errorMessage", "Lỗi khi lưu khoa: " + e.getMessage());
        }
        return "redirect:/admin/departments";
    }

    @GetMapping("/admin/departments/{id}/edit")
    public String editDepartment(@org.springframework.web.bind.annotation.PathVariable Long id,
            org.springframework.web.servlet.mvc.support.RedirectAttributes ra) {
        Department dept = departmentService.findById(id).orElse(null);
        if (dept != null) {
            ra.addFlashAttribute("departmentForm", dept);
            ra.addFlashAttribute("editDeptMode", true);
        } else {
            ra.addFlashAttribute("errorMessage", "Không tìm thấy khoa!");
        }
        return "redirect:/admin/departments";
    }

    @GetMapping("/admin/departments/{id}/delete")
    public String deleteDepartment(@org.springframework.web.bind.annotation.PathVariable Long id,
            org.springframework.web.servlet.mvc.support.RedirectAttributes ra) {
        try {
            departmentService.delete(id);
            ra.addFlashAttribute("successMessage", "Xóa khoa thành công!");
        } catch (Exception e) {
            ra.addFlashAttribute("errorMessage",
                    "Không thể xóa khoa này (có thể đang có phòng hoặc lịch khám liên kết)!");
        }
        return "redirect:/admin/departments";
    }

    // --- Room CRUD ---
    @PostMapping("/admin/rooms/save")
    public String saveRoom(@org.springframework.web.bind.annotation.ModelAttribute Room room,
            org.springframework.web.servlet.mvc.support.RedirectAttributes ra) {
        try {
            if (room.getDepartmentId() != null) {
                Department dept = departmentService.findById(room.getDepartmentId()).orElse(null);
                room.setDepartment(dept);
            }
            roomService.save(room);
            ra.addFlashAttribute("successMessage", "Lưu phòng khám thành công!");
        } catch (Exception e) {
            ra.addFlashAttribute("errorMessage", "Lỗi khi lưu phòng: " + e.getMessage());
        }
        return "redirect:/admin/departments";
    }

    @GetMapping("/admin/rooms/{id}/edit")
    public String editRoom(@org.springframework.web.bind.annotation.PathVariable Long id,
            org.springframework.web.servlet.mvc.support.RedirectAttributes ra) {
        Room room = roomService.findById(id).orElse(null);
        if (room != null) {
            ra.addFlashAttribute("roomForm", room);
            ra.addFlashAttribute("editRoomMode", true);
        } else {
            ra.addFlashAttribute("errorMessage", "Không tìm thấy phòng!");
        }
        return "redirect:/admin/departments";
    }

    @GetMapping("/admin/rooms/{id}/delete")
    public String deleteRoom(@org.springframework.web.bind.annotation.PathVariable Long id,
            org.springframework.web.servlet.mvc.support.RedirectAttributes ra) {
        try {
            roomService.delete(id);
            ra.addFlashAttribute("successMessage", "Xóa phòng thành công!");
        } catch (Exception e) {
            ra.addFlashAttribute("errorMessage", "Không thể xóa phòng này: " + e.getMessage());
        }
        return "redirect:/admin/departments";
    }

    // Quản lý Khách đặt lịch online
    @GetMapping("/admin/online-bookings")
    public String showOnlineBookings(Model model) {
        model.addAttribute("appointments", appointmentService.findPending());
        model.addAttribute("page_title", "Danh Sách Khách Đặt Lịch");
        model.addAttribute("content", "admin/online_bookings");
        return "layout/admin-layout";
    }

    // Add pending count globally for sidebar badge
    @org.springframework.web.bind.annotation.ModelAttribute
    public void addAttributes(Model model) {
        model.addAttribute("pendingCount", appointmentService.countPending());
    }
}
