package com.freto.usuarioService.service;

import java.util.List;
import java.util.UUID;

import com.freto.usuarioService.dto.CreateUserDTO;
import com.freto.usuarioService.dto.UpdatePasswordDTO;
import com.freto.usuarioService.dto.UpdateUserDTO;
import com.freto.usuarioService.dto.UserResponseDTO;

public interface UserService {
    UserResponseDTO createUser(CreateUserDTO dto);

    UserResponseDTO getUserById(UUID id);

    List<UserResponseDTO> getAllUsers();

    UserResponseDTO updateUser(UUID id, UpdateUserDTO dto);

    void updatePassword(UUID id, UpdatePasswordDTO dto);

    void deleteUser(UUID id);
}