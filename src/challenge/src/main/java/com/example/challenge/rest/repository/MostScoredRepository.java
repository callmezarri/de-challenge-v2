package com.example.challenge.rest.repository;

import com.example.challenge.rest.dao.MostScoredDAO;
import com.example.challenge.rest.dao.id.MostScoredID;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MostScoredRepository extends JpaRepository<MostScoredDAO, MostScoredID> {

    List<MostScoredDAO> findAllBySeason(String season);

}
