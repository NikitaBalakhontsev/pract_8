package com.example.taskservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskWithUsernameDTO {
    private Long id;
    private String title;
    private String description;
    private String status;
    private Long projectId;
    private Long userId;
    private String username;
}
