package com.zhukm.sync.service;

import com.zhukm.sync.dto.RoleCreateRequest;
import com.zhukm.sync.dto.RoleDTO;
import com.zhukm.sync.entity.Permission;
import com.zhukm.sync.entity.Role;
import com.zhukm.sync.exception.ResourceNotFoundException;
import com.zhukm.sync.mapper.RoleMapper;
import com.zhukm.sync.repository.PermissionRepository;
import com.zhukm.sync.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class RoleService {

    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final RoleMapper roleMapper;

    @Transactional(readOnly = true)
    public List<RoleDTO> getAllRoles() {
        return roleRepository.findAll().stream()
                .map(roleMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public RoleDTO getRoleById(Long id) {
        Role role = roleRepository.findByIdWithPermissions(id)
                .orElseThrow(() -> new ResourceNotFoundException("角色不存在"));
        return roleMapper.toDTO(role);
    }

    public RoleDTO createRole(RoleCreateRequest request) {
        if (roleRepository.existsByCode(request.getCode())) {
            throw new RuntimeException("角色代码已存在");
        }

        if (roleRepository.existsByName(request.getName())) {
            throw new RuntimeException("角色名称已存在");
        }

        Role role = new Role();
        role.setName(request.getName());
        role.setCode(request.getCode());
        role.setDescription(request.getDescription());
        role.setEnabled(request.getEnabled());

        // 设置权限
        if (request.getPermissionIds() != null && !request.getPermissionIds().isEmpty()) {
            Set<Permission> permissions = new HashSet<>(
                    permissionRepository.findAllById(request.getPermissionIds())
            );
            role.setPermissions(permissions);
        }

        Role savedRole = roleRepository.save(role);
        return roleMapper.toDTO(savedRole);
    }

    public RoleDTO updateRole(Long id, RoleCreateRequest request) {
        Role role = roleRepository.findByIdWithPermissions(id)
                .orElseThrow(() -> new ResourceNotFoundException("角色不存在"));

        // 检查代码是否被其他角色使用
        if (!role.getCode().equals(request.getCode()) &&
                roleRepository.existsByCode(request.getCode())) {
            throw new RuntimeException("角色代码已存在");
        }

        // 检查名称是否被其他角色使用
        if (!role.getName().equals(request.getName()) &&
                roleRepository.existsByName(request.getName())) {
            throw new RuntimeException("角色名称已存在");
        }

        role.setName(request.getName());
        role.setCode(request.getCode());
        role.setDescription(request.getDescription());
        role.setEnabled(request.getEnabled());

        // 更新权限
        if (request.getPermissionIds() != null) {
            Set<Permission> permissions = new HashSet<>(
                    permissionRepository.findAllById(request.getPermissionIds())
            );
            role.setPermissions(permissions);
        }

        Role updatedRole = roleRepository.save(role);
        return roleMapper.toDTO(updatedRole);
    }

    public void deleteRole(Long id) {
        if (!roleRepository.existsById(id)) {
            throw new ResourceNotFoundException("角色不存在");
        }
        roleRepository.deleteById(id);
    }

    public long getRoleCount() {
        return roleRepository.count();
    }
}
