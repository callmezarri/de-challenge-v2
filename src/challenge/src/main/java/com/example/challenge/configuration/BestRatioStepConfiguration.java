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
public class BestRatioStepConfiguration {

    private final StepBuilderFactory stepBuilderFactory;

    @Autowired
    public BestRatioStepConfiguration(StepBuilderFactory stepBuilderFactory) {
        this.stepBuilderFactory = stepBuilderFactory;
    }

    @Bean
    public JdbcCursorItemReader<SeasonStatistics> bestRatioReader(DataSource dataSource){
        return new JdbcCursorItemReaderBuilder<SeasonStatistics>()
                .sql("SELECT s.TEAM, s.SEASON, s.GOAL_PERCENTAGE as STATISTIC FROM SCOREBOARD s INNER JOIN " +
                        "(SELECT SEASON, MAX(GOAL_PERCENTAGE) as m FROM SCOREBOARD GROUP BY SEASON)  g ON s.GOAL_PERCENTAGE = g.m AND s.SEASON = g.SEASON;")
                .rowMapper(new SeasonStatisticRowMapper())
                .dataSource(dataSource)
                .name("bestRatioReader")
                .build();
    }

    @Bean
    public JdbcBatchItemWriter<SeasonStatistics> bestRatioWriter(DataSource dataSource){
        return new JdbcBatchItemWriterBuilder<SeasonStatistics>()
                .dataSource(dataSource)
                .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
                .sql("UPDATE season_statistics SET best_goals_ratio_number = :value, best_goals_ratio_team = :team WHERE SEASON = :season")
                .build();
    }

    @Bean
    public Step loadBestRatioOnDB(
            @Qualifier("bestRatioReader") JdbcCursorItemReader<SeasonStatistics> reader,
            @Qualifier("bestRatioWriter") JdbcBatchItemWriter<SeasonStatistics> writer
    ){
        return stepBuilderFactory.get("loadBestRatioOnDB").<SeasonStatistics, SeasonStatistics>chunk(10)
                .reader(reader)
                .writer(writer)
                .build();
    }

}
