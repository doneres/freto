package com.freto.usuarioService.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.freto.usuarioService.dto.CreateUserDTO;
import com.freto.usuarioService.dto.UpdatePasswordDTO;
import com.freto.usuarioService.dto.UpdateUserDTO;
import com.freto.usuarioService.dto.UserResponseDTO;
import com.freto.usuarioService.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserResponseDTO> createUser(@Valid @RequestBody CreateUserDTO dto) {

        UserResponseDTO userCreated = userService.createUser(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(userCreated);

    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable UUID id) {
        UserResponseDTO getUserById = userService.getUserById(id);

        return ResponseEntity.status(HttpStatus.OK).body(getUserById);
    }

    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        List<UserResponseDTO> users = userService.getAllUsers();

        return ResponseEntity.status(HttpStatus.OK).body(users);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<UserResponseDTO> updateUser(@PathVariable UUID id, @Valid @RequestBody UpdateUserDTO dto) {
        UserResponseDTO user = userService.updateUser(id, dto);

        return ResponseEntity.status(HttpStatus.OK).body(user);

    }

    @PatchMapping("/{id}/password")
    public ResponseEntity<Void> updatePassword(@PathVariable UUID id, @Valid @RequestBody UpdatePasswordDTO dto) {

        userService.updatePassword(id, dto);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable UUID id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

}
