package com.anrikot.manabi.services;

import java.time.Instant;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.anrikot.manabi.domain.Focus;
import com.anrikot.manabi.domain.FocusSession;
import com.anrikot.manabi.dto.FocusSessionDTO;
import com.anrikot.manabi.exceptions.BadRequestException;
import com.anrikot.manabi.exceptions.ResourceNotFoundException;
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
            .orElseThrow(() -> new ResourceNotFoundException("Session not found"));
        return FocusSessionMapper.toDTO(f);
    }

    @Transactional
    public FocusSessionDTO save(Long focusId, Long userId) {
        if (repository.existsByUserIdAndEndIsNull(userId))
            throw new BadRequestException("Cannot start more than one session at once.");

        Focus f = focusRepository.findByIdAndUserId(focusId, userId)
            .orElseThrow(() -> new ResourceNotFoundException("Focus not found"));

        FocusSession session = new FocusSession();
        session.setFocus(f);
        session.setStart(Instant.now());
        session.setUser(f.getUser());
        
        return FocusSessionMapper.toDTO(repository.save(session));
    }

    @Transactional
    public void update(Long id, Instant end, Long userId) {
        FocusSession fs = repository.findByIdAndUserId(id, userId)
            .orElseThrow(() -> new ResourceNotFoundException("Session not found"));

        if (fs.getEnd() == null) throw new BadRequestException("Cannot edit the session while it's running");
        if (end.isAfter(Instant.now()) ||
            end.isBefore(fs.getStart())) {
                throw new BadRequestException("Invalid 'end' time.");
            }
        fs.setEnd(end);
    }

    @Transactional
    public void finish(Long id, Long userId) {
        FocusSession f = repository.findByIdAndUserId(id, userId)
            .orElseThrow(() -> new ResourceNotFoundException("Session not found"));
        f.setEnd(Instant.now());
    }
}
