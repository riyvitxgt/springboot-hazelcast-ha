package com.zhukm.sync.mapper;

import com.zhukm.sync.dto.PermissionDTO;
import com.zhukm.sync.entity.Permission;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PermissionMapper {

    PermissionDTO toDTO(Permission permission);

    List<PermissionDTO> toDTOList(List<Permission> permissions);

    Permission toEntity(PermissionDTO permissionDTO);
}