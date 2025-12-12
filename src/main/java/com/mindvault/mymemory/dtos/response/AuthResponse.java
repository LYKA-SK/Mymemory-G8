package com.mindvault.mymemory.dtos.response;

import com.mindvault.mymemory.entities.User;

import lombok.Data;

@Data
public class AuthResponse {
   private int status;
   private String token;
   private String message;
   private User user;

    public AuthResponse(int status,String token,String message,User user ) {
    		this.message = message;
    		this.status = status;
    		this.token = token;
    		this.user = user;
    }

 
}