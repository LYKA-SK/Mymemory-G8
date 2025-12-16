package com.mindvault.mymemory.dtos.request;

public class MemoryUpdateRequest {

    private String title;
    private String content;

    // Default Constructor (Required by JSON mapping)
    public MemoryUpdateRequest() {}

    // Constructor with fields (Optional, but useful)
    public MemoryUpdateRequest(String title, String content) {
        this.title = title;
        this.content = content;
    }

    // Getters and Setters (REQUIRED for service methods to access fields)
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}