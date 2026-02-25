package com.anrikot.manabi.services;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.anrikot.manabi.domain.User;
import com.anrikot.manabi.domain.UserRole;
import com.anrikot.manabi.dto.AuthDTO;
import com.anrikot.manabi.dto.EmailRequestDTO;
import com.anrikot.manabi.dto.PasswordRequestDTO;
import com.anrikot.manabi.dto.RegisterDTO;
import com.anrikot.manabi.dto.UserDTO;
import com.anrikot.manabi.repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class UserService implements UserDetailsService {
    private final UserRepository repository;
    private final PasswordEncoder encoder;

    public UserService(UserRepository repository, PasswordEncoder encoder) {
        this.repository = repository;
        this.encoder = encoder;
    }

    @Override
    public User loadUserByUsername(String username) {
        return repository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    public List<UserDTO> findAll() {
        return repository.findAll().stream()
            .map(this::toDTO)
            .toList();
    }

    public UserDTO findByUsername(String username) {
        User user = repository.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("User not found"));
        return toDTO(user);
    }
    
    public UserDTO findById(Long id) {
        if (id == null) return null;
        User user = repository.findById(null)
            .orElseThrow(() -> new RuntimeException("User not found"));
        
        return toDTO(user);
    }

    @Transactional
    public UserDTO updateEmail(EmailRequestDTO req, String username) {
        User u = repository.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("Invalid user."));
        if (repository.existsByEmail(req.email())) throw new RuntimeException("Email already registered.");

        u.setEmail(req.email());
        repository.save(u);
        return toDTO(u);
    }

    @Transactional
    public void updatePassword(PasswordRequestDTO req, String username) {
        User u = repository.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("Invalid user."));
        if (!encoder.matches(req.oldPassword(), u.getPassword())) 
            throw new RuntimeException("Old password is incorrect.");

        String newPassword = encoder.encode(req.oldPassword());
        u.setPassword(newPassword);
        repository.save(u);
    }

    @Transactional
    public void changeRole(Long userId, UserRole newRole) {
        User u = repository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found."));
        u.setRole(newRole);
        repository.save(u);
    }
    
    @Transactional
    public void save(RegisterDTO user) {
        if (user == null) throw new RuntimeException("User is null");
        String encryptedPassword = encoder.encode(user.password());

        User u = new User();
        u.setUsername(user.username());
        u.setEmail(user.email());
        u.setRole(user.role());
        u.setPassword(encryptedPassword);

        repository.save(u);
    }

    @Transactional
    public void deleteByUsername(AuthDTO login, String username) {
        User u = repository.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("User does not exists."));
        
        if (!login.login().equals(username)) throw new RuntimeException("Login username and target username do not match.");
        if (!encoder.matches(login.password(), u.getPassword())) throw new RuntimeException("Password is incorrect.");
        
        repository.deleteByUsername(username);
    }

    @Transactional
    public void deleteById(Long id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("User does not exist.");
        }
        repository.deleteById(id);
    }

    public boolean existsByEmail(String email) {
        return repository.existsByEmail(email);
    }

    private UserDTO toDTO(User entity) {
        return new UserDTO(entity.getId(), entity.getUsername(), entity.getEmail());
    }
}