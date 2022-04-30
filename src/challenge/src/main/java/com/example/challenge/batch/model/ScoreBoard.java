package com.example.challenge.batch.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ScoreBoard {
    private String team;
    private short score;
    private short matchesPlayed;
    private short goals;
    private short goalsAgainst;
    private short goalDifference;
    private short shotsOnTarget;
    private Double goalPercentage;
    private String season;
}