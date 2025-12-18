

package com.mindvault.mymemory.services;

import com.mindvault.mymemory.dtos.request.MemoryCreateRequest;

import com.mindvault.mymemory.dtos.request.MemoryUpdateRequest;
import com.mindvault.mymemory.dtos.response.MemoryResponse;
import java.util.List;

public interface MemoryService {

    // CREATE
    MemoryResponse createMemory(MemoryCreateRequest createRequest, String userEmail);

    // READ
    List<MemoryResponse> getAllMemories(String userEmail);
    MemoryResponse getMemoryById(Long id, String userEmail);

    // UPDATE
    MemoryResponse updateMemory(Long id, MemoryUpdateRequest updateRequest, String userEmail);

    // DELETE
    void deleteMemory(Long id, String userEmail);
}
