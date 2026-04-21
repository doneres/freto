package com.freto.usuarioService.service.impl;

import java.util.List;
import java.util.UUID;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.freto.usuarioService.dto.CreateUserDTO;
import com.freto.usuarioService.dto.UserResponseDTO;
import com.freto.usuarioService.exception.EmailAlreadyExistsException;
import com.freto.usuarioService.model.User;
import com.freto.usuarioService.repository.UserRepository;
import com.freto.usuarioService.service.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserResponseDTO createUser(CreateUserDTO dto) {

        if (userRepository.existsByEmail(dto.email())) {
            throw new EmailAlreadyExistsException("Email já cadastrado!");
        }

        String passCript = passwordEncoder.encode(dto.password());

        User user = new User();

        user.setName(dto.name());
        user.setEmail(dto.email());
        user.setPassword(passCript);
        user.setPhoneNumber(dto.phoneNumber());
        user.setRole(dto.role());

        User savedUser = userRepository.save(user);

        return new UserResponseDTO(savedUser.getId(), savedUser.getName(), savedUser.getEmail(), savedUser.getRole());
    }

    @Override
    public void deleteUser(UUID id) {

    }

    @Override
    public List<UserResponseDTO> getAllUsers() {
        return null;
    }

    @Override
    public UserResponseDTO getUserById(UUID id) {
        return null;
    }

    @Override
    public UserResponseDTO updateUser(UUID id, CreateUserDTO dto) {
        return null;
    }

}
