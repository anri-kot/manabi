package com.anrikot.manabi.controllers;

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

import jakarta.validation.Valid;


@RestController
@RequestMapping("auth")
public class AuthController {
    private final UserService service;
    private final AuthenticationManager authManager;
    private final TokenService tokenService;

    public AuthController(UserService service, AuthenticationManager authManager, TokenService tokenService) {
        this.service = service;
        this.authManager = authManager;
        this.tokenService = tokenService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody @Valid AuthDTO dto) {
        var usernamePassword = new UsernamePasswordAuthenticationToken(dto.login(), dto.password());
        var auth = authManager.authenticate(usernamePassword);

        var token = tokenService.generateToken((User) auth.getPrincipal());

        return ResponseEntity.ok(new LoginResponseDTO(token));
    }
    
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @Valid RegisterDTO dto) {
        if (service.existsByEmail(dto.email())) return ResponseEntity.badRequest().build();
        service.save(dto);

        return ResponseEntity.ok().build();
    }
    
}
