package com.zhukm.sync.controller;

import com.zhukm.sync.dto.ApiResponse;
import com.zhukm.sync.dto.UserCreateRequest;
import com.zhukm.sync.dto.UserDTO;
import com.zhukm.sync.dto.UserUpdateRequest;
import com.zhukm.sync.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    @PreAuthorize("hasAuthority('user:read')")
    public ApiResponse<List<UserDTO>> getAllUsers() {
        List<UserDTO> users = userService.getAllUsers();
        return ApiResponse.success(users);
    }

    @GetMapping("/page")
    @PreAuthorize("hasAuthority('user:read')")
    public ApiResponse<Page<UserDTO>> getUsersPage(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sort,
            @RequestParam(defaultValue = "desc") String direction) {

        Sort.Direction sortDirection = Sort.Direction.fromString(direction);
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sort));

        Page<UserDTO> users = userService.getUsersPage(pageable);
        return ApiResponse.success(users);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('user:read')")
    public ApiResponse<UserDTO> getUserById(@PathVariable Long id) {
        UserDTO user = userService.getUserById(id);
        return ApiResponse.success(user);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('user:create')")
    public ApiResponse<UserDTO> createUser(@Valid @RequestBody UserCreateRequest request) {
        UserDTO user = userService.createUser(request);
        return ApiResponse.success("用户创建成功", user);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('user:update')")
    public ApiResponse<UserDTO> updateUser(@PathVariable Long id, @Valid @RequestBody UserUpdateRequest request) {
        UserDTO user = userService.updateUser(id, request);
        return ApiResponse.success("用户更新成功", user);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('user:delete')")
    public ApiResponse<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ApiResponse.success("用户删除成功", null);
    }
}

