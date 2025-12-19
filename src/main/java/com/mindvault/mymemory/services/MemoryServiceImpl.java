//package com.mindvault.mymemory.services;
//
//import com.mindvault.mymemory.dtos.request.MemoryCreateRequest;
//import com.mindvault.mymemory.dtos.request.MemoryUpdateRequest;
//import com.mindvault.mymemory.dtos.response.MemoryResponse;
//import com.mindvault.mymemory.entities.Category; // <-- NEW IMPORT
//import com.mindvault.mymemory.entities.Memory;
//import com.mindvault.mymemory.entities.User;
//import com.mindvault.mymemory.repositories.CategoryRepository; // <-- NEW IMPORT
//import com.mindvault.mymemory.repositories.MemoryRepository;
//import com.mindvault.mymemory.repositories.UserRepository;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//import java.util.List;
//import java.util.stream.Collectors;
//
//@Service
//public class MemoryServiceImpl implements MemoryService {
//
//    private final MemoryRepository memoryRepository;
//    private final UserRepository userRepository;
//    private final CategoryRepository categoryRepository; // <-- NEW FIELD
//
//    
//    public MemoryServiceImpl(MemoryRepository memoryRepository, 
//                             UserRepository userRepository,
//                             CategoryRepository categoryRepository) { // <-- ADD CategoryRepository
//        this.memoryRepository = memoryRepository;
//        this.userRepository = userRepository;
//        this.categoryRepository = categoryRepository; // <-- ASSIGN FIELD
//    }
//
//    private User getUserByEmail(String email) {
//        return userRepository.findByEmail(email)
//                .orElseThrow(() -> new RuntimeException("Authenticated user not found: " + email));
//    }
//    
//    private MemoryResponse mapToResponse(Memory memory) {
//        return new MemoryResponse(
//            memory.getId(), 
//            memory.getTitle(), 
//            memory.getContent() 
//        );
//    }
//
//    @Transactional
//    @Override
//    public MemoryResponse createMemory(MemoryCreateRequest createRequest, String userEmail) {
//        User user = getUserByEmail(userEmail);
//        
//        Category category = categoryRepository.findById(createRequest.getCategoryId())
//                .orElseThrow(() -> new RuntimeException("Category not found with ID: " + createRequest.getCategoryId()));
//        
//        Memory newMemory = new Memory();
//        newMemory.setTitle(createRequest.getTitle());
//        newMemory.setContent(createRequest.getContent());
//        
//        // Set the foreign key references
//        newMemory.setUser(user);
//        newMemory.setCategory(category); // <-- CRITICAL FIX: Setting the non-null Category
//        // ... set other fields from the DTO (e.g., ImageUrl)
//
//        // 2. Save Entity
//        Memory savedMemory = memoryRepository.save(newMemory);
//        
//        // 3. Convert saved Entity back to Response DTO
//        return mapToResponse(savedMemory);
//    }
//
//    // READ All (R) - No changes needed
//    @Override
//    public List<MemoryResponse> getAllMemories(String userEmail) {
//        List<Memory> memories = memoryRepository.findAllByUserEmail(userEmail); 
//        return memories.stream()
//                .map(this::mapToResponse)
//                .collect(Collectors.toList());
//    }
//
//    // READ by ID (R) - No changes needed
//    @Override
//    public MemoryResponse getMemoryById(Long id, String userEmail) {
//        Memory memory = memoryRepository.findByIdAndUserEmail(id, userEmail)
//                .orElseThrow(() -> new RuntimeException("Memory not found or not owned by user with ID: " + id));
//        return mapToResponse(memory);
//    }
//
//    @Transactional
//    @Override
//    public MemoryResponse updateMemory(Long id, MemoryUpdateRequest updateRequest, String userEmail) {
//        Memory existingMemory = memoryRepository.findByIdAndUserEmail(id, userEmail)
//                .orElseThrow(() -> new RuntimeException("Memory not found or not owned by user with ID: " + id));
//        
//        existingMemory.setTitle(updateRequest.getTitle());
//        existingMemory.setContent(updateRequest.getContent());
//
//        Memory savedMemory = memoryRepository.save(existingMemory);
//        return mapToResponse(savedMemory);
//    }
//
//    // DELETE (D) - No changes needed
//    @Transactional
//    @Override
//    public void deleteMemory(Long id, String userEmail) {
//        Memory memory = memoryRepository.findByIdAndUserEmail(id, userEmail)
//                .orElseThrow(() -> new RuntimeException("Memory not found or not owned by user with ID: " + id));
//        
//        memoryRepository.delete(memory);
//    }
//}

package com.mindvault.mymemory.services;

import com.mindvault.mymemory.dtos.request.MemoryCreateRequest;
import com.mindvault.mymemory.dtos.request.MemoryUpdateRequest;
import com.mindvault.mymemory.dtos.response.MemoryResponse;
import com.mindvault.mymemory.entities.Category;
import com.mindvault.mymemory.entities.Memory;
import com.mindvault.mymemory.entities.User;
import com.mindvault.mymemory.exceptions.ResourceNotFoundException; // <-- IMPORT YOUR NEW EXCEPTION
import com.mindvault.mymemory.repositories.CategoryRepository;
import com.mindvault.mymemory.repositories.MemoryRepository;
import com.mindvault.mymemory.repositories.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MemoryServiceImpl implements MemoryService {

    private final MemoryRepository memoryRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;

    public MemoryServiceImpl(MemoryRepository memoryRepository, 
                             UserRepository userRepository,
                             CategoryRepository categoryRepository) {
        this.memoryRepository = memoryRepository;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
    }

    private User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Authenticated user not found: " + email));
    }
    
    private MemoryResponse mapToResponse(Memory memory) {
        return new MemoryResponse(
            memory.getId(), 
            memory.getTitle(), 
            memory.getContent() 
        );
    }

    @Transactional
    @Override
    public MemoryResponse createMemory(MemoryCreateRequest createRequest, String userEmail) {
        User user = getUserByEmail(userEmail);
        
        Category category = categoryRepository.findById(createRequest.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with ID: " + createRequest.getCategoryId()));
        
        Memory newMemory = new Memory();
        newMemory.setTitle(createRequest.getTitle());
        newMemory.setContent(createRequest.getContent());
        newMemory.setUser(user);
        newMemory.setCategory(category);

        Memory savedMemory = memoryRepository.save(newMemory);
        return mapToResponse(savedMemory);
    }

    @Override
    public List<MemoryResponse> getAllMemories(String userEmail) {
        return memoryRepository.findAllByUserEmail(userEmail).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public MemoryResponse getMemoryById(Long id, String userEmail) {
        Memory memory = memoryRepository.findByIdAndUserEmail(id, userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Memory not found or not owned by user with ID: " + id));
        return mapToResponse(memory);
    }

    @Transactional
    @Override
    public MemoryResponse updateMemory(Long id, MemoryUpdateRequest updateRequest, String userEmail) {
        Memory existingMemory = memoryRepository.findByIdAndUserEmail(id, userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Memory not found with ID: " + id));
        
        existingMemory.setTitle(updateRequest.getTitle());
        existingMemory.setContent(updateRequest.getContent());

        Memory savedMemory = memoryRepository.save(existingMemory);
        return mapToResponse(savedMemory);
    }

    @Transactional
    @Override
    public void deleteMemory(Long id, String userEmail) {
        Memory memory = memoryRepository.findByIdAndUserEmail(id, userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Memory not found with ID: " + id));
        
        memoryRepository.delete(memory);
    }

    @Override
    public List<MemoryResponse> searchMemories(String keyword, String userEmail) {
        // This uses the fuzzy search method from your MemoryRepository
        List<Memory> memories = memoryRepository.searchByTitleOrCategory(userEmail, keyword);
        
        return memories.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }
}



