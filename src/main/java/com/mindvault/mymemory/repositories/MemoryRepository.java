//package com.mindvault.mymemory.repositories;
//
//import com.mindvault.mymemory.entities.Memory;
//import com.mindvault.mymemory.entities.User;
//import org.springframework.data.jpa.repository.JpaRepository;
//import java.util.List;
//import java.util.Optional;
//
//public interface MemoryRepository extends JpaRepository<Memory, Long> {
//    // Custom method to fetch all memories belonging to a specific user
//    List<Memory> findAllByUserEmail(String email);
//
//    // Custom method to find a memory by ID belonging to a specific user
//    Optional<Memory> findByIdAndUserEmail(Long id, String email);
//}

package com.mindvault.mymemory.repositories;

import com.mindvault.mymemory.entities.Memory;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface MemoryRepository extends JpaRepository<Memory, Long> {
    List<Memory> findAllByUserEmail(String email);

    Optional<Memory> findByIdAndUserEmail(Long id, String email);
}
