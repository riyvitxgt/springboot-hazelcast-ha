package com.zhukm.sync.config;

import com.zhukm.sync.entity.Permission;
import com.zhukm.sync.entity.Role;
import com.zhukm.sync.entity.User;
import com.zhukm.sync.repository.PermissionRepository;
import com.zhukm.sync.repository.RoleRepository;
import com.zhukm.sync.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (userRepository.count() == 0) {
            initData();
        }
    }

    private void initData() {
        log.info("Initializing data...");

        // 创建权限
        List<Permission> permissions = Arrays.asList(
                createPermission("仪表盘查看", "dashboard:read", "dashboard", "read", "查看仪表盘"),
                createPermission("用户查看", "user:read", "user", "read", "查看用户列表"),
                createPermission("用户创建", "user:create", "user", "create", "创建用户"),
                createPermission("用户更新", "user:update", "user", "update", "更新用户"),
                createPermission("用户删除", "user:delete", "user", "delete", "删除用户"),
                createPermission("角色查看", "role:read", "role", "read", "查看角色列表"),
                createPermission("角色创建", "role:create", "role", "create", "创建角色"),
                createPermission("角色更新", "role:update", "role", "update", "更新角色"),
                createPermission("角色删除", "role:delete", "role", "delete", "删除角色"),
                createPermission("权限查看", "permission:read", "permission", "read", "查看权限列表"),
                createPermission("系统管理", "system:manage", "system", "manage", "系统管理"),
                createPermission("日志查看", "system:log:read", "system", "log:read", "查看系统日志")
        );
        permissionRepository.saveAll(permissions);

        // 创建角色
        Role adminRole = createRole("超级管理员", "SUPER_ADMIN", "系统超级管理员", permissions);
        Role managerRole = createRole("管理员", "ADMIN", "系统管理员",
                permissions.subList(0, 10)); // 前10个权限
        Role userRole = createRole("普通用户", "USER", "普通用户",
                Collections.singletonList(permissions.get(0))); // 只有仪表盘查看权限

        roleRepository.saveAll(Arrays.asList(adminRole, managerRole, userRole));

        // 创建用户
        User admin = createUser("admin", "admin@example.com", "123456",
                new HashSet<>(List.of(adminRole)));
        User manager = createUser("manager", "manager@example.com", "123456",
                new HashSet<>(List.of(managerRole)));
        User user = createUser("user", "user@example.com", "123456",
                new HashSet<>(List.of(userRole)));

        userRepository.saveAll(Arrays.asList(admin, manager, user));

        log.info("Data initialization completed");
    }

    private Permission createPermission(String name, String code, String resource, String action, String description) {
        Permission permission = new Permission();
        permission.setName(name);
        permission.setCode(code);
        permission.setResource(resource);
        permission.setAction(action);
        permission.setDescription(description);
        return permission;
    }

    private Role createRole(String name, String code, String description, List<Permission> permissions) {
        Role role = new Role();
        role.setName(name);
        role.setCode(code);
        role.setDescription(description);
        role.setEnabled(true);
        role.setPermissions(new HashSet<>(permissions));
        return role;
    }

    private User createUser(String username, String email, String password, Set<Role> roles) {
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setEnabled(true);
        user.setRoles(roles);
        return user;
    }
}
