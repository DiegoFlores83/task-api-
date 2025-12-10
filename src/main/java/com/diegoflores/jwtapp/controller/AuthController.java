package com.diegoflores.jwtapp.controller;


import com.diegoflores.jwtapp.dto.LoginRequest;
import com.diegoflores.jwtapp.dto.UserDTO;
import com.diegoflores.jwtapp.mapper.UserMapper;
import com.diegoflores.jwtapp.model.ApiResponse;
import com.diegoflores.jwtapp.security.JwtProvider;
import com.diegoflores.jwtapp.security.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
import java.util.stream.Collectors;



@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:4200")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtProvider jwtProvider;

    @Autowired
    private UserMapper userMapper;



    //Endpoint de login
    @PostMapping("/login")
    public ResponseEntity<ApiResponse> login(@RequestBody LoginRequest loginRequest){
        try {
            //Intentar autenticar usuario
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(),
                            loginRequest.getPassword()
                    )
            );

            //Obtener los detalles del usuario
            UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

            //Generar el token JWT
            String token = jwtProvider.generateToken(userPrincipal);

            // Crear UserDTO
            UserDTO userDTO = UserDTO.builder()
                    .id(userPrincipal.getId())
                    .username(userPrincipal.getUsername())
                    .email(userPrincipal.getEmail())
                    .roles(
                            userPrincipal.getAuthorities().stream()
                                    .map(GrantedAuthority::getAuthority)
                                    .collect(Collectors.toSet())
                    )
                    .build();

            ApiResponse response = ApiResponse.builder()
                    .message("Login exitoso")
                    .status(HttpStatus.OK.value())
                    .data(Map.of(
                            "token", token,
                            "user", userDTO
                    ))
                    .build();

            return ResponseEntity.ok(response);

        } catch (AuthenticationException e){
            //Si las credenciales son incorrectas
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiResponse("Credenciales invalidas", HttpStatus.UNAUTHORIZED.value()));
        }
    }
}
