package com.anrikot.manabi.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public record EmailRequestDTO(@Email @NotNull String email) {}
