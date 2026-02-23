package com.anrikot.manabi.controllers;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.anrikot.manabi.dto.RegisterDTO;
import com.anrikot.manabi.services.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<RegisterDTO>> getAll() {
        return ResponseEntity.ok(service.findAll());
    }
    
    @PostMapping()
    public ResponseEntity<?> save(@Valid @RequestBody RegisterDTO user) {
        service.save(user);
        URI uri = URI.create("/users" + user.username());
        return ResponseEntity.created(uri).body(user);
    }
    
}
