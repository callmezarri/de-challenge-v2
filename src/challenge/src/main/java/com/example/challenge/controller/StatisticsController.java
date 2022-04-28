package com.example.challenge.controller;

import com.example.challenge.model.dao.StatisticsDAO;
import com.example.challenge.repository.StatisticsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/statistics")
public class StatisticsController {

    private final StatisticsRepository statisticsRepository;

    @Autowired
    public StatisticsController(StatisticsRepository statisticsRepository) {
        this.statisticsRepository = statisticsRepository;
    }

    @GetMapping()
    public ResponseEntity<List<StatisticsDAO>> getAll(){
        return new ResponseEntity<>(statisticsRepository.findAll(), HttpStatus.OK);
    }

    @GetMapping("/{season}")
    public ResponseEntity<StatisticsDAO> getStatisticBySeason(@PathVariable String season){
        Optional<StatisticsDAO> statisticsDAOResult = statisticsRepository.findById(season);
        return statisticsDAOResult.map(statisticsDAO -> new ResponseEntity<>(statisticsDAO, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NO_CONTENT));
    }

}
