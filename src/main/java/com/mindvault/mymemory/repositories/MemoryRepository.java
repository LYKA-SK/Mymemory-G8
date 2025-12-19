//
//package com.mindvault.mymemory.repositories;
//
//import com.mindvault.mymemory.entities.Memory;
//import org.springframework.data.jpa.repository.JpaRepository;
//import java.util.List;
//import java.util.Optional;
//
//public interface MemoryRepository extends JpaRepository<Memory, Long> {
//    List<Memory> findAllByUserEmail(String email);
//
//    Optional<Memory> findByIdAndUserEmail(Long id, String email);
//}

package com.mindvault.mymemory.repositories;

import com.mindvault.mymemory.entities.Memory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

public interface MemoryRepository extends JpaRepository<Memory, Long> {
    List<Memory> findAllByUserEmail(String email);

    Optional<Memory> findByIdAndUserEmail(Long id, String email);

    // Fuzzy Search by Title or Category Name for a specific user
    @Query("SELECT m FROM Memory m WHERE m.user.email = :email AND (" +
           "LOWER(m.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(m.category.name) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    List<Memory> searchByTitleOrCategory(@Param("email") String email, @Param("keyword") String keyword);
}
