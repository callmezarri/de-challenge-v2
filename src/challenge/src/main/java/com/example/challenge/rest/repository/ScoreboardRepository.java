package com.example.challenge.rest.repository;

import com.example.challenge.rest.dao.ScoreboardDAO;
import com.example.challenge.rest.dao.id.ScoreboardID;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ScoreboardRepository extends JpaRepository<ScoreboardDAO, ScoreboardID> {

    List<ScoreboardDAO> findAllBySeason(String season);

}
