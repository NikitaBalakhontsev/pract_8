package com.example.userservice.controller;

import com.example.userservice.model.NameResponse;
import com.example.userservice.model.User;
import com.example.userservice.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @Test
    void getAll_ShouldReturnAllUsers() {
        // Arrange
        User user1 = new User(1L, "User1", "user1@example.com");
        User user2 = new User(2L, "User2", "user2@example.com");
        List<User> expectedUsers = Arrays.asList(user1, user2);

        when(userService.findAll()).thenReturn(expectedUsers);

        // Act
        List<User> actualUsers = userController.getAll();

        // Assert
        assertEquals(2, actualUsers.size());
        assertEquals("user2@example.com", actualUsers.get(1).getEmail());
        verify(userService).findAll();
    }

    @Test
    void getById_WhenUserExists_ShouldReturnUser() {
        // Arrange
        Long userId = 1L;
        User expectedUser = new User(userId, "Test User", "test@example.com");
        when(userService.findById(userId)).thenReturn(Optional.of(expectedUser));

        // Act
        ResponseEntity<User> response = userController.getById(userId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("test@example.com", response.getBody().getEmail());
        verify(userService).findById(userId);
    }

    @Test
    void getById_WhenUserNotExists_ShouldReturnNotFound() {
        // Arrange
        Long userId = 999L;
        when(userService.findById(userId)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<User> response = userController.getById(userId);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(userService).findById(userId);
    }

    @Test
    void create_ShouldReturnCreatedUser() {
        // Arrange
        User userToCreate = new User(null, "New User", "new@example.com");
        User createdUser = new User(1L, "New User", "new@example.com");
        when(userService.save(userToCreate)).thenReturn(createdUser);

        // Act
        User result = userController.create(userToCreate);

        // Assert
        assertNotNull(result.getId());
        assertEquals("new@example.com", result.getEmail());
        verify(userService).save(userToCreate);
    }

    @Test
    void delete_ShouldReturnNoContent() {
        // Arrange
        Long userId = 1L;
        doNothing().when(userService).delete(userId);

        // Act
        ResponseEntity<Void> response = userController.delete(userId);

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(userService).delete(userId);
    }

    @Test
    void getName_WhenUserExists_ShouldReturnNameResponse() {
        // Arrange
        Long userId = 1L;
        User user = new User(userId, "Test User", "test@example.com");
        when(userService.findById(userId)).thenReturn(Optional.of(user));

        // Act
        NameResponse response = userController.getName(userId);

        // Assert
        assertEquals("Test User", response.getName());
        verify(userService).findById(userId);
    }

    @Test
    void getName_WhenUserNotExists_ShouldReturnUnknown() {
        // Arrange
        Long userId = 999L;
        when(userService.findById(userId)).thenReturn(Optional.empty());

        // Act
        NameResponse response = userController.getName(userId);

        // Assert
        assertEquals("Unknown", response.getName());
        verify(userService).findById(userId);
    }
}