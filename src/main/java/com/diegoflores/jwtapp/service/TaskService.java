package com.diegoflores.jwtapp.service;

import com.diegoflores.jwtapp.dto.CreateTaskDTO;
import com.diegoflores.jwtapp.dto.TaskResponseDTO;
import com.diegoflores.jwtapp.dto.UpdateTaskDTO;

import java.util.List;

public interface TaskService {


    List<TaskResponseDTO> getTasksByUser(String username);

    TaskResponseDTO  createTask(String username, CreateTaskDTO dto);

    TaskResponseDTO  updateTask(Long id, String username, UpdateTaskDTO dto);

    void deleteTask(Long id, String username);

    TaskResponseDTO  getTaskByIdAndUsername(Long id, String username);

}
