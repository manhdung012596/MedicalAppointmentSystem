package com.clinic.config;

import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import com.clinic.model.Role;
import com.clinic.model.User;
import com.clinic.repository.UserRepository;

@Component
public class DataInit {

    private final UserRepository userRepository;
    private final com.clinic.repository.DoctorRepository doctorRepository;
    private final com.clinic.repository.PatientRepository patientRepository;
    private final com.clinic.repository.AppointmentRepository appointmentRepository;
    private final com.clinic.repository.MedicineRepository medicineRepository;
    private final com.clinic.repository.DepartmentRepository departmentRepository;
    private final com.clinic.repository.RoomRepository roomRepository;

    public DataInit(UserRepository userRepository,
            com.clinic.repository.DoctorRepository doctorRepository,
            com.clinic.repository.PatientRepository patientRepository,
            com.clinic.repository.AppointmentRepository appointmentRepository,
            com.clinic.repository.MedicineRepository medicineRepository,
            com.clinic.repository.DepartmentRepository departmentRepository,
            com.clinic.repository.RoomRepository roomRepository) {
        this.userRepository = userRepository;
        this.doctorRepository = doctorRepository;
        this.patientRepository = patientRepository;
        this.appointmentRepository = appointmentRepository;
        this.medicineRepository = medicineRepository;
        this.departmentRepository = departmentRepository;
        this.roomRepository = roomRepository;
    }

    @Bean
    public ApplicationRunner init() {
        return args -> {
            try {
                System.out.println(">>> DataInit STARTED <<<");
                Thread.sleep(1000); // Wait for schema creation

                // 1. Tạo Admin
                if (!userRepository.existsByEmail("admin@clinic.com")) {
                    User admin = new User();
                    admin.setEmail("admin@clinic.com");
                    admin.setPassword("admin123");
                    admin.setName("Administrator");
                    admin.setRole(Role.ADMIN);
                    userRepository.save(admin);
                    System.out.println("✓ Admin user created!");
                }

                // 2. Tạo Bác sĩ Mẫu
                if (doctorRepository.count() == 0) {
                    com.clinic.model.Doctor doc = new com.clinic.model.Doctor();
                    doc.setEmail("doctor@clinic.com");
                    doc.setPassword("123456");
                    doc.setName("Doctor Strange");
                    doc.setFullName("Dr. Stephen Strange");
                    doc.setRole(Role.DOCTOR);
                    doc.setSpecialty("Thần Kinh"); // Neurology
                    doc.setPhone("0999888777");
                    doc.setGender("Nam");
                    doctorRepository.save(doc);
                    System.out.println("✓ Sample Doctor created!");
                }

                // 3. Tạo Khoa & Phòng (Matching Frontend)
                if (departmentRepository.count() == 0) {
                    String[] deptNames = { "Nội khoa", "Ngoại khoa", "Nhi khoa", "Da liễu", "Răng hàm mặt",
                            "Tai mũi họng", "Sản phụ khoa", "Mắt" };

                    for (String name : deptNames) {
                        com.clinic.model.Department dep = new com.clinic.model.Department();
                        dep.setName(name);
                        dep.setDescription("Chuyên khoa " + name);
                        departmentRepository.save(dep);

                        // Create a room for each department
                        com.clinic.model.Room room = new com.clinic.model.Room();
                        room.setName("Phòng khám " + name);
                        room.setDepartment(dep);
                        roomRepository.save(room);
                    }
                    System.out.println("✓ Sample Departments & Rooms created!");
                }

                // 4. Tạo Bệnh nhân Mẫu & Lịch hẹn
                if (patientRepository.count() == 0) {
                    com.clinic.model.Patient pat = new com.clinic.model.Patient();
                    pat.setFullName("Nguyen Van A");
                    pat.setEmail("patient@email.com");
                    pat.setPhone("0912345678");
                    pat.setDob(java.time.LocalDate.parse("1990-01-01"));
                    pat.setSymptom("Đau đầu, chóng mặt");
                    pat.setAddress("Hà Nội");
                    pat.setPassword("123456"); // Default password for testing
                    patientRepository.save(pat);
                    System.out.println("✓ Sample Patient created!");

                    // Tạo Lịch hẹn (Dependent on Patient and Department)
                    if (appointmentRepository.count() == 0) {
                        com.clinic.model.Appointment appt = new com.clinic.model.Appointment();
                        appt.setPatient(pat);
                        appt.setDoctor(doctorRepository.findAll().get(0));
                        appt.setDepartment(departmentRepository.findAll().get(0));
                        appt.setRoom(roomRepository.findAll().get(0));
                        appt.setAppointmentTime(java.time.LocalDateTime.now().plusDays(1));
                        appt.setAppointmentType("KHAM_BENH");
                        appt.setStatus(com.clinic.model.enums.AppointmentStatus.PENDING);
                        appointmentRepository.save(appt);
                        System.out.println("✓ Sample Appointment created!");
                    }
                }

                // 5. Tạo Thuốc Mẫu
                if (medicineRepository.count() == 0) {
                    com.clinic.model.Medicine med = new com.clinic.model.Medicine();
                    med.setName("Paracetamol 500mg");
                    med.setUnit("Viên");
                    med.setPrice(1000.0);
                    med.setDescription("Thuốc giảm đau, hạ sốt");
                    medicineRepository.save(med);
                    System.out.println("✓ Sample Medicine created!");
                }
            } catch (Exception e) {
                System.out.println("❌ Error initializing data: " + e.getMessage());
                e.printStackTrace();
            } finally {
                System.out.println(">>> DataInit FINISHED <<<");
            }
        };
    }
}
