package com.example.taskservice.service;

import com.example.taskservice.client.UserClient;
import com.example.taskservice.dto.NameResponse;
import com.example.taskservice.dto.TaskWithUsernameDTO;
import com.example.taskservice.model.Task;
import com.example.taskservice.repository.TaskRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private UserClient userClient;

    @InjectMocks
    private TaskService taskService;

    @Test
    void create_ShouldSaveTaskAndLogUsername() {
        // Arrange
        Task taskToSave = new Task();
        taskToSave.setUserId(1L);
        Task savedTask = new Task();
        savedTask.setId(1L);
        savedTask.setUserId(1L);

        when(userClient.getUsernameById(1L)).thenReturn(new NameResponse("testUser"));
        when(taskRepository.save(taskToSave)).thenReturn(savedTask);

        // Act
        Task result = taskService.create(taskToSave);

        // Assert
        assertNotNull(result.getId());
        verify(userClient).getUsernameById(1L);
        verify(taskRepository).save(taskToSave);
    }

    @Test
    void findAll_ShouldReturnAllTasks() {
        // Arrange
        Task task1 = new Task();
        task1.setId(1L);
        Task task2 = new Task();
        task2.setId(2L);
        List<Task> expectedTasks = List.of(task1, task2);

        when(taskRepository.findAll()).thenReturn(expectedTasks);

        // Act
        List<Task> actualTasks = taskService.findAll();

        // Assert
        assertEquals(2, actualTasks.size());
        verify(taskRepository).findAll();
    }

    @Test
    void findByProjectId_ShouldReturnFilteredTasks() {
        // Arrange
        Task task1 = new Task();
        task1.setId(1L);
        task1.setProjectId(100L);
        Task task2 = new Task();
        task2.setId(2L);
        task2.setProjectId(100L);
        List<Task> expectedTasks = List.of(task1, task2);

        when(taskRepository.findByProjectId(100L)).thenReturn(expectedTasks);

        // Act
        List<Task> actualTasks = taskService.findByProjectId(100L);

        // Assert
        assertEquals(2, actualTasks.size());
        verify(taskRepository).findByProjectId(100L);
    }

    @Test
    void findById_WhenTaskExists_ShouldReturnTask() {
        // Arrange
        Long taskId = 1L;
        Task expectedTask = new Task();
        expectedTask.setId(taskId);

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(expectedTask));

        // Act
        Optional<Task> actualTask = taskService.findById(taskId);

        // Assert
        assertTrue(actualTask.isPresent());
        assertEquals(taskId, actualTask.get().getId());
        verify(taskRepository).findById(taskId);
    }

    @Test
    void delete_ShouldCallRepositoryDelete() {
        // Arrange
        Long taskId = 1L;
        doNothing().when(taskRepository).deleteById(taskId);

        // Act
        taskService.delete(taskId);

        // Assert
        verify(taskRepository).deleteById(taskId);
    }

    @Test
    void update_ShouldUpdateExistingTask() {
        // Arrange
        Long taskId = 1L;
        Task existingTask = new Task();
        existingTask.setId(taskId);
        existingTask.setTitle("Old Title");

        Task updateData = new Task();
        updateData.setTitle("New Title");
        updateData.setDescription("New Desc");
        updateData.setStatus("DONE");

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(existingTask));
        when(taskRepository.save(existingTask)).thenReturn(existingTask);

        // Act
        Task result = taskService.update(taskId, updateData);

        // Assert
        assertEquals("New Title", result.getTitle());
        assertEquals("New Desc", result.getDescription());
        assertEquals("DONE", result.getStatus());
        verify(taskRepository).save(existingTask);
    }

    @Test
    void findAllWithUsernames_ShouldReturnDTOsWithUsernames() {
        // Arrange
        Task task1 = new Task();
        task1.setId(1L);
        task1.setUserId(100L);
        Task task2 = new Task();
        task2.setId(2L);
        task2.setUserId(200L);
        List<Task> tasks = List.of(task1, task2);

        when(taskRepository.findAll()).thenReturn(tasks);
        when(userClient.getUsernameById(100L)).thenReturn(new NameResponse("User1"));
        when(userClient.getUsernameById(200L)).thenReturn(new NameResponse("User2"));

        // Act
        List<TaskWithUsernameDTO> result = taskService.findAllWithUsernames();

        // Assert
        assertEquals(2, result.size());
        assertEquals("User1", result.get(0).getUsername());
        assertEquals("User2", result.get(1).getUsername());
        verify(taskRepository).findAll();
        verify(userClient, times(2)).getUsernameById(anyLong());
    }

    @Test
    void findByIdWithUsername_ShouldReturnDTOWithUsername() {
        // Arrange
        Long taskId = 1L;
        Task task = new Task();
        task.setId(taskId);
        task.setUserId(100L);

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));
        when(userClient.getUsernameById(100L)).thenReturn(new NameResponse("TestUser"));

        // Act
        TaskWithUsernameDTO result = taskService.findByIdWithUsername(taskId);

        // Assert
        assertNotNull(result);
        assertEquals(taskId, result.getId());
        assertEquals("TestUser", result.getUsername());
        verify(taskRepository).findById(taskId);
        verify(userClient).getUsernameById(100L);
    }
}