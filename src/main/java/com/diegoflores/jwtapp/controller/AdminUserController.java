package com.diegoflores.jwtapp.controller;

import com.diegoflores.jwtapp.dto.UserCreateDTO;
import com.diegoflores.jwtapp.dto.UserDTO;
import com.diegoflores.jwtapp.model.ApiResponse;
import com.diegoflores.jwtapp.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/users")
@RequiredArgsConstructor
public class AdminUserController {

    private final UserService userService;

    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> createUser(@RequestBody UserCreateDTO dto){

        UserDTO userDTO = userService.createUser(dto);

        ApiResponse response = ApiResponse.builder()
                .message("Usuario creado exitosamente por ADMIN")
                .status(HttpStatus.CREATED.value())
                .data(userDTO)
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
