package com.freto.usuarioService.dto;

import java.util.UUID;

import com.freto.usuarioService.model.enums.UserRole;

public record UserResponseDTO(
        UUID id,
        String name,
        String email,
        UserRole role) {
}
