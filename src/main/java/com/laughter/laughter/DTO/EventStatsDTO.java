package com.laughter.laughter.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EventStatsDTO {
    private long accomplished;
    private long ongoing;
    private long upcoming;

}
