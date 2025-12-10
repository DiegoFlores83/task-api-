package com.diegoflores.jwtapp.controller;

import com.diegoflores.jwtapp.model.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test")
public class TestController {

    // Endpoint para probar rutas publicas no requiere token
    @GetMapping("/public")
    public ResponseEntity<ApiResponse> publicEndpoint(){
        ApiResponse response = new ApiResponse("Ruta publica accesible sin autenticación", HttpStatus.OK.value());
        return ResponseEntity.ok(response);
    }

    // Endpoint para probar rutas protegidas (requiere JWT)
    @GetMapping("/private")
    public ResponseEntity<ApiResponse> privateEndpoint() {
        ApiResponse apiResponse= new ApiResponse("Acceso concedido: ruta protegida con JWT", HttpStatus.OK.value());
        return ResponseEntity.ok(apiResponse);
    }

    //Ruta solo para administradores
    @GetMapping("/admin")
    public ResponseEntity<ApiResponse> adminEndpoint(){
        ApiResponse response = new ApiResponse("Accesp permitido solo administradores", HttpStatus.OK.value());
        return ResponseEntity.ok(response);
    }
}
