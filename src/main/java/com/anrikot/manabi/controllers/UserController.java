package com.anrikot.manabi.controllers;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.anrikot.manabi.dto.AuthDTO;
import com.anrikot.manabi.dto.EmailRequestDTO;
import com.anrikot.manabi.dto.PasswordRequestDTO;
import com.anrikot.manabi.dto.RegisterDTO;
import com.anrikot.manabi.dto.UserDTO;
import com.anrikot.manabi.services.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/users/me")
public class UserController {
    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<UserDTO> getUser(@AuthenticationPrincipal UserDetails user) {
        return ResponseEntity.ok(service.findByUsername(user.getUsername()));
    }

    @PostMapping()
    public ResponseEntity<?> save(@Valid @RequestBody RegisterDTO user) {
        service.save(user);
        URI uri = URI.create("/users" + user.username());
        return ResponseEntity.created(uri).body(user);
    }

    @PatchMapping("/email")
    public ResponseEntity<UserDTO> updateEmail(
            @RequestBody @Valid EmailRequestDTO req,
            @AuthenticationPrincipal UserDetails user) {
        service.updateEmail(req, user.getUsername());
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/password")
    public ResponseEntity<Void> updatePassword(
            @RequestBody @Valid PasswordRequestDTO req,
            @AuthenticationPrincipal UserDetails user) {
        service.updatePassword(req, user.getUsername());
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteUser(@Valid @RequestBody AuthDTO login, @AuthenticationPrincipal UserDetails user) {
        service.deleteByUsername(login, user.getUsername());
        return ResponseEntity.noContent().build();
    }
}
