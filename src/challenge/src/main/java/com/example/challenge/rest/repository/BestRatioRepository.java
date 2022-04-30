package com.example.challenge.rest.repository;

import com.example.challenge.rest.dao.BestRatioDAO;
import com.example.challenge.rest.dao.id.BestRatioID;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BestRatioRepository extends JpaRepository<BestRatioDAO, BestRatioID> {

    List<BestRatioDAO> findAllBySeason(String season);

}
