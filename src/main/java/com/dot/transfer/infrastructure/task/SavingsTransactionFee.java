package com.dot.transfer.infrastructure.task;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class SavingsTransactionFee {

    private final JobLauncher jobLauncher;
    private final Job job;


    // @Scheduled(cron = "0 0 0 * * ?") // --- Actual Cron
    @Scheduled(cron = "*/30 * * * * *") // --- Test Purpose
    public void feeDeduction() {
        try {
            JobParameters jobParameters = new JobParametersBuilder()
                    .addLong("start_at", System.currentTimeMillis()) // Unique job parameter

                    .toJobParameters();

            JobExecution jobExecution = jobLauncher.run(job, jobParameters);
            log.info("Job Status : {}" , jobExecution.getStatus());
            log.info("Job completed");
        } catch (Exception e) {
            log.error("Job failed", e);
        }
    }

}
