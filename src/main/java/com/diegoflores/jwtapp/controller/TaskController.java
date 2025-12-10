package com.diegoflores.jwtapp.controller;

import com.diegoflores.jwtapp.dto.CreateTaskDTO;
import com.diegoflores.jwtapp.dto.TaskResponseDTO;
import com.diegoflores.jwtapp.dto.UpdateTaskDTO;
import com.diegoflores.jwtapp.mapper.TaskMapper;
import com.diegoflores.jwtapp.model.ApiResponse;
import com.diegoflores.jwtapp.service.TaskService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    private final TaskMapper taskMapper;

    @GetMapping
    public ResponseEntity<List<TaskResponseDTO>> getTasks(@AuthenticationPrincipal UserDetails userDetails){
        List<TaskResponseDTO> dtos = taskService.getTasksByUser(userDetails.getUsername());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getTaskById(@AuthenticationPrincipal UserDetails userDetails,
                                                   @PathVariable Long id){

        try {
            TaskResponseDTO dto = taskService.getTaskByIdAndUsername(id, userDetails.getUsername());

            ApiResponse response = new ApiResponse(
                    "Tarea obtenida correctamente.",
                    HttpStatus.OK.value(),
                    dto
            );
            return ResponseEntity.ok(response);

        }catch (EntityNotFoundException  e){
            ApiResponse response = new ApiResponse(
                    "Tarea no encontrada o no pertenece al usuario.",
                    HttpStatus.NOT_FOUND.value()
            );
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @PostMapping
    public ResponseEntity<ApiResponse> createTask(@AuthenticationPrincipal UserDetails userDetails,
                                                  @RequestBody CreateTaskDTO createTaskDTO){

        TaskResponseDTO dto = taskService.createTask(userDetails.getUsername(), createTaskDTO);

        ApiResponse response = new ApiResponse(
                "Tarea creada con exito." + dto.getTitulo(),
                HttpStatus.CREATED.value(),
                dto
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> updateTask(@AuthenticationPrincipal UserDetails userDetails,
                                                  @RequestBody UpdateTaskDTO updateTaskDTO,
                                                  @PathVariable Long id){
        //Actualizamos
        TaskResponseDTO dto = taskService.updateTask(id, userDetails.getUsername(), updateTaskDTO);
        //Construir respuesta
        ApiResponse response = new ApiResponse(
              "Tarea actualizada con exito.",
              HttpStatus.OK.value(),
                dto
        );

        //Devolvemos 200 OK con el cuerpo JSON
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteTask(@AuthenticationPrincipal UserDetails userDetails,
                                                  @PathVariable Long id){
        //Ejecutamos la eliminación
        taskService.deleteTask(id, userDetails.getUsername());

        ApiResponse response = new ApiResponse(
                "Tarea eliminada con éxito.",
                HttpStatus.OK.value()
        );
        //Retornamos 200
        return ResponseEntity.ok(response);
    }
}
