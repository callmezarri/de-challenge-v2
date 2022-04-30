package com.example.challenge.batch.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SeasonStatistics {

    private String team;
    private Double value;
    private String season;

}
