package com.zhukm.sync.service;

import com.zhukm.sync.dto.*;
import com.zhukm.sync.entity.Role;
import com.zhukm.sync.entity.User;
import com.zhukm.sync.repository.RoleRepository;
import com.zhukm.sync.repository.UserRepository;
import com.zhukm.sync.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Transactional
    public ApiResponse<LoginResponse> login(LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = jwtUtil.generateJwtToken(authentication);
            LoginResponse loginResponse = LoginResponse.builder().token(jwt).build();
            return ApiResponse.success("Login successful", loginResponse);
        } catch (Exception e) {
            log.error("Login failed for user: {}", loginRequest.getUsername(), e);
            return ApiResponse.error("Invalid credentials");
        }
    }

    @Transactional
    public ApiResponse<UserResponse> getSession() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return ApiResponse.error("未登录");
        }
        User user = (User) authentication.getPrincipal();
        UserResponse userResponse = UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .enabled(user.isEnabled())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .roles(user.getRoles().stream().map(Role::getName).collect(Collectors.toSet())).build();
        return ApiResponse.success(userResponse);
    }

    @Transactional
    @CacheEvict(value = "users", allEntries = true)
    public ApiResponse<String> register(RegisterRequest registerRequest) {
        try {
            if (userRepository.existsByUsername(registerRequest.getUsername())) {
                return ApiResponse.error("Username is already taken!");
            }

            if (userRepository.existsByEmail(registerRequest.getEmail())) {
                return ApiResponse.error("Email is already in use!");
            }

            User user = User.builder()
                    .username(registerRequest.getUsername())
                    .email(registerRequest.getEmail())
                    .password(passwordEncoder.encode(registerRequest.getPassword()))
                    .firstName(registerRequest.getFirstName())
                    .lastName(registerRequest.getLastName())
                    .build();

            // Assign default USER role
            Role userRole = roleRepository.findByName("ROLE_USER")
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            Set<Role> roles = new HashSet<>();
            roles.add(userRole);
            user.setRoles(roles);

            userRepository.save(user);

            return ApiResponse.success("User registered successfully!");
        } catch (Exception e) {
            log.error("Registration failed for user: {}", registerRequest.getUsername(), e);
            return ApiResponse.error("Registration failed: " + e.getMessage());
        }
    }
}

