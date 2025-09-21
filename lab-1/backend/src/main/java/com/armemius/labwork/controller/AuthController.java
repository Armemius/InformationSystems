package com.armemius.labwork.controller;

import com.armemius.labwork.data.dto.CredentialsDto;
import com.armemius.labwork.data.dto.ErrorResponseDto;
import com.armemius.labwork.data.dto.JwtResponseDto;
import com.armemius.labwork.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Auth API", description = "API for managing user authentication")
@CrossOrigin("*")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    @Operation(
            summary = "Register new user",
            description = "Tries to register new user with given credentials"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Registration success",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = JwtResponseDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Username taken",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDto.class)
                    )
            )
    })
    public ResponseEntity<JwtResponseDto> register(@Valid @RequestBody CredentialsDto credentials) {
        return ResponseEntity.ok(authService.registerUser(credentials));
    }

    @PostMapping("/login")
    @Operation(
            summary = "Authenticate user",
            description = "Tries to authenticate existing user with given credentials"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Authentication success",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = JwtResponseDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "User with given credentials does not exist",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDto.class)
                    )
            )
    })
    public ResponseEntity<JwtResponseDto> login(@Valid @RequestBody CredentialsDto credentials) {
        return ResponseEntity.ok(authService.loginUser(credentials));
    }
}
