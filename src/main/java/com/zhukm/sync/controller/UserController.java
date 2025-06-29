package com.zhukm.sync.controller;

import com.zhukm.sync.dto.ApiResponse;
import com.zhukm.sync.dto.UserResponse;
import com.zhukm.sync.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", maxAge = 3600)
public class UserController {

    private final UserService userService;

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('USER_READ') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<UserResponse>> getUserById(@PathVariable Long id) {
        ApiResponse<UserResponse> response = userService.getUserById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/profile")
    @PreAuthorize("hasAuthority('USER_READ')")
    public ResponseEntity<String> userProfile() {
        return ResponseEntity.ok("User profile - accessible to users with USER_READ permission");
    }
}