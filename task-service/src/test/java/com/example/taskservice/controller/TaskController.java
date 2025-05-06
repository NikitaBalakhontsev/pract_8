package com.example.taskservice.controller;

import com.example.taskservice.dto.TaskWithUsernameDTO;
import com.example.taskservice.model.Task;
import com.example.taskservice.service.TaskService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskControllerTest {

    @Mock
    private TaskService taskService;

    @InjectMocks
    private TaskController taskController;

    @Test
    void create_ShouldReturnCreatedTask() {
        // Arrange
        Task taskToCreate = new Task();
        Task createdTask = new Task();
        createdTask.setId(1L);

        when(taskService.create(taskToCreate)).thenReturn(createdTask);

        // Act
        Task result = taskController.create(taskToCreate);

        // Assert
        assertNotNull(result.getId());
        verify(taskService).create(taskToCreate);
    }

    @Test
    void getAll_ShouldReturnAllTasks() {
        // Arrange
        Task task1 = new Task();
        task1.setId(1L);
        Task task2 = new Task();
        task2.setId(2L);
        List<Task> expectedTasks = List.of(task1, task2);

        when(taskService.findAll()).thenReturn(expectedTasks);

        // Act
        List<Task> actualTasks = taskController.getAll();

        // Assert
        assertEquals(2, actualTasks.size());
        verify(taskService).findAll();
    }

    @Test
    void getAllWithUsernames_ShouldReturnDTOs() {
        // Arrange
        TaskWithUsernameDTO dto1 = new TaskWithUsernameDTO();
        dto1.setId(1L);
        TaskWithUsernameDTO dto2 = new TaskWithUsernameDTO();
        dto2.setId(2L);
        List<TaskWithUsernameDTO> expectedDTOs = List.of(dto1, dto2);

        when(taskService.findAllWithUsernames()).thenReturn(expectedDTOs);

        // Act
        List<TaskWithUsernameDTO> result = taskController.getAllWithUsernames();

        // Assert
        assertEquals(2, result.size());
        verify(taskService).findAllWithUsernames();
    }

    @Test
    void getWithUsername_ShouldReturnDTO() {
        // Arrange
        Long taskId = 1L;
        TaskWithUsernameDTO expectedDTO = new TaskWithUsernameDTO();
        expectedDTO.setId(taskId);

        when(taskService.findByIdWithUsername(taskId)).thenReturn(expectedDTO);

        // Act
        TaskWithUsernameDTO result = taskController.getWithUsername(taskId);

        // Assert
        assertEquals(taskId, result.getId());
        verify(taskService).findByIdWithUsername(taskId);
    }

    @Test
    void getByProject_ShouldReturnFilteredTasks() {
        // Arrange
        Long projectId = 100L;
        Task task1 = new Task();
        task1.setId(1L);
        task1.setProjectId(projectId);
        List<Task> expectedTasks = List.of(task1);

        when(taskService.findByProjectId(projectId)).thenReturn(expectedTasks);

        // Act
        List<Task> result = taskController.getByProject(projectId);

        // Assert
        assertEquals(1, result.size());
        assertEquals(projectId, result.get(0).getProjectId());
        verify(taskService).findByProjectId(projectId);
    }

    @Test
    void getById_WhenTaskExists_ShouldReturnTask() {
        // Arrange
        Long taskId = 1L;
        Task expectedTask = new Task();
        expectedTask.setId(taskId);

        when(taskService.findById(taskId)).thenReturn(Optional.of(expectedTask));

        // Act
        Task result = taskController.getById(taskId);

        // Assert
        assertEquals(taskId, result.getId());
        verify(taskService).findById(taskId);
    }


    @Test
    void update_ShouldReturnUpdatedTask() {
        // Arrange
        Long taskId = 1L;
        Task updateData = new Task();
        updateData.setTitle("New Title");
        Task updatedTask = new Task();
        updatedTask.setId(taskId);
        updatedTask.setTitle("New Title");

        when(taskService.update(taskId, updateData)).thenReturn(updatedTask);

        // Act
        Task result = taskController.update(taskId, updateData);

        // Assert
        assertEquals("New Title", result.getTitle());
        verify(taskService).update(taskId, updateData);
    }

    @Test
    void delete_ShouldCallServiceDelete() {
        // Arrange
        Long taskId = 1L;
        doNothing().when(taskService).delete(taskId);

        // Act
        taskController.delete(taskId);

        // Assert
        verify(taskService).delete(taskId);
    }
}