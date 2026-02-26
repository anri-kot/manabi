package com.anrikot.manabi.controllers;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.anrikot.manabi.domain.User;
import com.anrikot.manabi.dto.CreateSessionDTO;
import com.anrikot.manabi.dto.FocusSessionDTO;
import com.anrikot.manabi.dto.UpdateFocusSessionDTO;
import com.anrikot.manabi.services.FocusSessionService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/sessions")
public class FocusSessionController {
    private final FocusSessionService service;

    public FocusSessionController(FocusSessionService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<FocusSessionDTO>> getAll(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok().body(service.findAll(user.getId()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<FocusSessionDTO> get(@AuthenticationPrincipal User user, @PathVariable Long id) {
        return ResponseEntity.ok().body(service.findById(id, user.getId()));
    }

    @PostMapping
    public ResponseEntity<?> save(@RequestBody @Valid CreateSessionDTO dto, @AuthenticationPrincipal User user) {
        FocusSessionDTO saved = service.save(dto.focusId(), user.getId());
        URI uri = URI.create("/session/" + saved.id());
        return ResponseEntity.created(uri).body(saved);
    }

    @PostMapping("/{id}/finish")
    public ResponseEntity<?> finish(@AuthenticationPrincipal User user, @PathVariable Long id) {
        service.finish(id, user.getId());
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody @Valid UpdateFocusSessionDTO dto, @AuthenticationPrincipal User user) {
        service.update(id, dto.end(), user.getId());
        return ResponseEntity.noContent().build();
    }
}
