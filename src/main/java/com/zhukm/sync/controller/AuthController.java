package com.zhukm.sync.controller;

import com.zhukm.sync.dto.*;
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
    public ResponseEntity<ApiResponse<LoginResponse>> login(@Valid @RequestBody LoginRequest loginRequest) {
        ApiResponse<LoginResponse> response = authService.login(loginRequest);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/user")
    public ResponseEntity<ApiResponse<UserResponse>> getSession() {
        ApiResponse<UserResponse> response = authService.getSession();
        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        return ResponseEntity.ok().build();
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<String>> register(@Valid @RequestBody RegisterRequest registerRequest) {
        ApiResponse<String> response = authService.register(registerRequest);
        return ResponseEntity.ok(response);
    }
}