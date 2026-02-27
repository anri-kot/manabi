package com.anrikot.manabi.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.anrikot.manabi.domain.User;
import com.anrikot.manabi.dto.AuthDTO;
import com.anrikot.manabi.dto.LoginResponseDTO;
import com.anrikot.manabi.dto.RegisterDTO;
import com.anrikot.manabi.infra.security.TokenService;
import com.anrikot.manabi.services.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("api/auth")
@Tag(name = "Authentication")
public class AuthController {
    private final UserService service;
    private final AuthenticationManager authManager;
    private final TokenService tokenService;

    public AuthController(UserService service, AuthenticationManager authManager, TokenService tokenService) {
        this.service = service;
        this.authManager = authManager;
        this.tokenService = tokenService;
    }

    @Operation(summary = "Login", security = @SecurityRequirement(name = ""))
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Logged successfully"),
        @ApiResponse(responseCode = "401", description = "Invalid username or password")
    })
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody @Valid AuthDTO dto) {
        var usernamePassword = new UsernamePasswordAuthenticationToken(dto.login(), dto.password());
        var auth = authManager.authenticate(usernamePassword);

        var token = tokenService.generateToken((User) auth.getPrincipal());

        return ResponseEntity.ok(new LoginResponseDTO(token));
    }
    
    @Operation(summary = "Register", security = @SecurityRequirement(name = ""))
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "User registered successfully"),
        @ApiResponse(responseCode = "409", description = "Username or email already exists"),
        @ApiResponse(responseCode = "400", description = "Validation error")
    })
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @Valid RegisterDTO dto) {
        service.save(dto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
    
}
