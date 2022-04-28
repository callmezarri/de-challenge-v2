package com.example.challenge.model.batch;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SeasonStatistics {

    private String team;
    private Double value;
    private String season;

}
