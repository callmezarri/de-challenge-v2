package com.example.challenge.rest.dto;

import com.example.challenge.rest.dao.BestRatioDAO;
import com.example.challenge.rest.dao.BestScorerDAO;
import com.example.challenge.rest.dao.MostScoredDAO;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class StatisticsDTO {

    private String season;
    private List<MostScoredDAO> mostScoredTeams;
    private List<BestScorerDAO> bestScorerTeams;
    private List<BestRatioDAO> bestRatioTeams;

}
