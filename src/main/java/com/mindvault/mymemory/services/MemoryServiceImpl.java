package com.mindvault.mymemory.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mindvault.mymemory.dtos.request.MemoryCreateRequest;
import com.mindvault.mymemory.dtos.request.MemoryUpdateRequest;
import com.mindvault.mymemory.dtos.response.MemoryResponse;
import com.mindvault.mymemory.entities.Category;
import com.mindvault.mymemory.entities.Memory;
import com.mindvault.mymemory.entities.User;
import com.mindvault.mymemory.repositories.CategoryRepository;
import com.mindvault.mymemory.repositories.MemoryRepository;
import com.mindvault.mymemory.repositories.UserRepository;

@Service
@Transactional
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

    private String getAuthenticatedUsername() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || auth.getPrincipal().equals("anonymousUser")) {
            throw new RuntimeException("Authenticated user not found.");
        }
        return auth.getName(); 
    }

    private User getAuthenticatedUser() {
        String username = getAuthenticatedUsername();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Authenticated user not found in database."));
    }

    /**
     * Helper method to get a User object by username, throwing an exception if not found.
     */
    private User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found with username: " + username));
    }


    // --- Map Memory entity to Response DTO ---
    private MemoryResponse mapToResponse(Memory memory) {
        MemoryResponse response = new MemoryResponse();
        response.setId(memory.getId());
        response.setTitle(memory.getTitle());
        response.setContent(memory.getContent());
        response.setImageUrl(memory.getImageUrl());
        response.setCreatedAt(memory.getCreatedAt());
        response.setUpdatedAt(memory.getUpdatedAt());

        if (memory.getCategory() != null) {
            response.setCategoryId(memory.getCategory().getId());
            response.setCategoryName(memory.getCategory().getName());
        }
        return response;
    }

    // --- CREATE Memory (using Security Context) ---
    @Override
    public MemoryResponse createMemory(MemoryCreateRequest request) {
        User user = getAuthenticatedUser();
        return saveNewMemory(user, request);
    }
    
    // --- CREATE Memory (Explicit User) ---
    @Override
    public MemoryResponse createMemory(String username, MemoryCreateRequest request) {
        User user = getUserByUsername(username);
        return saveNewMemory(user, request);
    }

    // Helper method to save memory logic (avoiding duplication)
    private MemoryResponse saveNewMemory(User user, MemoryCreateRequest request) {
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found with ID: " + request.getCategoryId()));

        Memory memory = new Memory();
        memory.setUser(user);
        memory.setCategory(category);
        memory.setTitle(request.getTitle());
        memory.setContent(request.getContent());
        memory.setImageUrl(request.getImageUrl());

        Memory savedMemory = memoryRepository.save(memory);
        return mapToResponse(savedMemory);
    }

    // --- READ One Memory (using Security Context) ---
    @Override
    public MemoryResponse getMemoryById(Long memoryId) {
        User user = getAuthenticatedUser();
        return getMemoryByUserIdAndMemoryId(user, memoryId);
    }
    
    // --- READ One Memory (Explicit User) ---
    @Override
    public MemoryResponse getMemoryById(String username, Long memoryId) {
        User user = getUserByUsername(username);
        return getMemoryByUserIdAndMemoryId(user, memoryId);
    }

    private MemoryResponse getMemoryByUserIdAndMemoryId(User user, Long memoryId) {
        Memory memory = memoryRepository.findByIdAndUserAndDeletedFalse(memoryId, user)
                .orElseThrow(() -> new RuntimeException("Memory not found or access denied."));
        return mapToResponse(memory);
    }

    // --- READ All Memories (using Security Context) ---
    @Override
    public List<MemoryResponse> getAllUserMemories() {
        User user = getAuthenticatedUser();
        return fetchAllUserMemories(user);
    }

    // --- READ All Memories (Explicit User) ---
    @Override
    public List<MemoryResponse> getAllUserMemories(String username) {
        User user = getUserByUsername(username);
        return fetchAllUserMemories(user);
    }
    
    private List<MemoryResponse> fetchAllUserMemories(User user) {
        return memoryRepository.findByUserAndDeletedFalse(user)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // --- UPDATE Memory (using Security Context) ---
    @Override
    public MemoryResponse updateMemory(Long memoryId, MemoryUpdateRequest request) {
        User user = getAuthenticatedUser();
        return updateExistingMemory(user, memoryId, request);
    }

    // --- UPDATE Memory (Explicit User) ---
    @Override
    public MemoryResponse updateMemory(String username, Long memoryId, MemoryUpdateRequest request) {
        User user = getUserByUsername(username);
        return updateExistingMemory(user, memoryId, request);
    }

    private MemoryResponse updateExistingMemory(User user, Long memoryId, MemoryUpdateRequest request) {
        Memory memory = memoryRepository.findByIdAndUserAndDeletedFalse(memoryId, user)
                .orElseThrow(() -> new RuntimeException("Memory not found or access denied."));

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found with ID: " + request.getCategoryId()));

        memory.setTitle(request.getTitle());
        memory.setContent(request.getContent());
        memory.setImageUrl(request.getImageUrl());
        memory.setCategory(category);

        Memory updatedMemory = memoryRepository.save(memory);
        return mapToResponse(updatedMemory);
    }

    // --- DELETE Memory (Soft Delete - using Security Context) ---
    @Override
    public void deleteMemory(Long memoryId) {
        User user = getAuthenticatedUser();
        softDeleteMemory(user, memoryId);
    }

    // --- DELETE Memory (Soft Delete - Explicit User) ---
    @Override
    public void deleteMemory(String username, Long memoryId) {
        User user = getUserByUsername(username);
        softDeleteMemory(user, memoryId);
    }

    private void softDeleteMemory(User user, Long memoryId) {
        Memory memory = memoryRepository.findByIdAndUserAndDeletedFalse(memoryId, user)
                .orElseThrow(() -> new RuntimeException("Memory not found or access denied."));
        memory.setDeleted(true);
        memoryRepository.save(memory);
    }
}