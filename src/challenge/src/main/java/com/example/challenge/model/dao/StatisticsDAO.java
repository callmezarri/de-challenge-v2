package com.example.challenge.model.dao;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "season_statistics")
@Data
public class StatisticsDAO {

    @Id
    @Column(name = "season")
    private String season;

    @Column(name = "most_goals_team")
    private String mostGoalsTeam;

    @Column(name = "most_goals_number")
    private short mostGoalsNumber;

    @Column(name = "most_goals_against_team")
    private String mostGoalsAgainstTeam;

    @Column(name = "most_goals_against_number")
    private short mostGoalsAgainstNumber;

    @Column(name = "best_goals_ratio_team")
    private String bestGoalsRatioTeam;

    @Column(name = "best_goals_ratio_number")
    private Double bestGoalsRatioNumber;

}
