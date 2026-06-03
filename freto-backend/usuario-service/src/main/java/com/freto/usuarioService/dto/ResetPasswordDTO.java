package com.freto.usuarioService.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record ResetPasswordDTO(
        @NotBlank @Email String email,
        @NotBlank @Pattern(
                regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=!]).{8,}$",
                message = "Senha deve ter no mínimo 8 caracteres, letra maiúscula, minúscula, número e caractere especial"
        ) String newPassword,
        @NotBlank String confirmNewPassword) {
}
