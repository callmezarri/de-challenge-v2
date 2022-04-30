package com.example.challenge.batch.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class SeasonStatisticsJobExecutionListener extends JobExecutionListenerSupport {

    @Override
    public void afterJob(JobExecution jobExecution) {
        if(jobExecution.getStatus() == BatchStatus.COMPLETED) {
            log.info("\n!!! JOB FINISHED! there are two APIs under" +
                    "\n/api/v1/{scoreboard, statistics} both of them have / for all /{season}/ to filter by season" +
                    "\nadditionally there is /api/v1/{scoreboard}/{season}/{team} for the results of an specific team" +
                    "\nalso you can login on /h2-console with user: sa and password: password," +
                    "\nbe sure to change the connection string to jdbc:h2:mem:test");
        }
    }

}