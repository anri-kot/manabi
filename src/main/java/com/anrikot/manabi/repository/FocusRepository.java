package com.anrikot.manabi.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.anrikot.manabi.domain.Focus;

public interface FocusRepository extends JpaRepository<Focus, Long> {
    public Optional<Focus> findByIdAndUserId(Long id, Long userId);
    public List<Focus> findAllByNameContainsAndUserId(String name, Long userId);
    public List<Focus> findAllByUserId(Long userId);

    public boolean existsByIdAndUserId(Long id, Long userId);
}