package com.diegoflores.jwtapp.mapper;

import com.diegoflores.jwtapp.dto.CreateTaskDTO;
import com.diegoflores.jwtapp.dto.UpdateTaskDTO;
import com.diegoflores.jwtapp.dto.TaskResponseDTO;
import com.diegoflores.jwtapp.model.Task;
import com.diegoflores.jwtapp.model.User;
import org.springframework.stereotype.Component;

@Component
public class TaskMapper {

    // CreateTaskDTO -> Task
    public Task toEntity(CreateTaskDTO dto, User user) {
        return Task.builder()
                .titulo(dto.getTitulo())
                .descripcion(dto.getDescripcion())
                .completed(false)
                .user(user)
                .build();
    }

    // UpdateTaskDTO -> Task existente
    public void updateEntity(Task task, UpdateTaskDTO dto) {
        task.setTitulo(dto.getTitulo());
        task.setDescripcion(dto.getDescripcion());

        if (dto.getCompleted() != null) {
            task.setCompleted(dto.getCompleted());
        }
    }

    // Task -> TaskResponseDTO
    public TaskResponseDTO toResponseDTO(Task task) {
        TaskResponseDTO dto = new TaskResponseDTO();
        dto.setId(task.getId());
        dto.setTitulo(task.getTitulo());
        dto.setDescripcion(task.getDescripcion());
        dto.setCompleted(task.isCompleted());
        return dto;
    }
}
