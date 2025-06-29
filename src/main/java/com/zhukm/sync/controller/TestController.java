package com.zhukm.sync.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/test")
@CrossOrigin(origins = "*", maxAge = 3600)
public class TestController {

    @GetMapping("/public")
    public ResponseEntity<String> publicAccess() {
        return ResponseEntity.ok("Public Content - Accessible to everyone");
    }

    @GetMapping("/user")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<String> userAccess() {
        return ResponseEntity.ok("User Content - Accessible to users and admins");
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> adminAccess() {
        return ResponseEntity.ok("Admin Content - Accessible only to admins");
    }
}