package com.freto.usuarioService.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.freto.usuarioService.dto.LoginRequestDTO;
import com.freto.usuarioService.dto.LoginResponseDTO;
import com.freto.usuarioService.exception.PasswordNotMatchException;
import com.freto.usuarioService.exception.UserNotFoundException;
import com.freto.usuarioService.model.User;
import com.freto.usuarioService.repository.UserRepository;
import com.freto.usuarioService.security.JwtService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.PostMapping;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody LoginRequestDTO dto) {
        User user = userRepository.findByEmail(dto.email())
                .orElseThrow(() -> new UserNotFoundException("Email ou Senha incorretos!"));

        if (!passwordEncoder.matches(dto.password(), user.getPassword())) {
            throw new PasswordNotMatchException("Email ou Senha incorretos!");
        }

        String token = jwtService.generateToken(user.getEmail());

        return ResponseEntity.ok(new LoginResponseDTO(token));
    }
}
