package com.galafis.erp.service;

import com.galafis.erp.dto.UserDto;
import com.galafis.erp.entity.User;
import com.galafis.erp.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * Unit tests for UserService
 * 
 * This test class demonstrates best practices for testing service layer:
 * - Use of @ExtendWith(MockitoExtension.class) for Mockito integration
 * - Mocking of dependencies (repository, password encoder)
 * - Testing various scenarios (success, failure, edge cases)
 * - Proper assertions using JUnit 5 and AssertJ
 * - Clear test method naming following shouldDoSomething_whenCondition pattern
 */
@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private User testUser;
    private UserDto testUserDto;

    @BeforeEach
    void setUp() {
        // Initialize test data
        testUser = User.builder()
            .id(1L)
            .username("testuser")
            .email("test@example.com")
            .firstName("Test")
            .lastName("User")
            .password("encodedPassword")
            .role("USER")
            .active(true)
            .build();

        testUserDto = UserDto.builder()
            .username("testuser")
            .email("test@example.com")
            .firstName("Test")
            .lastName("User")
            .password("plainPassword")
            .role("USER")
            .build();
    }

    @Test
    void shouldCreateUser_whenValidUserDto() {
        // Given
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(testUser);
        when(userRepository.existsByUsername(anyString())).thenReturn(false);
        when(userRepository.existsByEmail(anyString())).thenReturn(false);

        // When
        UserDto result = userService.createUser(testUserDto);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getUsername()).isEqualTo(testUserDto.getUsername());
        assertThat(result.getEmail()).isEqualTo(testUserDto.getEmail());
        verify(passwordEncoder).encode(testUserDto.getPassword());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void shouldThrowException_whenUsernameDuplicate() {
        // Given
        when(userRepository.existsByUsername(anyString())).thenReturn(true);

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            userService.createUser(testUserDto);
        });

        verify(userRepository).existsByUsername(testUserDto.getUsername());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void shouldReturnUser_whenFindByIdWithExistingId() {
        // Given
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(testUser));

        // When
        UserDto result = userService.findById(1L);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(testUser.getId());
        assertThat(result.getUsername()).isEqualTo(testUser.getUsername());
        verify(userRepository).findById(1L);
    }

    @Test
    void shouldThrowException_whenFindByIdWithNonExistingId() {
        // Given
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        // When & Then
        assertThrows(RuntimeException.class, () -> {
            userService.findById(999L);
        });

        verify(userRepository).findById(999L);
    }

    @Test
    void shouldReturnAllUsers_whenFindAll() {
        // Given
        User secondUser = User.builder()
            .id(2L)
            .username("testuser2")
            .email("test2@example.com")
            .build();
        
        List<User> users = Arrays.asList(testUser, secondUser);
        when(userRepository.findAll()).thenReturn(users);

        // When
        List<UserDto> result = userService.findAll();

        // Then
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getUsername()).isEqualTo("testuser");
        assertThat(result.get(1).getUsername()).isEqualTo("testuser2");
        verify(userRepository).findAll();
    }

    @Test
    void shouldUpdateUser_whenValidUserDto() {
        // Given
        UserDto updateDto = UserDto.builder()
            .id(1L)
            .username("updateduser")
            .email("updated@example.com")
            .firstName("Updated")
            .lastName("User")
            .build();

        User updatedUser = User.builder()
            .id(1L)
            .username("updateduser")
            .email("updated@example.com")
            .firstName("Updated")
            .lastName("User")
            .build();

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(testUser));
        when(userRepository.save(any(User.class))).thenReturn(updatedUser);

        // When
        UserDto result = userService.updateUser(updateDto);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getUsername()).isEqualTo("updateduser");
        assertThat(result.getEmail()).isEqualTo("updated@example.com");
        verify(userRepository).findById(1L);
        verify(userRepository).save(any(User.class));
    }

    @Test
    void shouldDeleteUser_whenValidId() {
        // Given
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(testUser));
        doNothing().when(userRepository).deleteById(anyLong());

        // When
        userService.deleteUser(1L);

        // Then
        verify(userRepository).findById(1L);
        verify(userRepository).deleteById(1L);
    }

    @Test
    void shouldReturnTrue_whenUserExistsByUsername() {
        // Given
        when(userRepository.existsByUsername(anyString())).thenReturn(true);

        // When
        boolean result = userService.existsByUsername("testuser");

        // Then
        assertTrue(result);
        verify(userRepository).existsByUsername("testuser");
    }

    @Test
    void shouldReturnFalse_whenUserNotExistsByUsername() {
        // Given
        when(userRepository.existsByUsername(anyString())).thenReturn(false);

        // When
        boolean result = userService.existsByUsername("nonexistentuser");

        // Then
        assertFalse(result);
        verify(userRepository).existsByUsername("nonexistentuser");
    }
}
