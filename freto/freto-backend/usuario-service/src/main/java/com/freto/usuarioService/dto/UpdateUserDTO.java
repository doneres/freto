package com.freto.usuarioService.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdateUserDTO(
        @NotBlank String name,
        @Size(min = 11, max = 13) String phoneNumber) {
}
