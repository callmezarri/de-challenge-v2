package com.example.challenge.batch.mapper;

import com.example.challenge.batch.model.SeasonStatistics;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SeasonStatisticRowMapper implements RowMapper<SeasonStatistics> {

    @Override
    public SeasonStatistics mapRow(ResultSet rs, int rowNum) throws SQLException {
        return SeasonStatistics.builder()
                .team(rs.getNString("TEAM"))
                .value(rs.getDouble("STATISTIC"))
                .season(rs.getNString("SEASON"))
                .build();
    }

}
