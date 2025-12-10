package com.diegoflores.jwtapp.mapper;


import com.diegoflores.jwtapp.dto.UserDTO;
import com.diegoflores.jwtapp.model.Role;
import com.diegoflores.jwtapp.model.User;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class UserMapper {

    public UserDTO toDTO(User user) {
        return UserDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .roles(user.getRoles().stream()
                        .map(Role::getName)
                        .collect(Collectors.toSet()))
                .build();
    }

    public User toEntity(UserDTO userDTO) {
        User user = new User();
        user.setId(userDTO.getId());
        user.setUsername(userDTO.getUsername());
        user.setEmail(userDTO.getEmail());
        // Los roles se asignarían con RoleRepository
        return user;
    }

}
