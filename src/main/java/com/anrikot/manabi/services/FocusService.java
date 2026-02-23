package com.anrikot.manabi.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.anrikot.manabi.domain.Focus;
import com.anrikot.manabi.domain.User;
import com.anrikot.manabi.dto.FocusDTO;
import com.anrikot.manabi.mappers.FocusMapper;
import com.anrikot.manabi.repository.FocusRepository;

@Service
public class FocusService {
    private final FocusRepository repository;
    private final UserService uService;

    public FocusService(FocusRepository repository, UserService uService) {
        this.repository = repository;
        this.uService = uService;
    }

    public List<FocusDTO> findAll() {
        return repository.findAll().stream()
                .map(FocusMapper::toDTO)
                .toList();
    }

    public FocusDTO findById(int id) {
        Focus f = repository.findById(id).orElseThrow(() -> new RuntimeException("Focus not found"));
        return FocusMapper.toDTO(f);
    }

    public FocusDTO save(FocusDTO dto, String username) {
        User user = uService.loadUserByUsername(username);

        Focus parent = null;

        if (dto.parentId() != null) {
            parent = repository.findById(dto.parentId())
                    .orElseThrow(() -> new RuntimeException("Parent not found"));
        }

        Focus focus = new Focus();
        focus.setName(dto.name());
        focus.setUser(user);
        focus.setParent(parent);

        Focus saved = repository.save(focus);

        return FocusMapper.toDTO(saved);
    }
}
