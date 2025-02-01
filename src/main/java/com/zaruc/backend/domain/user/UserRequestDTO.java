package com.zaruc.backend.domain.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UserRequestDTO(
        @NotBlank
        String login,

        @NotBlank
        String name
) {
}