package com.laughter.laughter.Responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponse {
    private boolean status;
    private String message;
    private String token;
    private String userName;
    private Long userId;
    private Object data;

     public AuthResponse(boolean status,String message){
        this.status=status;
        this.message=message;
    }

     public AuthResponse(boolean status, String message, Object data) {
        this.status = status;
        this.message = message;
        this.data = data;
     }

     public AuthResponse(boolean status, String message, String token, String userName, Long userId) {
        this.status = status;
        this.message = message;
        this.token = token;
        this.userName = userName;
        this.userId = userId;
     }

     
    
}
