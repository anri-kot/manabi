package com.anrikot.manabi.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.anrikot.manabi.domain.FocusSession;

@Repository
public interface FocusSessionRepository extends JpaRepository<FocusSession, Long> {
    public List<FocusSession> findAllByUserId(Long userId);
    public Optional<FocusSession> findByIdAndUserId(Long id, Long userId);
    public List<FocusSession> findAllByFocusIdAndUserId(Long focusId, Long userId);
}
