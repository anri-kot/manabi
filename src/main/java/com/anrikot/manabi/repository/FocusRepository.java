package com.anrikot.manabi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.anrikot.manabi.domain.Focus;

public interface FocusRepository extends JpaRepository<Focus, Integer> {
    public List<Focus> findAllByNameContains(String name);
}