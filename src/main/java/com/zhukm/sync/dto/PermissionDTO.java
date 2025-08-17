package com.zhukm.sync.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PermissionDTO {
    private Long id;
    private String name;
    private String code;
    private String resource;
    private String action;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
