package com.mindvault.mymemory.dtos.response;

import lombok.Data;
import java.time.LocalDateTime;

@Data 
public class MemoryResponse {
    private Long id; 
    private String title;
    private String content;
    private String imageUrl;
    
    private Long categoryId;
    private String categoryName; 
    
    private LocalDateTime createdAt; 
    private LocalDateTime updatedAt; }