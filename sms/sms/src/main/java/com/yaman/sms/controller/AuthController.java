package com.yaman.sms.controller;

import com.yaman.sms.config.JwtTokenProvider;
import com.yaman.sms.dto.AuthRequest;
import com.yaman.sms.dto.AuthResponse;
import com.yaman.sms.dto.RegisterAdminRequest;
import com.yaman.sms.entity.Admin;
import com.yaman.sms.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final AdminService adminService;

    // 🔹 Login (public)
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtTokenProvider.generateToken(authentication.getName());

        return ResponseEntity.ok(new AuthResponse(token));
    }

    // 🔹 Register Admin (secured - later we will protect it)
    @PostMapping("/register")
    public ResponseEntity<Admin> registerAdmin(@RequestBody RegisterAdminRequest request) {
        Admin created = adminService.createAdmin(request);
        return ResponseEntity.ok(created);
    }
}