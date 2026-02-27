package com.anrikot.manabi.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.anrikot.manabi.dto.AuthDTO;
import com.anrikot.manabi.dto.EmailRequestDTO;
import com.anrikot.manabi.dto.PasswordRequestDTO;
import com.anrikot.manabi.dto.UserDTO;
import com.anrikot.manabi.services.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("api/users/me")
@Tag(name = "User Management")
public class UserController {
    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @Operation(summary = "Get user details")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "User found", content = @Content(schema = @Schema(implementation = UserDTO.class))),
        @ApiResponse(responseCode = "404", description = "User not found")
    })
    @GetMapping
    public ResponseEntity<UserDTO> getUser(@AuthenticationPrincipal UserDetails user) {
        return ResponseEntity.ok(service.findByUsername(user.getUsername()));
    }

    @Operation(summary = "Update user email")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Email updated"),
        @ApiResponse(responseCode = "404", description = "User not found"),
        @ApiResponse(responseCode = "409", description = "Email already in use")
    })
    @PatchMapping("/email")
    public ResponseEntity<Void> updateEmail(
            @RequestBody @Valid EmailRequestDTO req,
            @AuthenticationPrincipal UserDetails user) {
        service.updateEmail(req, user.getUsername());
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Update user password")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Password updated"),
        @ApiResponse(responseCode = "404", description = "User not found"),
        @ApiResponse(responseCode = "400", description = "Invalid password")
    })
    @PatchMapping("/password")
    public ResponseEntity<Void> updatePassword(
            @RequestBody @Valid PasswordRequestDTO req,
            @AuthenticationPrincipal UserDetails user) {
        service.updatePassword(req, user.getUsername());
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Delete user")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "User deleted"),
        @ApiResponse(responseCode = "404", description = "User not found"),
        @ApiResponse(responseCode = "400", description = "Invalid password")
    })
    @DeleteMapping
    public ResponseEntity<Void> deleteUser(@Valid @RequestBody AuthDTO login, @AuthenticationPrincipal UserDetails user) {
        service.deleteByUsername(login, user.getUsername());
        return ResponseEntity.noContent().build();
    }
}
