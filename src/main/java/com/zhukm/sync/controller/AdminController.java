package com.zhukm.sync.controller;

import com.zhukm.sync.dto.ApiResponse;
import com.zhukm.sync.dto.UserResponse;
import com.zhukm.sync.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", maxAge = 3600)
public class AdminController {

    private final UserService userService;

    @GetMapping("/users")
    @PreAuthorize("hasRole('ADMIN') and hasAuthority('ADMIN_READ')")
    public ResponseEntity<ApiResponse<List<UserResponse>>> getAllUsers() {
        ApiResponse<List<UserResponse>> response = userService.getAllUsers();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/dashboard")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> adminDashboard() {
        return ResponseEntity.ok("Admin Dashboard - Only accessible to administrators");
    }
}