package com.anrikot.manabi.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record PasswordRequestDTO(
    @Size(min = 6, max = 20)
    @NotNull
    String oldPassword,
    @Size(min = 6, max = 16)
    @NotNull
    String newPassword
) {}
