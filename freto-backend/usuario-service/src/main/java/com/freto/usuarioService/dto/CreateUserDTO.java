package com.freto.usuarioService.dto;

import com.freto.usuarioService.model.enums.UserRole;

public record CreateUserDTO(
        String name,
        String email,
        String phoneNumber,
        String password,
        UserRole role) {
}