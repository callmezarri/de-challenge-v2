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
public class MostScoredID implements Serializable {

    String mostScoredTeam;
    short mostScoredNumber;
    String season;

}
