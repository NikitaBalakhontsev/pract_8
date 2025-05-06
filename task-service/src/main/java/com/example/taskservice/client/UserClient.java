package com.example.taskservice.client;

import com.example.taskservice.dto.NameResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user-service")
public interface UserClient {

    @GetMapping("/users/{id}/name")
    NameResponse getUsernameById(@PathVariable("id") Long id);
}
