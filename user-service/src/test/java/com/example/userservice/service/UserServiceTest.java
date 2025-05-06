package com.example.userservice.service;

import com.example.userservice.model.User;
import com.example.userservice.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    void findAll_ShouldReturnAllUsers() {
        // Arrange
        User user1 = new User(1L, "User1", "user1@example.com");
        User user2 = new User(2L, "User2", "user2@example.com");
        List<User> expectedUsers = Arrays.asList(user1, user2);

        when(userRepository.findAll()).thenReturn(expectedUsers);

        // Act
        List<User> actualUsers = userService.findAll();

        // Assert
        assertEquals(2, actualUsers.size());
        assertEquals("user1@example.com", actualUsers.get(0).getEmail());
        verify(userRepository).findAll();
    }

    @Test
    void findById_WhenUserExists_ShouldReturnUser() {
        // Arrange
        Long userId = 1L;
        User expectedUser = new User(userId, "Test User", "test@example.com");
        when(userRepository.findById(userId)).thenReturn(Optional.of(expectedUser));

        // Act
        Optional<User> actualUser = userService.findById(userId);

        // Assert
        assertTrue(actualUser.isPresent());
        assertEquals("test@example.com", actualUser.get().getEmail());
        verify(userRepository).findById(userId);
    }

    @Test
    void findById_WhenUserNotExists_ShouldReturnEmpty() {
        // Arrange
        Long userId = 999L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Act
        Optional<User> actualUser = userService.findById(userId);

        // Assert
        assertFalse(actualUser.isPresent());
        verify(userRepository).findById(userId);
    }

    @Test
    void save_ShouldReturnSavedUser() {
        // Arrange
        User userToSave = new User(null, "New User", "new@example.com");
        User savedUser = new User(1L, "New User", "new@example.com");
        when(userRepository.save(userToSave)).thenReturn(savedUser);

        // Act
        User result = userService.save(userToSave);

        // Assert
        assertNotNull(result.getId());
        assertEquals("new@example.com", result.getEmail());
        verify(userRepository).save(userToSave);
    }

    @Test
    void delete_ShouldCallRepositoryDelete() {
        // Arrange
        Long userId = 1L;
        doNothing().when(userRepository).deleteById(userId);

        // Act
        userService.delete(userId);

        // Assert
        verify(userRepository).deleteById(userId);
    }
}