package com.anrikot.manabi.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.anrikot.manabi.domain.Focus;
import com.anrikot.manabi.domain.User;
import com.anrikot.manabi.dto.FocusDTO;
import com.anrikot.manabi.repository.FocusRepository;

import jakarta.transaction.Transactional;

@Service
public class FocusService {
    private final FocusRepository repository;

    public FocusService(FocusRepository repository) {
        this.repository = repository;
    }

    public List<FocusDTO> findAllByUserId(Long userId) {
        return repository.findAllByUserId(userId).stream()
                .map(this::toDTO)
                .toList();
    }

    public FocusDTO findByIdAndUsername(long id, Long userId) {
        Focus f = repository.findByIdAndUserId(id, userId).orElseThrow(() -> new RuntimeException("Focus not found"));
        return toDTO(f);
    }

    @Transactional
    public FocusDTO save(FocusDTO dto, Long userId) {
        User user = new User();
        user.setId(userId);

        Focus parent = null;

        if (dto.parentId() != null) {
            parent = repository.findById(dto.parentId())
                    .orElseThrow(() -> new RuntimeException("Parent not found"));
        }

        Focus focus = new Focus();
        focus.setName(dto.name());
        focus.setUser(user);
        focus.setParent(parent);

        return toDTO(repository.save(focus));
    }

    @Transactional
    public void update(long id, FocusDTO dto, Long userId) {
        Focus f = repository.findByIdAndUserId(id, userId)
            .orElseThrow(() -> new RuntimeException("The Focus of ID '%d' from the user '%s' does not exist".formatted(id, userId)));
        
        f.setName(dto.name());
        Focus parent = null;
        if (dto.parentId() != null) {
            parent = repository.findByIdAndUserId(dto.parentId(), userId)
                .orElseThrow(() -> new RuntimeException("Parent not found"));
        }

        f.setParent(parent);
        repository.save(f);
    }

    @Transactional
    public void delete(Long id, Long userId) {
        if (!repository.existsByIdAndUserId(id, userId)) throw new RuntimeException("The Focus of ID '%d' does not exist".formatted(id));
        
        repository.deleteById(id);
    }

    private FocusDTO toDTO(Focus entity) {
        Long parentId = null;
        if (entity.getParent() != null) parentId = entity.getParent().getId();

        return new FocusDTO(entity.getId(), entity.getName(), parentId);
    }
}
