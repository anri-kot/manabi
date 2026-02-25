package com.anrikot.manabi.dto;

import jakarta.validation.constraints.NotNull;

public record CreateSessionDTO(
    @NotNull
    Long focusId
) {
    
}
