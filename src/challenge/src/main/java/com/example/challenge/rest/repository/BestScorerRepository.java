package com.example.challenge.rest.repository;

import com.example.challenge.rest.dao.BestScorerDAO;
import com.example.challenge.rest.dao.id.BestScorerID;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BestScorerRepository extends JpaRepository<BestScorerDAO, BestScorerID> {

    List<BestScorerDAO> findAllBySeason(String season);

}
