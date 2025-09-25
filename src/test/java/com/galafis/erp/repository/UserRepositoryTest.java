package com.galafis.erp.repository;

import com.galafis.erp.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Repository layer tests for UserRepository using @DataJpaTest
 * 
 * This test class demonstrates repository layer testing best practices:
 * - Use @DataJpaTest for focused repository tests
 * - TestEntityManager for test data management
 * - @ActiveProfiles for test-specific configuration
 * - Testing custom query methods
 * - Database constraint validations
 * - Transactional behavior verification
 */
@DataJpaTest
@ActiveProfiles("test")
class UserRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    private User testUser;
    private User secondUser;

    @BeforeEach
    void setUp() {
        // Clear any existing data
        entityManager.clear();
        
        // Initialize test data
        testUser = User.builder()
            .username("testuser")
            .email("test@example.com")
            .firstName("Test")
            .lastName("User")
            .password("encodedPassword")
            .role("USER")
            .active(true)
            .build();

        secondUser = User.builder()
            .username("adminuser")
            .email("admin@example.com")
            .firstName("Admin")
            .lastName("User")
            .password("adminPassword")
            .role("ADMIN")
            .active(true)
            .build();
    }

    @Test
    void shouldSaveUser_whenValidUser() {
        // When
        User savedUser = userRepository.save(testUser);
        
        // Then
        assertThat(savedUser).isNotNull();
        assertThat(savedUser.getId()).isNotNull();
        assertThat(savedUser.getUsername()).isEqualTo(testUser.getUsername());
        assertThat(savedUser.getEmail()).isEqualTo(testUser.getEmail());
        
        // Verify it's persisted
        User foundUser = entityManager.find(User.class, savedUser.getId());
        assertThat(foundUser).isNotNull();
        assertThat(foundUser.getUsername()).isEqualTo("testuser");
    }

    @Test
    void shouldFindUserById_whenUserExists() {
        // Given
        User persistedUser = entityManager.persistAndFlush(testUser);
        
        // When
        Optional<User> foundUser = userRepository.findById(persistedUser.getId());
        
        // Then
        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getUsername()).isEqualTo("testuser");
        assertThat(foundUser.get().getEmail()).isEqualTo("test@example.com");
    }

    @Test
    void shouldReturnEmpty_whenFindByNonExistentId() {
        // When
        Optional<User> foundUser = userRepository.findById(999L);
        
        // Then
        assertThat(foundUser).isEmpty();
    }

    @Test
    void shouldFindUserByUsername_whenUsernameExists() {
        // Given
        entityManager.persistAndFlush(testUser);
        
        // When
        Optional<User> foundUser = userRepository.findByUsername("testuser");
        
        // Then
        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getEmail()).isEqualTo("test@example.com");
        assertThat(foundUser.get().getFirstName()).isEqualTo("Test");
    }

    @Test
    void shouldReturnEmpty_whenFindByNonExistentUsername() {
        // When
        Optional<User> foundUser = userRepository.findByUsername("nonexistent");
        
        // Then
        assertThat(foundUser).isEmpty();
    }

    @Test
    void shouldFindUserByEmail_whenEmailExists() {
        // Given
        entityManager.persistAndFlush(testUser);
        
        // When
        Optional<User> foundUser = userRepository.findByEmail("test@example.com");
        
        // Then
        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getUsername()).isEqualTo("testuser");
        assertThat(foundUser.get().getRole()).isEqualTo("USER");
    }

    @Test
    void shouldReturnEmpty_whenFindByNonExistentEmail() {
        // When
        Optional<User> foundUser = userRepository.findByEmail("nonexistent@example.com");
        
        // Then
        assertThat(foundUser).isEmpty();
    }

    @Test
    void shouldReturnTrue_whenUsernameExists() {
        // Given
        entityManager.persistAndFlush(testUser);
        
        // When
        boolean exists = userRepository.existsByUsername("testuser");
        
        // Then
        assertTrue(exists);
    }

    @Test
    void shouldReturnFalse_whenUsernameDoesNotExist() {
        // When
        boolean exists = userRepository.existsByUsername("nonexistent");
        
        // Then
        assertFalse(exists);
    }

    @Test
    void shouldReturnTrue_whenEmailExists() {
        // Given
        entityManager.persistAndFlush(testUser);
        
        // When
        boolean exists = userRepository.existsByEmail("test@example.com");
        
        // Then
        assertTrue(exists);
    }

    @Test
    void shouldReturnFalse_whenEmailDoesNotExist() {
        // When
        boolean exists = userRepository.existsByEmail("nonexistent@example.com");
        
        // Then
        assertFalse(exists);
    }

    @Test
    void shouldFindAllUsers_whenUsersExist() {
        // Given
        entityManager.persistAndFlush(testUser);
        entityManager.persistAndFlush(secondUser);
        
        // When
        List<User> allUsers = userRepository.findAll();
        
        // Then
        assertThat(allUsers).hasSize(2);
        assertThat(allUsers).extracting(User::getUsername)
            .containsExactlyInAnyOrder("testuser", "adminuser");
    }

    @Test
    void shouldFindActiveUsers_whenActiveUsersExist() {
        // Given
        User inactiveUser = testUser.toBuilder()
            .username("inactiveuser")
            .email("inactive@example.com")
            .active(false)
            .build();
            
        entityManager.persistAndFlush(testUser);
        entityManager.persistAndFlush(secondUser);
        entityManager.persistAndFlush(inactiveUser);
        
        // When
        List<User> activeUsers = userRepository.findByActiveTrue();
        
        // Then
        assertThat(activeUsers).hasSize(2);
        assertThat(activeUsers).extracting(User::getUsername)
            .containsExactlyInAnyOrder("testuser", "adminuser");
        assertThat(activeUsers).allMatch(User::isActive);
    }

    @Test
    void shouldFindUsersByRole_whenRoleExists() {
        // Given
        entityManager.persistAndFlush(testUser);
        entityManager.persistAndFlush(secondUser);
        
        // When
        List<User> userRoleUsers = userRepository.findByRole("USER");
        List<User> adminRoleUsers = userRepository.findByRole("ADMIN");
        
        // Then
        assertThat(userRoleUsers).hasSize(1);
        assertThat(userRoleUsers.get(0).getUsername()).isEqualTo("testuser");
        
        assertThat(adminRoleUsers).hasSize(1);
        assertThat(adminRoleUsers.get(0).getUsername()).isEqualTo("adminuser");
    }

    @Test
    void shouldDeleteUser_whenUserExists() {
        // Given
        User persistedUser = entityManager.persistAndFlush(testUser);
        Long userId = persistedUser.getId();
        
        // Verify user exists
        assertThat(userRepository.findById(userId)).isPresent();
        
        // When
        userRepository.deleteById(userId);
        entityManager.flush();
        
        // Then
        assertThat(userRepository.findById(userId)).isEmpty();
    }

    @Test
    void shouldUpdateUser_whenUserExists() {
        // Given
        User persistedUser = entityManager.persistAndFlush(testUser);
        
        // When
        persistedUser.setFirstName("Updated");
        persistedUser.setLastName("Name");
        persistedUser.setEmail("updated@example.com");
        
        User updatedUser = userRepository.save(persistedUser);
        entityManager.flush();
        
        // Then
        assertThat(updatedUser.getFirstName()).isEqualTo("Updated");
        assertThat(updatedUser.getLastName()).isEqualTo("Name");
        assertThat(updatedUser.getEmail()).isEqualTo("updated@example.com");
        
        // Verify persistence
        User foundUser = entityManager.find(User.class, persistedUser.getId());
        assertThat(foundUser.getFirstName()).isEqualTo("Updated");
        assertThat(foundUser.getEmail()).isEqualTo("updated@example.com");
    }

    @Test
    void shouldCountUsers_whenUsersExist() {
        // Given
        entityManager.persistAndFlush(testUser);
        entityManager.persistAndFlush(secondUser);
        
        // When
        long userCount = userRepository.count();
        
        // Then
        assertThat(userCount).isEqualTo(2);
    }

    @Test
    void shouldFindUsersContainingName_whenNameMatches() {
        // Given
        User johnUser = testUser.toBuilder()
            .username("johnsmith")
            .email("john@example.com")
            .firstName("John")
            .lastName("Smith")
            .build();
            
        entityManager.persistAndFlush(testUser);
        entityManager.persistAndFlush(johnUser);
        
        // When
        List<User> usersWithTest = userRepository.findByFirstNameContainingIgnoreCase("test");
        List<User> usersWithJohn = userRepository.findByFirstNameContainingIgnoreCase("john");
        
        // Then
        assertThat(usersWithTest).hasSize(1);
        assertThat(usersWithTest.get(0).getFirstName()).isEqualTo("Test");
        
        assertThat(usersWithJohn).hasSize(1);
        assertThat(usersWithJohn.get(0).getFirstName()).isEqualTo("John");
    }
}
