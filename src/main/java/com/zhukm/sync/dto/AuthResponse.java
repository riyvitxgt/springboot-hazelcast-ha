package com.zhukm.sync.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@Builder
public class AuthResponse {

    private String token;

    private String type;

    private Long id;

    private String username;

    private String email;

    private Set<String> roles;

    private Set<String> permissions;
}