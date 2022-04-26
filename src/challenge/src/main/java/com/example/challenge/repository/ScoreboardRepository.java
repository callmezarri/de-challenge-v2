package com.example.challenge.repository;

import com.example.challenge.model.dao.ScoreboardDAO;
import com.example.challenge.model.dao.ScoreboardId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ScoreboardRepository extends JpaRepository<ScoreboardDAO, ScoreboardId> {

    List<ScoreboardDAO> findAllBySeason(String season);

}
