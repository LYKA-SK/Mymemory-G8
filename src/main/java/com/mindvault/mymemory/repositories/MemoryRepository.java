
package com.mindvault.mymemory.repositories;

import com.mindvault.mymemory.entities.Memory;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface MemoryRepository extends JpaRepository<Memory, Long> {
    List<Memory> findAllByUserEmail(String email);

    Optional<Memory> findByIdAndUserEmail(Long id, String email);
}
