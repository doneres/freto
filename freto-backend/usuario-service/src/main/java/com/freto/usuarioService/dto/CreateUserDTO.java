package com.freto.usuarioService.dto;

import com.freto.usuarioService.model.enums.UserRole;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record CreateUserDTO(
        @NotBlank String name,
        @NotBlank @Email String email,

        @Size(min = 11, max = 13) String phoneNumber,

        @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=!]).{8,}$", message = "Senha deve ter no mínimo 8 caracteres, letra maiúscula, minúscula, número e caractere especial") String password,
        String confirmPassword,
        UserRole role) {
}