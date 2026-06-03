package com.freto.usuarioService.service.impl;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.freto.usuarioService.dto.CreateUserDTO;
import com.freto.usuarioService.dto.UpdatePasswordDTO;
import com.freto.usuarioService.dto.UpdateUserDTO;
import com.freto.usuarioService.dto.UserResponseDTO;
import com.freto.usuarioService.event.UserCreatedEvent;
import com.freto.usuarioService.event.UserEventPublisher;
import com.freto.usuarioService.exception.EmailAlreadyExistsException;
import com.freto.usuarioService.exception.PasswordNotMatchException;
import com.freto.usuarioService.exception.UserNotFoundException;
import com.freto.usuarioService.model.User;
import com.freto.usuarioService.repository.UserRepository;
import com.freto.usuarioService.service.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserEventPublisher userEventPublisher;

    private UserResponseDTO toResponseDTO(User user) {
        return new UserResponseDTO(user.getId(), user.getName(), user.getEmail(), user.getRole());
    }

    @Override
    public UserResponseDTO createUser(CreateUserDTO dto) {

        if (userRepository.existsByEmail(dto.email())) {
            throw new EmailAlreadyExistsException("Email já cadastrado!");
        }

        if (!dto.password().equals(dto.confirmPassword())) {
            throw new PasswordNotMatchException("Senhas não coincidem!");
        }

        String passCript = passwordEncoder.encode(dto.password());

        User user = new User();

        user.setName(dto.name());
        user.setEmail(dto.email());
        user.setPassword(passCript);
        user.setPhoneNumber(dto.phoneNumber());
        user.setRole(dto.role());

        User savedUser = userRepository.save(user);

        userEventPublisher.publishUserCreated(
                new UserCreatedEvent(savedUser.getName(), savedUser.getEmail()));

        return toResponseDTO(savedUser);
    }

    @Override
    public void deleteUser(UUID id) {
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("Usuário não encontrado"));

        user.setActive(false);
        userRepository.save(user);
    }

    @Override
    public List<UserResponseDTO> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public UserResponseDTO getUserById(UUID id) {
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("Usuário não encontrado"));

        return toResponseDTO(user);
    }

    @Override
    public void updatePassword(UUID id, UpdatePasswordDTO dto) {
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("Usuário não encontrado"));

        if (!passwordEncoder.matches(dto.currentPassword(), user.getPassword())) {
            throw new PasswordNotMatchException("Senha atual incorreta!");

        }

        if (!dto.newPassword().equals(dto.confirmNewPassword())) {
            throw new PasswordNotMatchException("A nova senha e a confirmação não coincidem!");
        }

        String passCript = passwordEncoder.encode(dto.newPassword());

        user.setPassword(passCript);

        userRepository.save(user);

    }

    @Override
    public UserResponseDTO updateUser(UUID id, UpdateUserDTO dto) {
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("Usuário não encontrado"));

        user.setName(dto.name());
        user.setPhoneNumber(dto.phoneNumber());

        userRepository.save(user);

        return toResponseDTO(user);
    }

}
