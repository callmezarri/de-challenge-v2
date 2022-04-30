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
public class BestRatioID implements Serializable {

    String bestRatioTeam;
    Double bestRatioNumber;
    String season;

}
