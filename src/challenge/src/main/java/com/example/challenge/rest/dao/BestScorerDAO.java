package com.example.challenge.rest.dao;

import com.example.challenge.rest.dao.id.BestScorerID;
import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "best_scorer")
@IdClass(BestScorerID.class)
@Data
public class BestScorerDAO {

    @Id
    @Column(name = "most_goals_team")
    private String bestScorerTeam;

    @Id
    @Column(name = "most_goals_number")
    private short bestScorerNumber;

    @Id
    @Column(name = "season")
    private String season;
}
