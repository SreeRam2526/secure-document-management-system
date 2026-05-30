package com.sreeram.documentmanagementsystem.controller;

import com.sreeram.documentmanagementsystem.dto.LoginRequest;
import com.sreeram.documentmanagementsystem.dto.RegisterRequest;
import com.sreeram.documentmanagementsystem.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import com.sreeram.documentmanagementsystem.dto.ApiResponse;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ApiResponse register(
            @Valid @RequestBody RegisterRequest request
    ) {
        return authService.register(request);
    }

    @PostMapping("/login")
    public ApiResponse login(
            @Valid @RequestBody LoginRequest request
    ) {
        return authService.login(request);
    }
}