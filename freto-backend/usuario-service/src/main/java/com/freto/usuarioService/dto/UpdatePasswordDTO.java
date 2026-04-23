package com.freto.usuarioService.dto;

import jakarta.validation.constraints.Pattern;

public record UpdatePasswordDTO(
        String currentPassword,
        @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=!]).{8,}$", message = "Senha deve ter no mínimo 8 caracteres, letra maiúscula, minúscula, número e caractere especial") String newPassword,
        String confirmNewPassword) {

}
