package com.freto.usuarioService.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginRequestDTO(
        @NotBlank(message = "Email necessário") @Email String email,
        @NotBlank(message = "Senha necessária") String password) {

}
