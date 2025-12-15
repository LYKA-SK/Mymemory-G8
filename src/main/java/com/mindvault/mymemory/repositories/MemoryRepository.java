package com.mindvault.mymemory.repositories;

import com.mindvault.mymemory.entities.Memory;
import com.mindvault.mymemory.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface MemoryRepository extends JpaRepository<Memory, Long> {
    
    // Find all non-deleted memories for a specific user
    List<Memory> findByUserAndDeletedFalse(User user);

    // Find a specific non-deleted memory by ID and its owner
    Optional<Memory> findByIdAndUserAndDeletedFalse(Long id, User user);
    
    // Check existence
    boolean existsByIdAndUserAndDeletedFalse(Long id, User user);
}
