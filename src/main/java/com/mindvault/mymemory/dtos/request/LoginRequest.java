package com.mindvault.mymemory.dtos.request;

import jakarta.persistence.Column;
import lombok.Data;

@Data
public class LoginRequest {
	
	@Column
    private String email;
	
	@Column
    private String password;
}