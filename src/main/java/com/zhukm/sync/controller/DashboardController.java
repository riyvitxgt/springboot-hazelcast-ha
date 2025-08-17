package com.zhukm.sync.controller;

import com.zhukm.sync.dto.ApiResponse;
import com.zhukm.sync.service.PermissionService;
import com.zhukm.sync.service.RoleService;
import com.zhukm.sync.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final UserService userService;
    private final RoleService roleService;
    private final PermissionService permissionService;

    @GetMapping("/stats")
    @PreAuthorize("hasAuthority('dashboard:read')")
    public ApiResponse<Map<String, Object>> getStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("userCount", userService.getUserCount());
        stats.put("roleCount", roleService.getRoleCount());
        stats.put("permissionCount", permissionService.getPermissionCount());
        stats.put("onlineCount", 1); // 这里可以实现在线用户统计

        return ApiResponse.success(stats);
    }
}