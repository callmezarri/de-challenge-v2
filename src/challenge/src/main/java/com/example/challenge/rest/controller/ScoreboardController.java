package com.example.challenge.rest.controller;

import com.example.challenge.rest.dao.ScoreboardDAO;
import com.example.challenge.rest.dao.id.ScoreboardID;
import com.example.challenge.rest.repository.ScoreboardRepository;
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
@RequestMapping("/api/v1/scoreboard")
public class ScoreboardController {

    private final ScoreboardRepository scoreboardRepository;

    @Autowired
    public ScoreboardController(ScoreboardRepository scoreboardRepository) {
        this.scoreboardRepository = scoreboardRepository;
    }

    @GetMapping()
    public ResponseEntity<List<ScoreboardDAO>> getAll(){
        return new ResponseEntity<>(scoreboardRepository.findAll(),HttpStatus.OK);
    }

    @GetMapping("/{season}/{team}")
    public ResponseEntity<ScoreboardDAO> getScoreBoardById(@PathVariable String season, @PathVariable String team){
        Optional<ScoreboardDAO> scoreboardDAO = scoreboardRepository.findById(new ScoreboardID(season, team));
        return scoreboardDAO.map(dao -> new ResponseEntity<>(dao, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NO_CONTENT));
    }

    @GetMapping("/{season}")
    public ResponseEntity<List<ScoreboardDAO>> getScoreBoardBySeason(@PathVariable String season){
        List<ScoreboardDAO> scoreboardDAOList = scoreboardRepository.findAllBySeason(season);
        return scoreboardDAOList.isEmpty() ? new ResponseEntity<>(HttpStatus.NO_CONTENT) : new ResponseEntity<>(scoreboardDAOList, HttpStatus.OK);
    }

}
