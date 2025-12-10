package com.diegoflores.jwtapp.dto;

import lombok.*;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO {
    private Long id;
    private String username;
    private String email;
    private Set<String> roles; // Por ejemplo: ["ROLE_USER", "ROLE_ADMIN"]
}
