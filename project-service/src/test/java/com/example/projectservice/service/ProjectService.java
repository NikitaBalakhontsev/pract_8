package com.example.projectservice.service;

import com.example.projectservice.model.Project;
import com.example.projectservice.repository.ProjectRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@ExtendWith(MockitoExtension.class)
class ProjectServiceTest {

    @Mock
    private ProjectRepository projectRepository;

    @InjectMocks
    private ProjectService projectService;

    @Test
    void findAll_ShouldReturnAllProjects() {
        // Arrange
        Project project1 = Project.builder().id(1L).title("Project 1").build();
        Project project2 = Project.builder().id(2L).title("Project 2").build();
        List<Project> expectedProjects = Arrays.asList(project1, project2);

        when(projectRepository.findAll()).thenReturn(expectedProjects);

        // Act
        List<Project> actualProjects = projectService.findAll();

        // Assert
        assertEquals(2, actualProjects.size());
        assertEquals("Project 1", actualProjects.get(0).getTitle());
        verify(projectRepository).findAll();
    }

    @Test
    void findById_WhenProjectExists_ShouldReturnProject() {
        // Arrange
        Long projectId = 1L;
        Project expectedProject = Project.builder().id(projectId).title("Test Project").build();
        when(projectRepository.findById(projectId)).thenReturn(Optional.of(expectedProject));

        // Act
        Project actualProject = projectService.findById(projectId);

        // Assert
        assertNotNull(actualProject);
        assertEquals("Test Project", actualProject.getTitle());
        verify(projectRepository).findById(projectId);
    }

    @Test
    void findById_WhenProjectNotExists_ShouldThrowException() {
        // Arrange
        Long projectId = 999L;
        when(projectRepository.findById(projectId)).thenReturn(Optional.empty());

        // Act & Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> projectService.findById(projectId));

        assertEquals(NOT_FOUND, exception.getStatusCode());
        assertEquals("Project not found", exception.getReason());
        verify(projectRepository).findById(projectId);
    }

    @Test
    void save_ShouldReturnSavedProject() {
        // Arrange
        Project projectToSave = Project.builder().title("New Project").build();
        Project savedProject = Project.builder().id(1L).title("New Project").build();
        when(projectRepository.save(projectToSave)).thenReturn(savedProject);

        // Act
        Project result = projectService.save(projectToSave);

        // Assert
        assertNotNull(result.getId());
        assertEquals("New Project", result.getTitle());
        verify(projectRepository).save(projectToSave);
    }

    @Test
    void delete_ShouldCallRepositoryDelete() {
        // Arrange
        Long projectId = 1L;
        doNothing().when(projectRepository).deleteById(projectId);

        // Act
        projectService.delete(projectId);

        // Assert
        verify(projectRepository).deleteById(projectId);
    }
}