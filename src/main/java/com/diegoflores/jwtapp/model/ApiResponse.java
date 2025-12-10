package com.diegoflores.jwtapp.model;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApiResponse {

    private String message;
    private int status;
    private Object data;

    @Builder.Default
    private LocalDateTime timestamp = LocalDateTime.now();

    // Constructor personalizado sin data
    public ApiResponse(String message, int status) {
        this.message = message;
        this.status = status;
        this.timestamp = LocalDateTime.now();
    }

    // Constructor personalizado con data
    public ApiResponse(String message, int status, Object data) {
        this.message = message;
        this.status = status;
        this.data = data;
        this.timestamp = LocalDateTime.now();
    }
}
