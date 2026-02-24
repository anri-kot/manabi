package com.anrikot.manabi.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record AuthDTO(@Size(min = 6, max = 20) @NotNull String login, @Size(min = 6, max = 20) @NotNull String password) {
}
