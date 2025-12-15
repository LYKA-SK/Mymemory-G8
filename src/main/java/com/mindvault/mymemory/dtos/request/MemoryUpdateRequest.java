package com.mindvault.mymemory.dtos.request;

import lombok.Data;

@Data
public class MemoryUpdateRequest {
    private String title;
    private String content;
    private String imageUrl;
    private Long categoryId;
}