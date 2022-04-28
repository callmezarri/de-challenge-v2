package com.example.challenge.configuration;

import com.example.challenge.mapper.SeasonStatisticRowMapper;
import com.example.challenge.model.batch.SeasonStatistics;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class MostScoredStepConfiguration {

    private final StepBuilderFactory stepBuilderFactory;

    @Autowired
    public MostScoredStepConfiguration(StepBuilderFactory stepBuilderFactory) {
        this.stepBuilderFactory = stepBuilderFactory;
    }

    @Bean
    public JdbcCursorItemReader<SeasonStatistics> mostScoredReader(DataSource dataSource){
        return new JdbcCursorItemReaderBuilder<SeasonStatistics>()
                .sql("SELECT s.TEAM, s.SEASON, s.GOALS_AGAINST as STATISTIC FROM SCOREBOARD s INNER JOIN " +
                        "(SELECT SEASON, MAX(GOALS_AGAINST) as m FROM SCOREBOARD GROUP BY SEASON)  g ON s.GOALS_AGAINST = g.m AND s.SEASON = g.SEASON;")
                .rowMapper(new SeasonStatisticRowMapper())
                .dataSource(dataSource)
                .name("bestScorerReader")
                .build();
    }

    @Bean
    public JdbcBatchItemWriter<SeasonStatistics> mostScoredWriter(DataSource dataSource){
        return new JdbcBatchItemWriterBuilder<SeasonStatistics>()
                .dataSource(dataSource)
                .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
                .sql("UPDATE season_statistics SET most_goals_against_number = :value, most_goals_against_team = :team WHERE SEASON = :season")
                .build();
    }

    @Bean
    public Step loadMostScoredOnDB(
            @Qualifier("mostScoredReader") JdbcCursorItemReader<SeasonStatistics> reader,
            @Qualifier("mostScoredWriter") JdbcBatchItemWriter<SeasonStatistics> writer
    ){
        return stepBuilderFactory.get("loadMostScoredOnDB").<SeasonStatistics, SeasonStatistics>chunk(10)
                .reader(reader)
                .writer(writer)
                .build();
    }

}
