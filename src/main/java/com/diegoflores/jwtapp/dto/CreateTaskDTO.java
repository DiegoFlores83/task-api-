package com.diegoflores.jwtapp.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateTaskDTO {

    private String titulo;
    private String descripcion;
}
