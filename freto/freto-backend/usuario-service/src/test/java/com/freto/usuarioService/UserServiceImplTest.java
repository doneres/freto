package com.freto.usuarioService;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.freto.usuarioService.dto.CreateUserDTO;
import com.freto.usuarioService.dto.UpdatePasswordDTO;
import com.freto.usuarioService.dto.UpdateUserDTO;
import com.freto.usuarioService.dto.UserResponseDTO;
import com.freto.usuarioService.event.UserEventPublisher;
import com.freto.usuarioService.exception.EmailAlreadyExistsException;
import com.freto.usuarioService.exception.PasswordNotMatchException;
import com.freto.usuarioService.exception.UserNotFoundException;
import com.freto.usuarioService.model.User;
import com.freto.usuarioService.model.enums.UserRole;
import com.freto.usuarioService.repository.UserRepository;
import com.freto.usuarioService.service.impl.UserServiceImpl;

@ExtendWith(MockitoExtension.class)
@DisplayName("UserServiceImpl - Testes Unitários")
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserEventPublisher userEventPublisher;

    @InjectMocks
    private UserServiceImpl userService;

    private UUID userId;
    private User userMock;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();

        userMock = new User();
        userMock.setId(userId);
        userMock.setName("Arthur Vanzim");
        userMock.setEmail("arthur@freto.com");
        userMock.setPassword("$2a$10$hashedpassword");
        userMock.setPhoneNumber("(62) 9 9999-0000");
        userMock.setRole(UserRole.CONTRATANTE);
        userMock.setActive(true);
    }

    // ─── createUser ──────────────────────────────────────────

    @Test
    @DisplayName("Deve criar usuário com sucesso e publicar evento")
    void deveCriarUsuarioComSucesso() {
        CreateUserDTO dto = new CreateUserDTO(
                "Arthur Vanzim", "arthur@freto.com", "(62) 9 9999-0000",
                "senha123", "senha123", UserRole.CONTRATANTE
        );

        when(userRepository.existsByEmail(dto.email())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("$2a$10$hashed");
        when(userRepository.save(any(User.class))).thenReturn(userMock);

        UserResponseDTO response = userService.createUser(dto);

        assertNotNull(response);
        assertEquals("Arthur Vanzim", response.name());
        assertEquals("arthur@freto.com", response.email());
        verify(userEventPublisher, times(1)).publishUserCreated(any());
    }

    @Test
    @DisplayName("Deve lançar exceção se e-mail já cadastrado")
    void deveLancarExcecaoEmailJaCadastrado() {
        CreateUserDTO dto = new CreateUserDTO(
                "Arthur Vanzim", "arthur@freto.com", "(62) 9 9999-0000",
                "senha123", "senha123", UserRole.CONTRATANTE
        );

        when(userRepository.existsByEmail(dto.email())).thenReturn(true);

        assertThrows(EmailAlreadyExistsException.class, () -> userService.createUser(dto));
        verify(userRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve lançar exceção se senhas não coincidem no cadastro")
    void deveLancarExcecaoSenhasNaoConcidem() {
        CreateUserDTO dto = new CreateUserDTO(
                "Arthur Vanzim", "arthur@freto.com", "(62) 9 9999-0000",
                "senha123", "outrasenha", UserRole.CONTRATANTE
        );

        when(userRepository.existsByEmail(dto.email())).thenReturn(false);

        assertThrows(PasswordNotMatchException.class, () -> userService.createUser(dto));
    }

    // ─── getUserById ──────────────────────────────────────────

    @Test
    @DisplayName("Deve buscar usuário por ID com sucesso")
    void deveBuscarUsuarioPorId() {
        when(userRepository.findById(userId)).thenReturn(Optional.of(userMock));

        UserResponseDTO response = userService.getUserById(userId);

        assertNotNull(response);
        assertEquals(userId, response.id());
        assertEquals("Arthur Vanzim", response.name());
    }

    @Test
    @DisplayName("Deve lançar exceção quando usuário não encontrado")
    void deveLancarExcecaoUsuarioNaoEncontrado() {
        when(userRepository.findById(any())).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.getUserById(UUID.randomUUID()));
    }

    // ─── getAllUsers ──────────────────────────────────────────

    @Test
    @DisplayName("Deve listar todos os usuários")
    void deveListarTodosUsuarios() {
        when(userRepository.findAll()).thenReturn(List.of(userMock));

        List<UserResponseDTO> lista = userService.getAllUsers();

        assertFalse(lista.isEmpty());
        assertEquals(1, lista.size());
    }

    // ─── deleteUser ──────────────────────────────────────────

    @Test
    @DisplayName("Deve desativar usuário (soft delete)")
    void deveDesativarUsuario() {
        when(userRepository.findById(userId)).thenReturn(Optional.of(userMock));
        when(userRepository.save(any())).thenReturn(userMock);

        assertDoesNotThrow(() -> userService.deleteUser(userId));
        assertFalse(userMock.isActive());
        verify(userRepository, times(1)).save(userMock);
    }

    @Test
    @DisplayName("Deve lançar exceção ao deletar usuário inexistente")
    void deveLancarExcecaoAoDeletarUsuarioInexistente() {
        when(userRepository.findById(any())).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.deleteUser(UUID.randomUUID()));
    }

    // ─── updateUser ──────────────────────────────────────────

    @Test
    @DisplayName("Deve atualizar dados do usuário com sucesso")
    void deveAtualizarUsuario() {
        UpdateUserDTO dto = new UpdateUserDTO("Arthur Silva", "(62) 8 8888-1111");

        when(userRepository.findById(userId)).thenReturn(Optional.of(userMock));
        when(userRepository.save(any())).thenReturn(userMock);

        UserResponseDTO response = userService.updateUser(userId, dto);

        assertNotNull(response);
        verify(userRepository, times(1)).save(any());
    }

    // ─── updatePassword ──────────────────────────────────────

    @Test
    @DisplayName("Deve atualizar senha com sucesso")
    void deveAtualizarSenha() {
        UpdatePasswordDTO dto = new UpdatePasswordDTO("senha123", "novaSenha456", "novaSenha456");

        when(userRepository.findById(userId)).thenReturn(Optional.of(userMock));
        when(passwordEncoder.matches("senha123", userMock.getPassword())).thenReturn(true);
        when(passwordEncoder.encode("novaSenha456")).thenReturn("$2a$10$newHashed");
        when(userRepository.save(any())).thenReturn(userMock);

        assertDoesNotThrow(() -> userService.updatePassword(userId, dto));
        verify(userRepository, times(1)).save(any());
    }

    @Test
    @DisplayName("Deve lançar exceção se senha atual incorreta")
    void deveLancarExcecaoSenhaAtualIncorreta() {
        UpdatePasswordDTO dto = new UpdatePasswordDTO("errada", "novaSenha456", "novaSenha456");

        when(userRepository.findById(userId)).thenReturn(Optional.of(userMock));
        when(passwordEncoder.matches("errada", userMock.getPassword())).thenReturn(false);

        assertThrows(PasswordNotMatchException.class, () -> userService.updatePassword(userId, dto));
    }

    @Test
    @DisplayName("Deve lançar exceção se nova senha e confirmação não coincidem")
    void deveLancarExcecaoNovasSenhasNaoConcidem() {
        UpdatePasswordDTO dto = new UpdatePasswordDTO("senha123", "novaSenha456", "diferente");

        when(userRepository.findById(userId)).thenReturn(Optional.of(userMock));
        when(passwordEncoder.matches("senha123", userMock.getPassword())).thenReturn(true);

        assertThrows(PasswordNotMatchException.class, () -> userService.updatePassword(userId, dto));
    }
}
