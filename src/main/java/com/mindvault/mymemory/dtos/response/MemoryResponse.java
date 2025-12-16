package com.mindvault.mymemory.dtos.response;

import lombok.Data;
import lombok.NoArgsConstructor; 
import java.time.LocalDateTime;

@Data
@NoArgsConstructor // Adds the default no-argument constructor
public class MemoryResponse {
    
    private Long id;
    private String title;
    private String content;
    private String imageUrl;
    
    private Long categoryId;
    private String categoryName;
    
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public MemoryResponse(Long id, String title, String content) {
        this.id = id;
        this.title = title;
        this.content = content;
        
    }
}