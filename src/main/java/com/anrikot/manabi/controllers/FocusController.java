package com.anrikot.manabi.controllers;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.anrikot.manabi.domain.User;
import com.anrikot.manabi.dto.FocusDTO;
import com.anrikot.manabi.services.FocusService;

import jakarta.validation.Valid;


@RestController
@RequestMapping("/focus/me")
public class FocusController {
    private final FocusService service;

    public FocusController(FocusService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<FocusDTO>> getAll(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(service.findAllByUserId(user.getId()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<FocusDTO> get(@PathVariable Long id, @AuthenticationPrincipal User user) {
        return ResponseEntity.ok().body(service.findByIdAndUsername(id, user.getId()));
    }
    

    @PostMapping
    public ResponseEntity<?> save(@RequestBody @Valid FocusDTO dto, @AuthenticationPrincipal User user) {
        FocusDTO saved = service.save(dto, user.getId());
        URI uri = URI.create("/focus/me/" + saved.id());
        return ResponseEntity.created(uri).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@RequestBody @Valid FocusDTO dto,
        @AuthenticationPrincipal User user, @PathVariable Long id) {
        service.update(id, dto, user.getId());
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id, @AuthenticationPrincipal User user) {
        service.delete(id, user.getId());
        return ResponseEntity.noContent().build();
    }
}
