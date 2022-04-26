package com.example.challenge.mapper;

import com.example.challenge.model.batch.ScoreBoard;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ScoreBoardRowMapper implements RowMapper<ScoreBoard> {

    @Override
    public ScoreBoard mapRow(ResultSet rs, int rowNum) throws SQLException {
        return ScoreBoard.builder()
                .team(rs.getNString("TEAM"))
                .matchesPlayed(rs.getShort("PLAYED"))
                .score(rs.getShort("POINTS"))
                .goals(rs.getShort("GF"))
                .goalsAgainst(rs.getShort("GA"))
                .goalDifference(rs.getShort("GD"))
                .shotsOnTarget(rs.getShort("SOT"))
                .goalPercentage(rs.getDouble("GOAL_RATIO"))
                .season(rs.getNString("SEASON"))
                .build();
    }
}
