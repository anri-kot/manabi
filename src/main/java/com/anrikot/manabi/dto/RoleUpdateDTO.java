package com.anrikot.manabi.dto;

import com.anrikot.manabi.domain.UserRole;

import jakarta.validation.constraints.NotNull;

public record RoleUpdateDTO(
    @NotNull
    UserRole role
) {
    
}
