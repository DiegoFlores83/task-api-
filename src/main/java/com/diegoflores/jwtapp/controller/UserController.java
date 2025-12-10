package com.diegoflores.jwtapp.controller;

import com.diegoflores.jwtapp.dto.UserDTO;
import com.diegoflores.jwtapp.dto.UserRegisterDTO;
import com.diegoflores.jwtapp.exceptions.ResourceNotFoundException;
import com.diegoflores.jwtapp.mapper.UserMapper;
import com.diegoflores.jwtapp.model.ApiResponse;
import com.diegoflores.jwtapp.model.Role;
import com.diegoflores.jwtapp.model.User;
import com.diegoflores.jwtapp.repository.RoleRepository;
import com.diegoflores.jwtapp.repository.UserRepository;
import com.diegoflores.jwtapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;


    //Registrar usuario nuevo
    @PostMapping("/register")
    public ResponseEntity<ApiResponse> register(@RequestBody UserRegisterDTO dto){

       UserDTO userDTO = userService.register(dto);

        // Retornar con ApiResponse
        ApiResponse response = ApiResponse.builder()
                .message("Usuario registrado correctamente")
                .status(HttpStatus.CREATED.value())
                .data(userDTO)
                .build();

        return ResponseEntity.status(HttpStatus.CREATED)// 201
                .body(response);
    }

    // Obtener todos los usuarios
    @GetMapping
    public ResponseEntity<ApiResponse> getAllUsers(){
        List<UserDTO> users = userService.getAllUsers();

        ApiResponse response = ApiResponse.builder()
                .message("Lista de usuarios")
                .status(HttpStatus.OK.value())
                .data(users)
                .build();

        return ResponseEntity.ok(response);
    }
}
