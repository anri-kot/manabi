package com.anrikot.manabi.mappers;

import com.anrikot.manabi.domain.User;
import com.anrikot.manabi.dto.RegisterDTO;

public class UserMapper{
    public static RegisterDTO toDTO(User entity) {
        return new RegisterDTO(entity.getUsername(), entity.getPassword(), entity.getEmail(), entity.getRole());
    }
    
    public static User toEntity(RegisterDTO dto) {
        User u = new User();
        u.setUsername(dto.username());
        u.setEmail(dto.email());
        u.setPassword(dto.password());
        u.setRole(dto.role());

        return u;
    }
}