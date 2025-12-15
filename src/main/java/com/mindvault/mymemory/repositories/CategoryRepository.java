package com.mindvault.mymemory.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mindvault.mymemory.entities.Category;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    Optional<Category> findByName(String name);
    boolean existsByNameIgnoreCase(String name);
}