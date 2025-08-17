package com.zhukm.sync.service;

import com.zhukm.sync.dto.PermissionDTO;
import com.zhukm.sync.entity.Permission;
import com.zhukm.sync.exception.ResourceNotFoundException;
import com.zhukm.sync.mapper.PermissionMapper;
import com.zhukm.sync.repository.PermissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PermissionService {

    private final PermissionRepository permissionRepository;
    private final PermissionMapper permissionMapper;

    public List<PermissionDTO> getAllPermissions() {
        return permissionRepository.findAll().stream()
                .map(permissionMapper::toDTO)
                .collect(Collectors.toList());
    }

    public PermissionDTO getPermissionById(Long id) {
        Permission permission = permissionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("权限不存在"));
        return permissionMapper.toDTO(permission);
    }

    public long getPermissionCount() {
        return permissionRepository.count();
    }
}