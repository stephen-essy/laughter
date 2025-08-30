package com.laughter.laughter.DTO;

import com.laughter.laughter.Entity.Gender;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProfileDTO {
    private Gender gender;
    private String phoneNumber;
    private String University;
    private String corse;
}
