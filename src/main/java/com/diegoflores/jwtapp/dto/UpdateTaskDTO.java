package com.diegoflores.jwtapp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateTaskDTO {

    private String titulo;
    private String descripcion;
    private Boolean completed; // boolean con B mayuscula para permitir null
}
