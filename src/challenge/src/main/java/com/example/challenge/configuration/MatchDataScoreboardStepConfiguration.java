package com.example.challenge.configuration;

import com.example.challenge.mapper.ScoreBoardRowMapper;
import com.example.challenge.model.batch.ScoreBoard;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class MatchDataScoreboardStepConfiguration {

    private final StepBuilderFactory stepBuilderFactory;

    @Autowired
    public MatchDataScoreboardStepConfiguration(StepBuilderFactory stepBuilderFactory) {
        this.stepBuilderFactory = stepBuilderFactory;
    }

    @Bean
    public JdbcCursorItemReader<ScoreBoard> matchReader(DataSource dataSource){
        return new JdbcCursorItemReaderBuilder<ScoreBoard>()
                .sql("SELECT TEAM, COUNT(TEAM) as PLAYED, SUM(SCORE) as POINTS, SUM(GOALS) as GF, SUM(GOALS_AGAINST) GA, " +
                        "SUM(GOALS) - SUM(GOALS_AGAINST) as GD, SUM(SHOTS_TARGET) SOT, " +
                        "CAST(SUM(GOALS) as FLOAT) / CAST(SUM(SHOTS_TARGET) as FLOAT) as GOAL_RATIO, SEASON " +
                        "FROM MATCH_DATA GROUP BY TEAM, SEASON ORDER BY SEASON ASC, POINTS DESC")
                .rowMapper(new ScoreBoardRowMapper())
                .dataSource(dataSource)
                .name("matchReader")
                .build();
    }

    @Bean
    public JdbcBatchItemWriter<ScoreBoard> scoreBoardJdbcBatchItemWriter(DataSource dataSource){
        return new JdbcBatchItemWriterBuilder<ScoreBoard>()
                .dataSource(dataSource)
                .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
                .sql("INSERT INTO scoreboard (team, score, matches_played, goals_favor, goals_against, " +
                        "goal_difference, shots_on_target, goal_percentage, season) values " +
                        "(:team, :score, :matchesPlayed, :goals, :goalsAgainst, :goalDifference, " +
                        ":shotsOnTarget, :goalPercentage, :season)")
                .build();
    }

    @Bean
    public Step loadScoreBoardOnDB(JdbcCursorItemReader<ScoreBoard> reader, JdbcBatchItemWriter<ScoreBoard> writer) {
        return stepBuilderFactory.get("loadScoreBoardOnDB").<ScoreBoard, ScoreBoard>chunk(100)
                .reader(reader)
                .writer(writer)
                .build();
    }

}
