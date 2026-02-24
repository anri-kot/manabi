package com.anrikot.manabi.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record FocusDTO(
    Long id,
    @Size(min = 3, max = 100)
    @NotNull
    String name,
    Long parentId
) {
}