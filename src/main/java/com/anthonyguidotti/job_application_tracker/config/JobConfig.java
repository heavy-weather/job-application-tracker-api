package com.anthonyguidotti.job_application_tracker.config;

import com.anthonyguidotti.job_application_tracker.job_application.JobApplicationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;

import java.time.temporal.ChronoUnit;

import java.time.ZoneId;
import java.time.ZonedDateTime;

import static com.anthonyguidotti.job_application_tracker.job_application.JobApplicationStatus.ACTIVE;
import static com.anthonyguidotti.job_application_tracker.job_application.JobApplicationStatus.INACTIVE;

@Configuration
public class JobConfig {
    private static final Logger LOGGER = LoggerFactory.getLogger(JobConfig.class.getName());
    private final JobApplicationService jobApplicationService;

    public JobConfig(JobApplicationService jobApplicationService) {
        this.jobApplicationService = jobApplicationService;
    }

    @Transactional
    @Scheduled(cron = "0 0 0 * * *")
    public void markInactiveEntries() {
        ZonedDateTime thirtyDaysAgo = ZonedDateTime.now(ZoneId.of("UTC"))
                .truncatedTo(ChronoUnit.DAYS)
                .minusDays(30);
        LOGGER.info("Marking job applications older than {} as inactive...", thirtyDaysAgo);
        int updateCount = jobApplicationService.updateStatusForApplicationsOlderThanDate(thirtyDaysAgo, ACTIVE, INACTIVE);
        LOGGER.info("{} job applications marked as inactive", updateCount);
    }
}
