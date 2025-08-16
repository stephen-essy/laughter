package com.laughter.laughter.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private String name;
    private String email;
    private String password;
    private String recoveryString;

    public UserDTO(String email, String password) {
        this.email = email;
        this.password = password;
    } 
}
