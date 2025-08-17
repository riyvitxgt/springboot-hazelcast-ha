package com.zhukm.sync.mapper;

import com.zhukm.sync.dto.RoleDTO;
import com.zhukm.sync.entity.Role;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = {PermissionMapper.class})
public interface RoleMapper {

    RoleDTO toDTO(Role role);

    List<RoleDTO> toDTOList(List<Role> roles);

    Role toEntity(RoleDTO roleDTO);
}
