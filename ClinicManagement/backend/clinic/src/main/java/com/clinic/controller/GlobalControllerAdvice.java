package com.clinic.controller;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice
public class GlobalControllerAdvice {

    @ModelAttribute("requestURI")
    public String addRequestURI(HttpServletRequest request) {
        return request.getRequestURI();
    }
}
