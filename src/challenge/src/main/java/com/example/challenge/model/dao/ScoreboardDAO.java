package com.example.challenge.model.dao;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "scoreboard")
@IdClass(ScoreboardId.class)
@Data
public class ScoreboardDAO {

    @Id
    @Column(name = "season")
    private String season;

    @Id
    @Column(name = "team")
    private String team;

    @Column(name = "score")
    private short score;

    @Column(name = "matches_played")
    private short matchesPlayed;

    @Column(name = "goals_favor")
    private short goals;

    @Column(name = "goals_against")
    private short goalsAgainst;

    @Column(name = "goal_difference")
    private short goalDifference;

    @Column(name = "shots_on_target")
    private short shotsOnTarget;

    @Column(name = "goal_percentage")
    private Double goalPercentage;

}
