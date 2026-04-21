package com.freto.usuarioService.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.freto.usuarioService.dto.CreateUserDTO;
import com.freto.usuarioService.dto.UserResponseDTO;
import com.freto.usuarioService.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserResponseDTO> createUser(@RequestBody CreateUserDTO dto) {

        UserResponseDTO userCreated = userService.createUser(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(userCreated);

    }

}
