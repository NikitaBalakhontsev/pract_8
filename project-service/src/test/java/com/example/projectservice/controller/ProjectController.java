package com.example.projectservice.controller;

import com.example.projectservice.model.Project;
import com.example.projectservice.service.ProjectService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProjectControllerTest {

    @Mock
    private ProjectService projectService;

    @InjectMocks
    private ProjectController projectController;

    @Test
    void findAll_ShouldReturnAllProjects() {
        // Arrange
        Project project1 = Project.builder().id(1L).title("Project 1").build();
        Project project2 = Project.builder().id(2L).title("Project 2").build();
        List<Project> expectedProjects = Arrays.asList(project1, project2);

        when(projectService.findAll()).thenReturn(expectedProjects);

        // Act
        List<Project> actualProjects = projectController.findAll();

        // Assert
        assertEquals(2, actualProjects.size());
        assertEquals("Project 1", actualProjects.get(0).getTitle());
        verify(projectService).findAll();
    }

    @Test
    void findById_WhenProjectExists_ShouldReturnProject() {
        // Arrange
        Long projectId = 1L;
        Project expectedProject = Project.builder().id(projectId).title("Test Project").build();
        when(projectService.findById(projectId)).thenReturn(expectedProject);

        // Act
        Project actualProject = projectController.findById(projectId);

        // Assert
        assertNotNull(actualProject);
        assertEquals("Test Project", actualProject.getTitle());
        verify(projectService).findById(projectId);
    }

    @Test
    void findById_WhenProjectNotExists_ShouldThrowException() {
        // Arrange
        Long projectId = 999L;
        when(projectService.findById(projectId))
                .thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Project not found"));

        // Act & Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> projectController.findById(projectId));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        verify(projectService).findById(projectId);
    }

    @Test
    void save_ShouldReturnSavedProject() {
        // Arrange
        Project projectToSave = Project.builder().title("New Project").build();
        Project savedProject = Project.builder().id(1L).title("New Project").build();
        when(projectService.save(projectToSave)).thenReturn(savedProject);

        // Act
        Project result = projectController.save(projectToSave);

        // Assert
        assertNotNull(result.getId());
        assertEquals("New Project", result.getTitle());
        verify(projectService).save(projectToSave);
    }

    @Test
    void delete_ShouldCallServiceDelete() {
        // Arrange
        Long projectId = 1L;
        doNothing().when(projectService).delete(projectId);

        // Act
        projectController.delete(projectId);

        // Assert
        verify(projectService).delete(projectId);
    }
}