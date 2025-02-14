package com.todoapp.todo_app.service;

import com.todoapp.todo_app.dto.TaskDto;
import com.todoapp.todo_app.entity.Task;
import com.todoapp.todo_app.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class TaskService {

    private final TaskRepository taskRepository;
    private final ModelMapper modelMapper;
    private final ApiGatewayService apiGatewayService;

    private TaskDto convertToDto(Task task) {
        return modelMapper.map(task, TaskDto.class);
    }

    public TaskDto addTask(TaskDto taskDto) {
        Task task = new Task();
        task.setTitle(taskDto.getTitle());
        task.setDescription(taskDto.getDescription());
        task.setCompleted(taskDto.isCompleted());
        task = taskRepository.save(task);
        apiGatewayService.sendMessageToApiGateway(new TaskDto(task.getId(), task.getTitle(), task.getDescription(), task.isCompleted()));

        return convertToDto(task);
    }

    public TaskDto getTaskById(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> {
                    return new RuntimeException("Tâche non trouvée");
                });
        return convertToDto(task);
    }

    public List<TaskDto> getAllTasks() {
        List<Task> tasks = taskRepository.findAll();
        return tasks.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public TaskDto updateTask(Long id, TaskDto taskDto) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> {
                    return new RuntimeException("Tâche non trouvée");
                });
        task.setTitle(taskDto.getTitle());
        task.setDescription(taskDto.getDescription());
        task.setCompleted(taskDto.isCompleted());
        task = taskRepository.save(task);
        apiGatewayService.sendMessageToApiGateway(new TaskDto(task.getId(), task.getTitle(), task.getDescription(), task.isCompleted()));
        return convertToDto(task);
    }

    public void deleteTask(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> {
                    return new RuntimeException("Tâche non trouvée");
                });
        taskRepository.deleteById(id);
        apiGatewayService.sendMessageToApiGateway(new TaskDto(task.getId(), task.getTitle(), task.getDescription(), task.isCompleted()));
    }
}
