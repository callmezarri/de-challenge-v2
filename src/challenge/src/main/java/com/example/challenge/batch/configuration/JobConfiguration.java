package com.example.challenge.batch.configuration;

import com.example.challenge.batch.listener.SeasonStatisticsJobExecutionListener;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableBatchProcessing
public class JobConfiguration {
    private final JobBuilderFactory jobBuilderFactory;

    @Autowired
    public JobConfiguration(JobBuilderFactory jobBuilderFactory){
        this.jobBuilderFactory = jobBuilderFactory;
    }

    @Bean
    public Job seasonStatisticsJob(SeasonStatisticsJobExecutionListener listener,
                                   @Qualifier("loadMatchesOnDB") Step matches,
                                   @Qualifier("loadScoreBoardOnDB") Step scoreBoard,
                                   @Qualifier("loadBestScorerOnDB") Step loadBestScorerOnDB,
                                   @Qualifier("loadMostScoredOnDB") Step loadMostScoredOnDB,
                                   @Qualifier("loadBestRatioOnDB") Step loadBestRatioOnDB
    ) {
        return jobBuilderFactory.get("seasonStatisticsJob")
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .start(matches)
                .next(scoreBoard)
                .next(loadBestScorerOnDB)
                .next(loadMostScoredOnDB)
                .next(loadBestRatioOnDB)
                .build();
    }

}
