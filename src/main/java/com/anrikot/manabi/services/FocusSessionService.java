package com.anrikot.manabi.services;

import java.time.Instant;
import java.util.List;

import org.springframework.stereotype.Service;

import com.anrikot.manabi.domain.Focus;
import com.anrikot.manabi.domain.FocusSession;
import com.anrikot.manabi.dto.FocusSessionDTO;
import com.anrikot.manabi.mappers.FocusSessionMapper;
import com.anrikot.manabi.repository.FocusRepository;
import com.anrikot.manabi.repository.FocusSessionRepository;

@Service
public class FocusSessionService {
    private final FocusSessionRepository repository;
    private final FocusRepository focusRepository;

    public FocusSessionService(FocusSessionRepository repository, FocusRepository focusRepository) {
        this.repository = repository;
        this.focusRepository = focusRepository;
    }

    public List<FocusSessionDTO> findAll(Long userId) {
        return repository.findAllByUserId(userId).stream()
            .map(FocusSessionMapper::toDTO)
            .toList();
    }

    public FocusSessionDTO findById(Long id, Long userId) {
        FocusSession f = repository.findByIdAndUserId(id, userId)
            .orElseThrow(() -> new RuntimeException("Session not found"));
        return FocusSessionMapper.toDTO(f);
    }

    public FocusSessionDTO save(Long focusId, Long userId) {
        Focus f = focusRepository.findByIdAndUserId(focusId, userId)
            .orElseThrow(() -> new RuntimeException("Focus not found"));

        FocusSession session = new FocusSession();
        session.setFocus(f);
        session.setStart(Instant.now());
        session.setUser(f.getUser());
        
        return FocusSessionMapper.toDTO(repository.save(session));
    }

    public void finish(Long id, Long userId) {
        FocusSession f = repository.findByIdAndUserId(id, userId)
            .orElseThrow(() -> new RuntimeException("Session not found"));
        f.setEnd(Instant.now());
        repository.save(f);
    }
}
