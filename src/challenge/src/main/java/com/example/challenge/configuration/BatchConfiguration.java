package com.example.challenge.configuration;

import com.example.challenge.listener.SeasonStatisticsJobExecutionListener;
import com.example.challenge.mapper.ScoreBoardRowMapper;
import com.example.challenge.model.batch.Match;
import com.example.challenge.model.batch.ScoreBoard;
import com.example.challenge.processor.MatchDateFixerItemProcessor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.batch.item.file.MultiResourceItemReader;
import org.springframework.batch.item.json.JacksonJsonObjectReader;
import org.springframework.batch.item.json.JsonItemReader;
import org.springframework.batch.item.json.builder.JsonItemReaderBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import javax.sql.DataSource;

@Configuration
@EnableBatchProcessing
public class BatchConfiguration {

    @Value("classpath:/input/season-*.json")
    private Resource[] inputResources;

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Autowired
    public BatchConfiguration (JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory){
        this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory = stepBuilderFactory;
    }

    @Bean
    public MultiResourceItemReader<Match> multiResourceItemReader()
    {
        MultiResourceItemReader<Match> resourceItemReader = new MultiResourceItemReader<>();
        resourceItemReader.setResources(inputResources);
        resourceItemReader.setDelegate(reader());
        return resourceItemReader;
    }

    @Bean
    public MatchDateFixerItemProcessor processor() {
        return new MatchDateFixerItemProcessor();
    }

    @Bean
    public JsonItemReader<Match> reader(){
        return new JsonItemReaderBuilder<Match>()
                .jsonObjectReader(new JacksonJsonObjectReader<>(Match.class))
                .name("matchJsonItemReader")
                .build();
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
    public Step loadMatchesOnDB(JdbcBatchItemWriter<Match>  writer) {
        return stepBuilderFactory.get("loadMatchesOnDB").<Match, Match>chunk(100)
                .reader(multiResourceItemReader())
                .processor(processor())
                .writer(writer)
                .build();
    }

    @Bean
    public Step loadScoreBoardOnDB(JdbcCursorItemReader<ScoreBoard> reader, JdbcBatchItemWriter<ScoreBoard> writer) {
        return stepBuilderFactory.get("loadScoreBoardOnDB").<ScoreBoard, ScoreBoard>chunk(100)
                .reader(reader)
                .writer(writer)
                .build();
    }

    @Bean
    public Job seasonStatisticsJob(SeasonStatisticsJobExecutionListener listener,
                                   @Qualifier("loadMatchesOnDB") Step matches,
                                   @Qualifier("loadScoreBoardOnDB") Step scoreBoard) {
        return jobBuilderFactory.get("seasonStatisticsJob")
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .start(matches)
                .next(scoreBoard)
                .build();
    }



}
