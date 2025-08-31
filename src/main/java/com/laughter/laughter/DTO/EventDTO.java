package com.laughter.laughter.DTO;

import java.time.LocalTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class EventDTO {
    private String name;
    private LocalTime startTime;
    private LocalTime endTime;
    private String location;
}
