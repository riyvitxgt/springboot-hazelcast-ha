package com.zhukm.sync.service;

import com.zhukm.sync.dto.ApiResponse;
import com.zhukm.sync.dto.UserResponse;
import com.zhukm.sync.entity.Role;
import com.zhukm.sync.entity.User;
import com.zhukm.sync.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    @Cacheable(value = "userProfiles", key = "#id")
    public ApiResponse<UserResponse> getUserById(Long id) {
        try {
            User user = userRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("User not found with id: " + id));

            UserResponse userResponse = convertToUserResponse(user);
            return ApiResponse.success(userResponse);
        } catch (Exception e) {
            log.error("Error getting user by id: {}", id, e);
            return ApiResponse.error("User not found");
        }
    }

    @Transactional(readOnly = true)
    public ApiResponse<List<UserResponse>> getAllUsers() {
        try {
            List<User> users = userRepository.findAll();
            List<UserResponse> userResponses = users.stream()
                    .map(this::convertToUserResponse)
                    .collect(Collectors.toList());

            return ApiResponse.success(userResponses);
        } catch (Exception e) {
            log.error("Error getting all users", e);
            return ApiResponse.error("Failed to retrieve users");
        }
    }

    private UserResponse convertToUserResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .enabled(user.getEnabled())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .roles(user.getRoles().stream()
                        .map(Role::getName)
                        .collect(Collectors.toSet()))
                .permissions(user.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.toSet()))
                .build();
    }
}
