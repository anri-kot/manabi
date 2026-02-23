package com.anrikot.manabi.services;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.anrikot.manabi.domain.User;
import com.anrikot.manabi.dto.RegisterDTO;
import com.anrikot.manabi.mappers.UserMapper;
import com.anrikot.manabi.repository.UserRepository;

@Service
public class UserService implements UserDetailsService {
    private final UserRepository repository;

    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public User loadUserByUsername(String username) {
        return repository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    public List<RegisterDTO> findAll() {
        return repository.findAll().stream()
            .map(UserMapper::toDTO)
            .toList();
    }
    
    public RegisterDTO findById(String id) {
        if (id.isBlank() || id == null) return null;
        User user = repository.findById(null)
            .orElseThrow(() -> new RuntimeException("User not found"));
        
        return UserMapper.toDTO(user);
    }
    
    public void save(RegisterDTO user) {
        if (user == null) throw new RuntimeException("User is null");
        String encryptedPassword = new BCryptPasswordEncoder().encode(user.password());

        User u = UserMapper.toEntity(user);
        u.setPassword(encryptedPassword);

        repository.save(u);
    }

    public boolean existsByEmail(String email) {
        return repository.existsByEmail(email);
    }
}