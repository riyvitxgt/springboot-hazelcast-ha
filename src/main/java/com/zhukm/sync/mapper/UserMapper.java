package com.zhukm.sync.mapper;

import com.zhukm.sync.dto.PermissionDTO;
import com.zhukm.sync.dto.UserDTO;
import com.zhukm.sync.entity.Permission;
import com.zhukm.sync.entity.Role;
import com.zhukm.sync.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", uses = {RoleMapper.class})
public interface UserMapper {

    @Mapping(source = "roles", target = "permissions", qualifiedByName = "extractPermissions")
    UserDTO toDTO(User user);

    List<UserDTO> toDTOList(List<User> users);

    @Named("extractPermissions")
    default List<PermissionDTO> extractPermissions(Set<Role> roles) {
        return roles.stream()
                .flatMap(role -> role.getPermissions().stream())
                .distinct()
                .map(this::permissionToDTO)
                .collect(Collectors.toList());
    }

    default PermissionDTO permissionToDTO(Permission permission) {
        if (permission == null) {
            return null;
        }

        PermissionDTO dto = new PermissionDTO();
        dto.setId(permission.getId());
        dto.setName(permission.getName());
        dto.setCode(permission.getCode());
        dto.setResource(permission.getResource());
        dto.setAction(permission.getAction());
        dto.setDescription(permission.getDescription());
        dto.setCreatedAt(permission.getCreatedAt());
        dto.setUpdatedAt(permission.getUpdatedAt());

        return dto;
    }
}
