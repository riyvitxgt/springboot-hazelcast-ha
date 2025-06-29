package com.zhukm.sync;

import com.zhukm.sync.controller.AuthController;
import com.zhukm.sync.dto.ApiResponse;
import com.zhukm.sync.dto.AuthResponse;
import com.zhukm.sync.dto.LoginRequest;
import com.zhukm.sync.dto.RegisterRequest;
import com.zhukm.sync.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthControllerTest {

    @Mock
    private AuthService authService;

    @InjectMocks
    private AuthController authController;

    private LoginRequest loginRequest;
    private RegisterRequest registerRequest;

    @BeforeEach
    void setUp() {
        loginRequest = LoginRequest.builder().username("test@example.com").password("password123").build();
        registerRequest = RegisterRequest.builder().username("user").email("user@example.com").password("password123").build();
    }

    @Test
    void login_ShouldReturnOkResponse() {
        // 准备模拟响应
        ApiResponse<AuthResponse> mockResponse = ApiResponse.<AuthResponse>builder()
                .success(true)
                .message("Login successful")
                .build();

        // 模拟服务层调用
        when(authService.login(loginRequest)).thenReturn(mockResponse);

        // 执行测试
        ResponseEntity<ApiResponse<AuthResponse>> response = authController.login(loginRequest);

        // 验证结果
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isSuccess());
        verify(authService, times(1)).login(loginRequest);
    }

    @Test
    void register_ShouldReturnOkResponse() {
        // 准备模拟响应
        ApiResponse<String> mockResponse = ApiResponse.<String>builder()
                .success(true)
                .message("Registration successful")
                .build();

        // 模拟服务层调用
        when(authService.register(registerRequest)).thenReturn(mockResponse);

        // 执行测试
        ResponseEntity<ApiResponse<String>> response = authController.register(registerRequest);

        // 验证结果
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isSuccess());
        verify(authService, times(1)).register(registerRequest);
    }

    @Test
    void login_ShouldHandleServiceErrors() {
        // 模拟服务层抛出异常
        when(authService.login(loginRequest)).thenThrow(new RuntimeException("Service error"));

        // 执行并验证异常
        assertThrows(RuntimeException.class, () -> authController.login(loginRequest));
    }
}
