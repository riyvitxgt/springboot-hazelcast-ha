package com.zhukm.sync.controller;

import com.zhukm.sync.dto.ApiResponse;
import com.zhukm.sync.dto.AuthResponse;
import com.zhukm.sync.dto.LoginRequest;
import com.zhukm.sync.dto.RegisterRequest;
import com.zhukm.sync.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", maxAge = 3600)
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@Valid @RequestBody LoginRequest loginRequest) {
        ApiResponse<AuthResponse> response = authService.login(loginRequest);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<String>> register(@Valid @RequestBody RegisterRequest registerRequest) {
        ApiResponse<String> response = authService.register(registerRequest);
        return ResponseEntity.ok(response);
    }
}