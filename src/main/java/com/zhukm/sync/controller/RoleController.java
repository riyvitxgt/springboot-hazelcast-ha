package com.zhukm.sync.controller;

import com.zhukm.sync.dto.ApiResponse;
import com.zhukm.sync.dto.RoleCreateRequest;
import com.zhukm.sync.dto.RoleDTO;
import com.zhukm.sync.service.RoleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/roles")
@RequiredArgsConstructor
public class RoleController {

    private final RoleService roleService;

    @GetMapping
    @PreAuthorize("hasAuthority('role:read')")
    public ApiResponse<List<RoleDTO>> getAllRoles() {
        List<RoleDTO> roles = roleService.getAllRoles();
        return ApiResponse.success(roles);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('role:read')")
    public ApiResponse<RoleDTO> getRoleById(@PathVariable Long id) {
        RoleDTO role = roleService.getRoleById(id);
        return ApiResponse.success(role);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('role:create')")
    public ApiResponse<RoleDTO> createRole(@Valid @RequestBody RoleCreateRequest request) {
        RoleDTO role = roleService.createRole(request);
        return ApiResponse.success("角色创建成功", role);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('role:update')")
    public ApiResponse<RoleDTO> updateRole(@PathVariable Long id, @Valid @RequestBody RoleCreateRequest request) {
        RoleDTO role = roleService.updateRole(id, request);
        return ApiResponse.success("角色更新成功", role);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('role:delete')")
    public ApiResponse<Void> deleteRole(@PathVariable Long id) {
        roleService.deleteRole(id);
        return ApiResponse.success("角色删除成功", null);
    }
}