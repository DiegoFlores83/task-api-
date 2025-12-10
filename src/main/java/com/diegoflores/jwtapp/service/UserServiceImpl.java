package com.diegoflores.jwtapp.service;

import com.diegoflores.jwtapp.dto.UserCreateDTO;
import com.diegoflores.jwtapp.dto.UserDTO;
import com.diegoflores.jwtapp.dto.UserRegisterDTO;
import com.diegoflores.jwtapp.exceptions.ResourceNotFoundException;
import com.diegoflores.jwtapp.mapper.UserMapper;
import com.diegoflores.jwtapp.model.Role;
import com.diegoflores.jwtapp.model.User;
import com.diegoflores.jwtapp.repository.RoleRepository;
import com.diegoflores.jwtapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserMapper userMapper;

    @Override
    public UserDTO register(UserRegisterDTO dto) {

        // Validar username
        if (userRepository.existsByUsername(dto.getUsername())) {
            throw new ResourceNotFoundException("El usuario ya existe");
        }

        // Validar email
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new ResourceNotFoundException("El email ya existe");
        }

        // Crear usuario
        User user = new User();
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));

        // Roles
        Set<Role> roles = new HashSet<>();

        if (dto.getRoles() == null || dto.getRoles().isEmpty()) {
            Role roleUser = roleRepository.findByName("ROLE_USER")
                    .orElseThrow(() -> new ResourceNotFoundException("Rol no encontrado"));
            roles.add(roleUser);
        } else {
            dto.getRoles().forEach(roleName -> {
                Role role = roleRepository
                        .findByName("ROLE_" + roleName.toUpperCase())
                        .orElseThrow(() -> new ResourceNotFoundException("Role no encontrado: " + roleName));
                roles.add(role);
            });
        }

        user.setRoles(roles);

        userRepository.save(user);

        return userMapper.toDTO(user);
    }

    @Override
    public UserDTO createUser(UserCreateDTO dto){
        // Validar username
        if (userRepository.existsByUsername(dto.getUsername())) {
            throw new ResourceNotFoundException("El usuario ya existe");
        }

        // Validar email
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new ResourceNotFoundException("El email ya existe");
        }

        User user = new User();
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));

        //Asignar roles
        Set<Role> roles = new HashSet<>();

        if (dto.getRoles() == null || dto.getRoles().isEmpty()) {
            Role userRole = roleRepository.findByName("ROLE_USER")
                    .orElseThrow(() -> new RuntimeException("Rol USER no encontrado"));
            roles.add(userRole);
        } else {
            dto.getRoles().forEach(roleName -> {
                Role role = roleRepository.findByName("ROLE_" + roleName.toUpperCase())
                        .orElseThrow(() -> new RuntimeException("Rol no encontrado: " + roleName));
                roles.add(role);
            });
        }

        user.setRoles(roles);

        User saved = userRepository.save(user);

        return userMapper.toDTO(saved);
    }


    @Override
    public List<UserDTO> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(userMapper::toDTO)
                .collect(Collectors.toList());
    }
}

