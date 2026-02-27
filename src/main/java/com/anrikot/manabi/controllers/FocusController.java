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
@RequestMapping("api/focus")
@Tag(name = "Focus", description = "Endpoints for user Focus nodes")
public class FocusController {
    private final FocusService service;

    public FocusController(FocusService service) {
        this.service = service;
    }

    @GetMapping
    @Operation(summary = "Get all nodes")
    @ApiResponse(responseCode = "200", description = "List of nodes", content = @Content(
        array = @ArraySchema(schema = @Schema(implementation = FocusDTO.class))))
    // TODO: implement search and pagination
    public ResponseEntity<List<FocusDTO>> getAll(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(service.findAllByUserId(user.getId()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get node by id")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Node found", content = @Content(
            schema = @Schema(implementation = FocusDTO.class)
        )),
        @ApiResponse(responseCode = "404", description = "Node not found")
    })
    public ResponseEntity<FocusDTO> get(@PathVariable Long id, @AuthenticationPrincipal User user) {
        return ResponseEntity.ok().body(service.findByIdAndUsername(id, user.getId()));
    }
    

    @PostMapping
    @Operation(summary = "Create new Focus")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "204",
            description = "Focus created successfully",
            headers = @Header(
                name = "Location",
                description = "URI of the created Focus",
                schema = @Schema(type = "string", example = "/api/session/100")
            ),
            content = @Content(schema = @Schema(implementation = FocusDTO.class))
        ),
        @ApiResponse(responseCode = "400", description = "Invalid Focus")
    })
    public ResponseEntity<FocusDTO> save(@RequestBody @Valid FocusDTO dto, @AuthenticationPrincipal User user) {
        FocusDTO saved = service.save(dto, user.getId());
        URI uri = URI.create("/focus/me/" + saved.id());
        return ResponseEntity.created(uri).body(saved);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a Focus")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Focus updated successfully"),
        @ApiResponse(responseCode = "404", description = "Focus not found"),
        @ApiResponse(responseCode = "400", description = "Invalid Focus update"),
    })
    public ResponseEntity<?> update(@RequestBody @Valid FocusDTO dto,
        @AuthenticationPrincipal User user, @PathVariable Long id) {
        service.update(id, dto, user.getId());
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a Focus")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Focus deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Focus not found"),
    })
    public ResponseEntity<?> delete(@PathVariable Long id, @AuthenticationPrincipal User user) {
        service.delete(id, user.getId());
        return ResponseEntity.noContent().build();
    }
}
