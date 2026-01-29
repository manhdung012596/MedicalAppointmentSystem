package com.clinic.controller;

import com.clinic.model.Appointment;
import com.clinic.service.AppointmentService;
import com.clinic.dto.AppointmentRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/appointments")
@CrossOrigin
public class AppointmentController {

    @Autowired
    private AppointmentService service;

    @GetMapping
    public java.util.List<Appointment> getAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public Appointment getById(@PathVariable Long id) {
        return service.findById(id).orElse(null);
    }

    /*
     * @PostMapping
     * public ResponseEntity<?> create(@RequestBody AppointmentRequest request) {
     * try {
     * Appointment appointment = new Appointment();
     * 
     * // Xử lý appointmentDate - convert từ String (yyyy-MM-dd) sang LocalDateTime
     * try {
     * String dateStr = request.getAppointmentDate();
     * if (dateStr != null && !dateStr.isEmpty()) {
     * // Thêm giờ mặc định nếu chỉ có ngày
     * if (dateStr.length() == 10) {
     * dateStr += " 09:00:00";
     * }
     * DateTimeFormatter formatter =
     * DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
     * appointment.setAppointmentTime(LocalDateTime.parse(dateStr, formatter));
     * }
     * } catch (Exception e) {
     * appointment.setAppointmentTime(LocalDateTime.now());
     * }
     * 
     * appointment.setSymptom(request.getSymptoms() != null ? request.getSymptoms()
     * : "");
     * 
     * Appointment saved = service.save(appointment);
     * 
     * Map<String, Object> response = new HashMap<>();
     * response.put("success", true);
     * response.put("message", "✓ Đặt lịch khám thành công!");
     * response.put("appointmentId", saved.getId());
     * response.put("appointmentTime", saved.getAppointmentTime());
     * 
     * return ResponseEntity.ok(response);
     * } catch (Exception e) {
     * Map<String, Object> error = new HashMap<>();
     * error.put("success", false);
     * error.put("message", "❌ Lỗi: " + e.getMessage());
     * return ResponseEntity.badRequest().body(error);
     * }
     * }
     */

    @PutMapping("/{id}")
    public Appointment update(@PathVariable Long id, @RequestBody Appointment a) {
        a.setId(id);
        return service.save(a);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
