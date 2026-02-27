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
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.anrikot.manabi.domain.User;
import com.anrikot.manabi.dto.CreateSessionDTO;
import com.anrikot.manabi.dto.FocusSessionDTO;
import com.anrikot.manabi.dto.UpdateFocusSessionDTO;
import com.anrikot.manabi.services.FocusSessionService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("api/sessions")
@Tag(name = "Focus Sessions")
public class FocusSessionController {
    private final FocusSessionService service;

    public FocusSessionController(FocusSessionService service) {
        this.service = service;
    }

    @GetMapping
    @Operation(summary = "Get all sessions")
    @ApiResponse(responseCode = "200", description = "List of sessions", content = @Content(
        array = @ArraySchema(schema = @Schema(implementation = FocusSessionDTO.class))
    ))
    public ResponseEntity<List<FocusSessionDTO>> getAll(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok().body(service.findAll(user.getId()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get session by ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Session found", content = @Content(
            schema = @Schema(implementation = FocusSessionDTO.class)
        )),
        @ApiResponse(responseCode = "404", description = "Session or Focus not found")
    })
    public ResponseEntity<FocusSessionDTO> get(@AuthenticationPrincipal User user, @PathVariable Long id) {
        return ResponseEntity.ok().body(service.findById(id, user.getId()));
    }

    @PostMapping
    @Operation(summary = "Start a new session")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201",
            description = "Session created and started",
            headers = {
                @Header(
                    name = "Location",
                    description = "URI of the created session",
                    schema = @Schema(type = "string", example = "/api/session/42")
                )
            },
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = FocusSessionDTO.class)
            )
        ),
        @ApiResponse(responseCode = "404", description = "Focus not found"),
        @ApiResponse(responseCode = "409", description = "Can only have a single session active")
    })
    public ResponseEntity<FocusSessionDTO> save(@RequestBody @Valid CreateSessionDTO dto, @AuthenticationPrincipal User user) {
        FocusSessionDTO saved = service.save(dto.focusId(), user.getId());
        URI uri = ServletUriComponentsBuilder
            .fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(saved.id())
            .toUri();
        return ResponseEntity.created(uri).body(saved);
    }

    @PostMapping("/{id}/finish")
    @Operation(summary = "End session", description = "Sets the end time of the session")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Session ended"),
        @ApiResponse(responseCode = "404", description = "Session or Focus not found")
    })
    public ResponseEntity<?> finish(@AuthenticationPrincipal User user, @PathVariable Long id) {
        service.finish(id, user.getId());
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Update session")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Session updated successfully"),
        @ApiResponse(responseCode = "404", description = "Session or Focus not found"),
        @ApiResponse(responseCode = "400", description = "Invalid session update")
    })
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody @Valid UpdateFocusSessionDTO dto, @AuthenticationPrincipal User user) {
        service.update(id, dto.end(), user.getId());
        return ResponseEntity.noContent().build();
    }
}
