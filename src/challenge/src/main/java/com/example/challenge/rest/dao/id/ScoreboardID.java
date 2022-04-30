package com.example.challenge.rest.dao.id;


import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
public class ScoreboardID implements Serializable {
    String season;
    String team;

    public ScoreboardID(String season, String team){
        this.season = season;
        this.team = team;
    }
}
