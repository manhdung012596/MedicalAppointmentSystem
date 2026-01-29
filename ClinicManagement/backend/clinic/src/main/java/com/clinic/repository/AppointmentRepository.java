package com.clinic.repository;

import com.clinic.model.Appointment;
import com.clinic.model.enums.AppointmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    List<Appointment> findByDoctorId(Long doctorId);

    List<Appointment> findByPatientId(Long patientId);

    // Check bác sĩ có bị trùng lịch không (bỏ qua lịch đã hủy)
    boolean existsByDoctorIdAndAppointmentTimeAndStatusNot(
            Long doctorId,
            LocalDateTime appointmentTime,
            AppointmentStatus status);

    // Check bệnh nhân có bị trùng lịch không (bỏ qua lịch đã hủy)
    boolean existsByPatientIdAndAppointmentTimeAndStatusNot(
            Long patientId,
            LocalDateTime appointmentTime,
            AppointmentStatus status);
}