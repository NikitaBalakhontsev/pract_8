package com.example.taskservice.controller;

import com.example.taskservice.model.Task;
import com.example.taskservice.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @PostMapping
    public Task create(@RequestBody Task task) {
        return taskService.create(task);
    }

    @GetMapping
    public List<Task> getAll() {
        return taskService.findAll();
    }

    @GetMapping("/project/{projectId}")
    public List<Task> getByProject(@PathVariable Long projectId) {
        return taskService.findByProjectId(projectId);
    }

    @GetMapping("/{id}")
    public Task getById(@PathVariable Long id) {
        return taskService.findById(id).orElseThrow();
    }

    @PutMapping("/{id}")
    public Task update(@PathVariable Long id, @RequestBody Task task) {
        return taskService.update(id, task);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        taskService.delete(id);
    }

    @GetMapping("/{id}/username")
    public String getUserNameByTask(@PathVariable Long id) {
        Task task = taskService.findById(id).orElseThrow();
        return taskService.getUserName(task.getUserId());
    }
}
