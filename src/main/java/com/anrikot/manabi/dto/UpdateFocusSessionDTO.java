package com.anrikot.manabi.dto;

import java.time.Instant;

import jakarta.validation.constraints.NotNull;

public record UpdateFocusSessionDTO(
    @NotNull
    Instant end
) {
}
