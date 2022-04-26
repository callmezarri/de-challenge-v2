package com.example.challenge.model.dao;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
public class ScoreboardId implements Serializable {
    String season;
    String team;

    public ScoreboardId(String season, String team){
        this.season = season;
        this.team = team;
    }
}
