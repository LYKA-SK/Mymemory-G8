//package com.mindvault.mymemory.services;
//
//import java.util.List;
//
//import com.mindvault.mymemory.dtos.request.MemoryCreateRequest;
//import com.mindvault.mymemory.dtos.request.MemoryUpdateRequest;
//import com.mindvault.mymemory.dtos.response.MemoryResponse;
//
//public interface MemoryService {
//    
//    MemoryResponse createMemory(String username, MemoryCreateRequest request);
//    MemoryResponse getMemoryById(String username, Long memoryId);
//    List<MemoryResponse> getAllUserMemories(String username);
//    MemoryResponse updateMemory(String username, Long memoryId, MemoryUpdateRequest request);
//    void deleteMemory(String username, Long memoryId);
//	MemoryResponse createMemory(MemoryCreateRequest request);
//	MemoryResponse getMemoryById(Long memoryId);
//	List<MemoryResponse> getAllUserMemories();
//	MemoryResponse updateMemory(Long memoryId, MemoryUpdateRequest request);
//	void deleteMemory(Long memoryId); 
//}

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
