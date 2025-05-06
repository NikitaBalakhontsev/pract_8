package com.example.taskservice.service;

import com.example.taskservice.client.UserClient;
import com.example.taskservice.dto.TaskWithUsernameDTO;
import com.example.taskservice.model.Task;
import com.example.taskservice.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserClient userClient;

    public Task create(Task task) {
        // Демонстрация вызова другого сервиса
        String username = userClient.getUsernameById(task.getUserId()).getName();
        System.out.println("Создана задача для пользователя: " + username);
        return taskRepository.save(task);
    }

    public List<Task> findAll() {
        return taskRepository.findAll();
    }

    public List<Task> findByProjectId(Long projectId) {
        return taskRepository.findByProjectId(projectId);
    }

    public Optional<Task> findById(Long id) {
        return taskRepository.findById(id);
    }

    public void delete(Long id) {
        taskRepository.deleteById(id);
    }

    public Task update(Long id, Task task) {
        Task existing = taskRepository.findById(id).orElseThrow();
        existing.setTitle(task.getTitle());
        existing.setDescription(task.getDescription());
        existing.setStatus(task.getStatus());
        existing.setUserId(task.getUserId());
        return taskRepository.save(existing);
    }

    public List<TaskWithUsernameDTO> findAllWithUsernames() {
        return taskRepository.findAll().stream()
                .map(task -> {
                    String username = userClient.getUsernameById(task.getUserId()).getName();
                    return new TaskWithUsernameDTO(
                            task.getId(),
                            task.getTitle(),
                            task.getDescription(),
                            task.getStatus(),
                            task.getProjectId(),
                            task.getUserId(),
                            username
                    );
                })
                .collect(Collectors.toList());
    }

    public TaskWithUsernameDTO findByIdWithUsername(Long id) {
        Task task = findById(id).orElseThrow();
        String username = userClient.getUsernameById(task.getUserId()).getName();
        return new TaskWithUsernameDTO(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getStatus(),
                task.getProjectId(),
                task.getUserId(),
                username
        );
    }
}
