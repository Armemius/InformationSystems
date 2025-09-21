package com.armemius.labwork.controller;

import com.armemius.labwork.data.dto.CredentialsDto;
import com.armemius.labwork.data.dto.JwtResponseDto;
import com.armemius.labwork.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@CrossOrigin("*")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<JwtResponseDto> register(@Valid @RequestBody CredentialsDto credentials) {
        return ResponseEntity.ok(authService.registerUser(credentials));
    }

    @PostMapping("/login")
    public ResponseEntity<JwtResponseDto> login(@Valid @RequestBody CredentialsDto credentials) {
        return ResponseEntity.ok(authService.loginUser(credentials));
    }
}
