package com.example.taskservice.dto;

public class NameResponse {
    private String name;

    public NameResponse() {
    }

    public NameResponse(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
