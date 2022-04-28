package com.example.challenge.configuration;

import com.example.challenge.model.batch.Match;
import com.example.challenge.processor.MatchDateFixerItemProcessor;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.MultiResourceItemReader;
import org.springframework.batch.item.json.JacksonJsonObjectReader;
import org.springframework.batch.item.json.JsonItemReader;
import org.springframework.batch.item.json.builder.JsonItemReaderBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import javax.sql.DataSource;

@Configuration
public class MatchMatchDataStepConfiguration {

    private final StepBuilderFactory stepBuilderFactory;

    @Value("classpath:/input/season-*.json")
    private Resource[] inputResources;

    @Autowired
    public MatchMatchDataStepConfiguration(StepBuilderFactory stepBuilderFactory) {
        this.stepBuilderFactory = stepBuilderFactory;
    }

    @Bean
    public JsonItemReader<Match> matchJsonItemReader(){
        return new JsonItemReaderBuilder<Match>()
                .jsonObjectReader(new JacksonJsonObjectReader<>(Match.class))
                .name("matchJsonItemReader")
                .build();
    }

    @Bean
    public MultiResourceItemReader<Match> multiResourceItemReader()
    {
        MultiResourceItemReader<Match> resourceItemReader = new MultiResourceItemReader<>();
        resourceItemReader.setResources(inputResources);
        resourceItemReader.setDelegate(matchJsonItemReader());
        return resourceItemReader;
    }

    @Bean
    public MatchDateFixerItemProcessor dateFixerItemProcessor() {
        return new MatchDateFixerItemProcessor();
    }

    @Bean
    public JdbcBatchItemWriter<Match> writer(DataSource dataSource) {
        return new JdbcBatchItemWriterBuilder<Match>()
                .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
                .sql("INSERT INTO match_data (team, score, date, season, goals, goals_against, shots_target) " +
                        "VALUES (:homeTeam, :homeScore, :date, :season, :fullTimeHomeGoals, :fullTimeAwayGoals, :homeShotTarget); " +
                        "INSERT INTO match_data (team, score, date, season, goals, goals_against, shots_target) " +
                        "VALUES (:awayTeam, :awayScore, :date, :season, :fullTimeAwayGoals, :fullTimeHomeGoals, :awayShotTarget)"
                )
                .dataSource(dataSource)
                .build();
    }

    @Bean
    public Step loadMatchesOnDB(JdbcBatchItemWriter<Match>  writer) {
        return stepBuilderFactory.get("loadMatchesOnDB").<Match, Match>chunk(100)
                .reader(multiResourceItemReader())
                .processor(dateFixerItemProcessor())
                .writer(writer)
                .build();
    }

}
