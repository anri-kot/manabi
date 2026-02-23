package com.anrikot.manabi.controllers;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.anrikot.manabi.dto.FocusDTO;
import com.anrikot.manabi.services.FocusService;

@RestController
@RequestMapping("/focus")
public class FocusController {
    private final FocusService service;

    public FocusController(FocusService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<FocusDTO>> getAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @PostMapping
    public ResponseEntity<FocusDTO> save(@RequestBody FocusDTO dto, @AuthenticationPrincipal UserDetails user) {
        FocusDTO saved = service.save(dto, user.getUsername());
        URI uri = URI.create("/focus/" + saved.id());
        return ResponseEntity.created(uri).body(saved);
    }
}
