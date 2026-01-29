package com.clinic.service;

import com.clinic.dto.LoginRequest;
import com.clinic.model.User;
import com.clinic.model.Role;
import com.clinic.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    @Autowired
    private UserRepository repo;

    public User login(LoginRequest req) {
        User u = repo.findByEmail(req.getEmail())
                .orElseThrow(() -> new RuntimeException("Email không tồn tại"));
        if (!u.getPassword().equals(req.getPassword())) {
            throw new RuntimeException("Sai mật khẩu");
        }
        return u;
    }

    public void register(User user) {
        if (repo.findByEmail(user.getEmail()).isPresent()) {
            throw new RuntimeException("Email đã tồn tại");
        }
        if (user.getRole() == null) {
            user.setRole(Role.PATIENT);
        }
        repo.save(user);
    }
}
