package com.zhukm.sync.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponse {

    private Long id;

    private String username;

    private String email;

    private String firstName;

    private String lastName;

    private Boolean enabled;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private Set<String> roles;

    private Set<String> permissions;
}
