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
public class BestScorerStepConfiguration {

    private final StepBuilderFactory stepBuilderFactory;

    @Autowired
    public BestScorerStepConfiguration(StepBuilderFactory stepBuilderFactory) {
        this.stepBuilderFactory = stepBuilderFactory;
    }

    @Bean
    public JdbcCursorItemReader<SeasonStatistics> bestScorerReader(DataSource dataSource){
        return new JdbcCursorItemReaderBuilder<SeasonStatistics>()
                .sql("SELECT s.TEAM, s.SEASON, s.GOALS_FAVOR as STATISTIC FROM SCOREBOARD s INNER JOIN " +
                        "(SELECT SEASON, MAX(GOALS_FAVOR) as m FROM SCOREBOARD GROUP BY SEASON)  g ON s.GOALS_FAVOR = g.m AND s.SEASON = g.SEASON;")
                .rowMapper(new SeasonStatisticRowMapper())
                .dataSource(dataSource)
                .name("bestScorerReader")
                .build();
    }

    @Bean
    public JdbcBatchItemWriter<SeasonStatistics> bestScorerWriter(DataSource dataSource){
        return new JdbcBatchItemWriterBuilder<SeasonStatistics>()
                .dataSource(dataSource)
                .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
                .sql("INSERT INTO season_statistics (most_goals_number, most_goals_team, season) values " +
                        "(:value, :team, :season)")
                .build();
    }

    @Bean
    public Step loadBestScorerOnDB(
            @Qualifier("bestScorerReader") JdbcCursorItemReader<SeasonStatistics> reader,
            @Qualifier("bestScorerWriter") JdbcBatchItemWriter<SeasonStatistics> writer
    ){
        return stepBuilderFactory.get("loadBestScorerOnDB").<SeasonStatistics, SeasonStatistics>chunk(10)
                .reader(reader)
                .writer(writer)
                .build();
    }

}
