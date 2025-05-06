package com.example.taskservice.service;

import com.example.taskservice.model.Task;
import com.example.taskservice.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final RestTemplate restTemplate;

    public Task create(Task task) {
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
        existing.setProjectId(task.getProjectId());
        return taskRepository.save(existing);
    }

    public String getUserName(Long userId) {
        return restTemplate.getForObject("http://user-service/users/" + userId + "/name", String.class);
    }
}
