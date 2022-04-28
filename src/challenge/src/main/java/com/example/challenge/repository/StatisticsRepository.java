package com.example.challenge.repository;

import com.example.challenge.model.dao.StatisticsDAO;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StatisticsRepository extends JpaRepository<StatisticsDAO, String> {

}
