package com.anrikot.manabi.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.anrikot.manabi.dto.RoleUpdateDTO;
import com.anrikot.manabi.dto.UserDTO;
import com.anrikot.manabi.services.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("api/admin")
@Tag(name = "Admin", description = "Endpoints restricted to ADMIN role")
public class AdminController {
    private final UserService service;

    public AdminController(UserService service) {
        this.service = service;
    }

    @Operation(summary = "Get user details")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "User found", content = @Content(schema = @Schema(implementation = UserDTO.class))),
        @ApiResponse(responseCode = "404", description = "User not found")
    })
    @GetMapping("/users/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok().body(service.findById(id));
    }

    // TODO: implement search and pagination
    @Operation(summary = "Get a list of users")
    @ApiResponse(responseCode = "200", description = "List of users", content = @Content(
        array = @ArraySchema(schema = @Schema(implementation = UserDTO.class))))
    @GetMapping("/users")
    public ResponseEntity<List<UserDTO>> getUsers() {
        return ResponseEntity.ok().body(service.findAll());
    }

    @Operation(summary = "Update user role")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Role updated successfully"),
        @ApiResponse(responseCode = "404", description = "User not found")
    })
    @PatchMapping("/users/{id}/role")
    public ResponseEntity<?> updateRole(@RequestBody @Valid RoleUpdateDTO roleRequest, @PathVariable Long id) {
        service.changeRole(id, roleRequest.role());
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Delete user")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "User deleted successfully"),
        @ApiResponse(responseCode = "404", description = "User not found")
    })
    @DeleteMapping("/users/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
