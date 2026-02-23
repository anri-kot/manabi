package com.anrikot.manabi.dto;

import com.anrikot.manabi.domain.UserRole;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record RegisterDTO (
    @Size(min = 6, max = 20)
    String username,
    @Size(min = 6, max = 16)
    String password,
    @Email
    @NotNull
    String email,
    UserRole role
) {
}