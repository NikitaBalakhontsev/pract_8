package com.example.projectservice.controller;

import com.example.projectservice.model.Project;
import com.example.projectservice.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/projects")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectRepository projectRepository;

    @GetMapping
    public List<Project> findAll() {
        return projectRepository.findAll();
    }

    @GetMapping("/{id}")
    public Project findById(@PathVariable Long id) {
        return projectRepository.findById(id).orElseThrow();
    }

    @PostMapping
    public Project save(@RequestBody Project project) {
        return projectRepository.save(project);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        projectRepository.deleteById(id);
    }
}
