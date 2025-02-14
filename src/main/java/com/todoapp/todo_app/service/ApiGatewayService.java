package com.todoapp.todo_app.service;

import com.todoapp.todo_app.dto.TaskDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ApiGatewayService {

    @Value("${api.gateway.url}")
    private String apiGatewayUrl;
    private final RestTemplate restTemplate;

    public ApiGatewayService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String sendMessageToApiGateway(TaskDto taskDto) {
        String url = apiGatewayUrl + "/tasks";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<TaskDto> request = new HttpEntity<>(taskDto, headers);
        return restTemplate.postForObject(url, request, String.class);
    }
}