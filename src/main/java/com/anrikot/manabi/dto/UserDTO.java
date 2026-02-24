package com.anrikot.manabi.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UserDTO(
    Long id,
    @Size(min = 6, max = 20)
    @NotNull
    String username,
    @Email
    @NotNull
    String email
) {}
