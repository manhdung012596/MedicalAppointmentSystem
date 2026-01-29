package com.clinic.controller;

import com.clinic.dto.LoginRequest;
import com.clinic.model.User;
import com.clinic.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin
public class AuthController {
    @Autowired
    private AuthService service;

    @PostMapping("/login")
    public User login(@RequestBody LoginRequest req) {
        return service.login(req);
    }

    @PostMapping("/register")
    public String register(@RequestBody User user) {
        service.register(user);
        return "Đăng ký thành công";
    }
}
