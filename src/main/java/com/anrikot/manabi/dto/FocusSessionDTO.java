package com.anrikot.manabi.dto;

import java.time.Instant;

import jakarta.validation.constraints.NotNull;

public record FocusSessionDTO(
    Long id,
    @NotNull
    Long focusId,
    Instant start,
    Instant end
) {
}
