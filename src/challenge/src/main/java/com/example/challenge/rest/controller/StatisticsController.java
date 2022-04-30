package com.example.challenge.rest.controller;

import com.example.challenge.rest.dto.StatisticsDTO;
import com.example.challenge.rest.dao.BestScorerDAO;
import com.example.challenge.rest.repository.BestRatioRepository;
import com.example.challenge.rest.repository.BestScorerRepository;
import com.example.challenge.rest.repository.MostScoredRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/statistics")
public class StatisticsController {

    private final BestRatioRepository bestRatioRepository;
    private final BestScorerRepository bestScorerRepository;
    private final MostScoredRepository mostScoredRepository;

    @Autowired
    public StatisticsController(BestRatioRepository bestRatioRepository,
                                BestScorerRepository bestScorerRepository,
                                MostScoredRepository mostScoredRepository
    ) {
        this.bestRatioRepository = bestRatioRepository;
        this.bestScorerRepository = bestScorerRepository;
        this.mostScoredRepository = mostScoredRepository;
    }

    @GetMapping()
    public ResponseEntity<List<StatisticsDTO>> getAll(){
        List<String> seasons = bestScorerRepository.findAll().stream().map(BestScorerDAO::getSeason).collect(Collectors.toList());
        return new ResponseEntity<>(
                seasons.stream().map(this::getBySeason).collect(Collectors.toList()),
                HttpStatus.OK
        );
    }

    @GetMapping("/{season}")
    public ResponseEntity<StatisticsDTO> getStatisticBySeason(@PathVariable String season){
        return new ResponseEntity<>(getBySeason(season), HttpStatus.OK);
    }

    private StatisticsDTO getBySeason(String season){
        return new StatisticsDTO(
                season,
                mostScoredRepository.findAllBySeason(season),
                bestScorerRepository.findAllBySeason(season),
                bestRatioRepository.findAllBySeason(season)
        );
    }

}
