package com.zhukm.sync.service;

import com.zhukm.sync.dto.LoginRequest;
import com.zhukm.sync.dto.LoginResponse;
import com.zhukm.sync.dto.UserDTO;
import com.zhukm.sync.entity.User;
import com.zhukm.sync.mapper.UserMapper;
import com.zhukm.sync.repository.UserRepository;
import com.zhukm.sync.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public LoginResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = tokenProvider.generateToken(authentication);

        // 更新最后登录时间
        User user = userRepository.findByUsernameWithRolesAndPermissions(request.getUsername())
                .orElseThrow(() -> new RuntimeException("用户不存在"));
        user.setLastLogin(LocalDateTime.now());
        userRepository.save(user);

        UserDTO userDTO = userMapper.toDTO(user);

        return new LoginResponse(jwt, userDTO, 86400L); // 24小时
    }

    public UserDTO getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        User user = userRepository.findByUsernameWithRolesAndPermissions(username)
                .orElseThrow(() -> new RuntimeException("用户不存在"));

        return userMapper.toDTO(user);
    }
}
