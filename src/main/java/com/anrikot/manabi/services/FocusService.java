package com.anrikot.manabi.services;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.anrikot.manabi.domain.Focus;
import com.anrikot.manabi.domain.User;
import com.anrikot.manabi.dto.FocusDTO;
import com.anrikot.manabi.exceptions.ResourceNotFoundException;
import com.anrikot.manabi.repository.FocusRepository;
import com.anrikot.manabi.repository.UserRepository;

@Service
public class FocusService {
    private final FocusRepository repository;
    private final UserRepository userRepository;

    public FocusService(FocusRepository repository, UserRepository userRepository) {
        this.repository = repository;
        this.userRepository = userRepository;
    }

    public List<FocusDTO> findAllByUserId(Long userId) {
        return repository.findAllByUserId(userId).stream()
                .map(this::toDTO)
                .toList();
    }

    public FocusDTO findByIdAndUsername(long id, Long userId) {
        Focus f = repository.findByIdAndUserId(id, userId).orElseThrow(() -> new ResourceNotFoundException("Focus not found"));
        return toDTO(f);
    }

    @Transactional
    public FocusDTO save(FocusDTO dto, Long userId) {
        User user = userRepository.getReferenceById(userId);

        Focus parent = null;

        if (dto.parentId() != null) {
            parent = repository.findById(dto.parentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Parent not found"));
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
            .orElseThrow(() -> new ResourceNotFoundException("Focus not found"));
        
        f.setName(dto.name());
        Focus parent = null;
        if (dto.parentId() != null) {
            parent = repository.findByIdAndUserId(dto.parentId(), userId)
                .orElseThrow(() -> new ResourceNotFoundException("Parent not found"));
        }

        f.setParent(parent);
        repository.save(f);
    }

    @Transactional
    public void delete(Long id, Long userId) {
        if (!repository.existsByIdAndUserId(id, userId)) throw new ResourceNotFoundException("Focus not found");
        
        repository.deleteById(id);
    }

    private FocusDTO toDTO(Focus entity) {
        Long parentId = null;
        if (entity.getParent() != null) parentId = entity.getParent().getId();

        return new FocusDTO(entity.getId(), entity.getName(), parentId);
    }
}
