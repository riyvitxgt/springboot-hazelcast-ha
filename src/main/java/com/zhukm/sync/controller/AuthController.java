package com.zhukm.sync.controller;

import com.zhukm.sync.dto.ApiResponse;
import com.zhukm.sync.dto.LoginRequest;
import com.zhukm.sync.dto.LoginResponse;
import com.zhukm.sync.dto.UserDTO;
import com.zhukm.sync.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ApiResponse<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        LoginResponse response = authService.login(request);
        return ApiResponse.success("登录成功", response);
    }

    @PostMapping("/logout")
    public ApiResponse<Void> logout() {
        return ApiResponse.success("退出成功", null);
    }

    @GetMapping("/session")
    public ApiResponse<UserDTO> getSession() {
        UserDTO user = authService.getCurrentUser();
        return ApiResponse.success(user);
    }
}
