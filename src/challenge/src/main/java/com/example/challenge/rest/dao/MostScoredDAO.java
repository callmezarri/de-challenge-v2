package com.example.challenge.rest.dao;

import com.example.challenge.rest.dao.id.MostScoredID;
import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "most_scored")
@IdClass(MostScoredID.class)
@Data
public class MostScoredDAO {

    @Id
    @Column(name = "most_goals_against_team")
    private String mostScoredTeam;

    @Id
    @Column(name = "most_goals_against_number")
    private short mostScoredNumber;

    @Id
    @Column(name = "season")
    private String season;
}
