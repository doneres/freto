package com.freto.usuarioService.dto;

import java.util.UUID;
import com.freto.usuarioService.model.enums.UserRole;

public record LoginResponseDTO(
        String token,
        UUID id,
        String name,
        UserRole role) {
}
