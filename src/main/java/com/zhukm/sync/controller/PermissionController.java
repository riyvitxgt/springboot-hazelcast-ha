package com.zhukm.sync.controller;

import com.zhukm.sync.dto.ApiResponse;
import com.zhukm.sync.dto.PermissionDTO;
import com.zhukm.sync.service.PermissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/permissions")
@RequiredArgsConstructor
public class PermissionController {

    private final PermissionService permissionService;

    @GetMapping
    @PreAuthorize("hasAuthority('permission:read')")
    public ApiResponse<List<PermissionDTO>> getAllPermissions() {
        List<PermissionDTO> permissions = permissionService.getAllPermissions();
        return ApiResponse.success(permissions);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('permission:read')")
    public ApiResponse<PermissionDTO> getPermissionById(@PathVariable Long id) {
        PermissionDTO permission = permissionService.getPermissionById(id);
        return ApiResponse.success(permission);
    }
}