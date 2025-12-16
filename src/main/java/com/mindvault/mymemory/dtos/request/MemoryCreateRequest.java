package com.mindvault.mymemory.dtos.request;

public class MemoryCreateRequest {

    private String title;
    private String content;
    private Long categoryId; 

    public MemoryCreateRequest() {}

    public MemoryCreateRequest(String title, String content) {
        this.title = title;
        this.content = content;
    }
    
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    
    public Long getCategoryId() {
        return categoryId; 
    }

    public void setCategoryId(Long categoryId) { 
        this.categoryId = categoryId;
    }
}