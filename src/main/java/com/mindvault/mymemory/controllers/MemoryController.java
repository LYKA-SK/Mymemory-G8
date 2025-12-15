package com.mindvault.mymemory.controllers;

import java.security.Principal; 
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mindvault.mymemory.dtos.request.MemoryCreateRequest;
import com.mindvault.mymemory.dtos.request.MemoryUpdateRequest;
import com.mindvault.mymemory.dtos.response.MemoryResponse;
import com.mindvault.mymemory.services.MemoryService;

@RestController
@RequestMapping("/api/memories")
public class MemoryController {

    private final MemoryService memoryService;
    
    public MemoryController(MemoryService memoryService) {
        this.memoryService = memoryService;
    }
    
    // C - CREATE
    // CORRECTED: Changed @PostMapping("/create") to @PostMapping to follow REST convention
    @PostMapping("")
    public ResponseEntity<MemoryResponse> createMemory(
        Principal principal, 
        @RequestBody MemoryCreateRequest request
    ) {
        MemoryResponse response = memoryService.createMemory(principal.getName(), request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // R - READ (All for the current user)
    @GetMapping
    public ResponseEntity<List<MemoryResponse>> getAllUserMemories(Principal principal) {
        List<MemoryResponse> memories = memoryService.getAllUserMemories(principal.getName());
        return ResponseEntity.ok(memories);
    }
    
    // R - READ (One by ID, scoped to the current user)
    @GetMapping("/{id}")
    public ResponseEntity<MemoryResponse> getMemoryById(Principal principal, @PathVariable Long id) {
        MemoryResponse response = memoryService.getMemoryById(principal.getName(), id);
        return ResponseEntity.ok(response);
    }

    // U - UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<MemoryResponse> updateMemory(
        Principal principal, 
        @PathVariable Long id, 
        @RequestBody MemoryUpdateRequest request
    ) {
        MemoryResponse response = memoryService.updateMemory(principal.getName(), id, request);
        return ResponseEntity.ok(response);
    }

    // D - DELETE (Soft Delete)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMemory(Principal principal, @PathVariable Long id) {
        memoryService.deleteMemory(principal.getName(), id);
        return ResponseEntity.noContent().build();
    }
}