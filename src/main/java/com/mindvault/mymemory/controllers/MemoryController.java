package com.mindvault.mymemory.controllers;

import com.mindvault.mymemory.dtos.request.MemoryCreateRequest;


import com.mindvault.mymemory.dtos.request.MemoryUpdateRequest;
import com.mindvault.mymemory.dtos.response.MemoryResponse;
import com.mindvault.mymemory.services.MemoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/memories")
public class MemoryController {

    private final MemoryService memoryService;

    public MemoryController(MemoryService memoryService) {
        this.memoryService = memoryService;
    }

  
    private String extractUserEmail(UserDetails userDetails) {
        return userDetails.getUsername(); 
    }

    // CREATE: POST /api/memories
    @PostMapping
    public ResponseEntity<MemoryResponse> createMemory(@RequestBody MemoryCreateRequest createRequest,
                                                       @AuthenticationPrincipal UserDetails userDetails) {
        String userEmail = extractUserEmail(userDetails);
        MemoryResponse createdMemory = memoryService.createMemory(createRequest, userEmail);
        return new ResponseEntity<>(createdMemory, HttpStatus.CREATED);
    }

    // READ ALL: GET /api/memories
    @GetMapping
    public ResponseEntity<List<MemoryResponse>> getAllMemories(@AuthenticationPrincipal UserDetails userDetails) {
        String userEmail = extractUserEmail(userDetails);
        List<MemoryResponse> memories = memoryService.getAllMemories(userEmail);
        return ResponseEntity.ok(memories);
    }

    // READ BY ID: GET /api/memories/{id}
    @GetMapping("/{id}")
    public ResponseEntity<MemoryResponse> getMemoryById(@PathVariable Long id,
                                                        @AuthenticationPrincipal UserDetails userDetails) {
        String userEmail = extractUserEmail(userDetails);
        MemoryResponse memory = memoryService.getMemoryById(id, userEmail);
        return ResponseEntity.ok(memory);
    }

    // UPDATE: PUT /api/memories/{id}
    @PutMapping("/{id}")
    public ResponseEntity<MemoryResponse> updateMemory(@PathVariable Long id,
                                                       @RequestBody MemoryUpdateRequest updateRequest,
                                                       @AuthenticationPrincipal UserDetails userDetails) {
        String userEmail = extractUserEmail(userDetails);
        MemoryResponse updatedMemory = memoryService.updateMemory(id, updateRequest, userEmail);
        return ResponseEntity.ok(updatedMemory);
    }

    // DELETE: DELETE /api/memories/{id}
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT) 
    public void deleteMemory(@PathVariable Long id,
                               @AuthenticationPrincipal UserDetails userDetails) {
        String userEmail = extractUserEmail(userDetails);
        memoryService.deleteMemory(id, userEmail);
    }
    @GetMapping("/search")
    public ResponseEntity<List<MemoryResponse>> search(
            @RequestParam("query") String query, // Match the name in the URL
            @AuthenticationPrincipal UserDetails userDetails) {
        
        // Use the same helper method you used in other endpoints
        String userEmail = extractUserEmail(userDetails);
        
        List<MemoryResponse> results = memoryService.searchMemories(query, userEmail);
        return ResponseEntity.ok(results);
    }
    
}