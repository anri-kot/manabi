package com.anrikot.manabi.services;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.anrikot.manabi.domain.User;
import com.anrikot.manabi.domain.UserRole;
import com.anrikot.manabi.dto.AuthDTO;
import com.anrikot.manabi.dto.EmailRequestDTO;
import com.anrikot.manabi.dto.PasswordRequestDTO;
import com.anrikot.manabi.dto.RegisterDTO;
import com.anrikot.manabi.dto.UserDTO;
import com.anrikot.manabi.exceptions.BadRequestException;
import com.anrikot.manabi.exceptions.ConflictException;
import com.anrikot.manabi.exceptions.ResourceNotFoundException;
import com.anrikot.manabi.repository.UserRepository;

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
            .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return toDTO(user);
    }
    
    public UserDTO findById(Long id) {
        if (id == null) return null;
        User user = repository.findById(null)
            .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        
        return toDTO(user);
    }

    @Transactional
    public UserDTO updateEmail(EmailRequestDTO req, String username) {
        User u = repository.findByUsername(username)
            .orElseThrow(() -> new ResourceNotFoundException("User not found."));
        if (repository.existsByEmail(req.email())) throw new ConflictException("Email already registered.");

        u.setEmail(req.email());
        repository.save(u);
        return toDTO(u);
    }

    @Transactional
    public void updatePassword(PasswordRequestDTO req, String username) {
        User u = repository.findByUsername(username)
            .orElseThrow(() -> new ResourceNotFoundException("User not found."));
        if (!encoder.matches(req.oldPassword(), u.getPassword())) 
            throw new BadRequestException("Password is incorrect.");

        String newPassword = encoder.encode(req.oldPassword());
        u.setPassword(newPassword);
        repository.save(u);
    }

    @Transactional
    public void changeRole(Long userId, UserRole newRole) {
        User u = repository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User not found."));
        u.setRole(newRole);
        repository.save(u);
    }
    
    @Transactional
    public void save(RegisterDTO user) {
        if (user == null) throw new BadRequestException("User is null");
        if (repository.existsByEmail(user.email())) throw new ConflictException("Email already in use.");
        if (repository.existsByUsername(user.username())) throw new ConflictException("Username already in use.");

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
            .orElseThrow(() -> new ResourceNotFoundException("User does not exists."));
        
        if (!login.login().equals(username)) throw new BadRequestException("Login username and target username do not match.");
        if (!encoder.matches(login.password(), u.getPassword())) throw new BadRequestException("Password is incorrect.");
        
        repository.deleteByUsername(username);
    }

    @Transactional
    public void deleteById(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("User does not exist.");
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