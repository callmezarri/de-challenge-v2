package com.example.challenge.rest.dao;

import com.example.challenge.rest.dao.id.BestRatioID;
import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "best_ratio")
@IdClass(BestRatioID.class)
@Data
public class BestRatioDAO {

    @Id
    @Column(name = "best_goals_ratio_team")
    private String bestRatioTeam;

    @Id
    @Column(name = "best_goals_ratio_number")
    private Double bestRatioNumber;

    @Id
    @Column(name = "season")
    private String season;

}
