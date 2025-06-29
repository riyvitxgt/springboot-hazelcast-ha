package com.zhukm.sync;

import com.zhukm.sync.controller.AdminController;
import com.zhukm.sync.dto.ApiResponse;
import com.zhukm.sync.dto.UserResponse;
import com.zhukm.sync.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdminControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private AdminController adminController;

    @Test
    void getAllUsers_ShouldReturnUsersList() {
        // 准备模拟数据
        UserResponse user = UserResponse.builder().id(1L).username("ADMIN").email("admin@example.com").build();
        List<UserResponse> userList = Collections.singletonList(user);
        ApiResponse<List<UserResponse>> mockResponse = ApiResponse.<List<UserResponse>>builder()
                .success(true)
                .data(userList)
                .build();

        // 模拟服务层调用
        when(userService.getAllUsers()).thenReturn(mockResponse);

        // 执行测试
        ResponseEntity<ApiResponse<List<UserResponse>>> response =
                adminController.getAllUsers();

        // 验证结果
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isSuccess());
        assertEquals(1, response.getBody().getData().size());
        verify(userService, times(1)).getAllUsers();
    }

    @Test
    void adminDashboard_ShouldReturnWelcomeMessage() {
        // 执行测试
        ResponseEntity<String> response = adminController.adminDashboard();

        // 验证结果
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Admin Dashboard - Only accessible to administrators", response.getBody());
    }

    @Test
    void getAllUsers_ShouldHandleEmptyResponse() {
        // 模拟空响应
        ApiResponse<List<UserResponse>> mockResponse = ApiResponse.<List<UserResponse>>builder()
                .success(true)
                .data(Collections.emptyList())
                .build();

        when(userService.getAllUsers()).thenReturn(mockResponse);

        // 执行测试
        ResponseEntity<ApiResponse<List<UserResponse>>> response =
                adminController.getAllUsers();

        // 验证结果
        assertTrue(response.getBody().isSuccess());
        assertTrue(response.getBody().getData().isEmpty());
    }

    @Test
    void getAllUsers_ShouldHandleServiceErrors() {
        // 模拟服务层异常
        when(userService.getAllUsers()).thenThrow(new RuntimeException("DB error"));

        // 执行并验证异常
        assertThrows(RuntimeException.class, () -> adminController.getAllUsers());
    }
}
