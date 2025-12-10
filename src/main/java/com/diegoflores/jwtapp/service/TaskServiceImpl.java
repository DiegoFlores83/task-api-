package com.diegoflores.jwtapp.service;

import com.diegoflores.jwtapp.dto.CreateTaskDTO;
import com.diegoflores.jwtapp.dto.TaskResponseDTO;
import com.diegoflores.jwtapp.dto.UpdateTaskDTO;
import com.diegoflores.jwtapp.exceptions.ResourceNotFoundException;
import com.diegoflores.jwtapp.mapper.TaskMapper;
import com.diegoflores.jwtapp.model.Task;
import com.diegoflores.jwtapp.model.User;
import com.diegoflores.jwtapp.repository.TaskRepository;
import com.diegoflores.jwtapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;

    private final UserRepository userRepository;

    private final TaskMapper taskMapper;

    @Override
    public List<TaskResponseDTO> getTasksByUser(String username){
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado" + username));

        return taskRepository.findByUser(user).stream()
                .map(taskMapper::toResponseDTO)
                .toList();
    }

    @Override
    public TaskResponseDTO  getTaskByIdAndUsername(Long id, String username) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado: " + username));

        Task task = taskRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new ResourceNotFoundException("Tarea no encontrada o no pertenece al usuario"));

        return taskMapper.toResponseDTO(task);
    }

    @Override
    public TaskResponseDTO  createTask(String username, CreateTaskDTO dto){
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado" + username));

        Task task = taskMapper.toEntity(dto, user);

        Task saved = taskRepository.save(task);

        return  taskMapper.toResponseDTO(saved);
    }

    @Override
    public TaskResponseDTO  updateTask(Long id, String username, UpdateTaskDTO  dto){
        User user = userRepository.findByUsername(username)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Usuario no encontrado." + username));

        Task task = taskRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("La tarea con ID " + id + " no existe."));

           if(!task.getUser().getId().equals(user.getId()))
               throw new RuntimeException("No Autorizado para modificar esta tarea.");

           taskMapper.updateEntity(task, dto);

           Task update = taskRepository.save(task);

           return taskMapper.toResponseDTO(update);
    }

    @Override
    public void deleteTask(Long id, String username){
       User user = userRepository.findByUsername(username)
               .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado."));

       Task task = taskRepository.findById(id)
               .orElseThrow(() -> new ResourceNotFoundException("Tarea no encontrada."));

            if(!task.getUser().getId().equals(user.getId()))
                throw new RuntimeException("No Autorizado para eliminar esta tarea.");

            taskRepository.delete(task);
    }

}
