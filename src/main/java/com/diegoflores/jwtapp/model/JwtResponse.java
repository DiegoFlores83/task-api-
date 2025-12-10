package com.diegoflores.jwtapp.model;

import lombok.Builder;
import lombok.Data;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
public class JwtResponse {

    private String token;
    private String type;
    private String message;
    private int status;
    private LocalDateTime timestamp;
}
