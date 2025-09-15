package com.svym.inventory.scheduler.medicinedailysummary;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
public class MedicineDailySummaryScheduler {

    private static final Logger logger = LoggerFactory.getLogger(MedicineDailySummaryScheduler.class);

    @Autowired
    private MedicineDailySummaryService medicineDailySummaryService;

    // Run daily at 1:00 AM
    @Scheduled(cron = "0 0 1 * * *")
    @Transactional
    public void generateDailySummary() {
        logger.info("Starting medicine daily summary job at {}", LocalDateTime.now());

        try {
            LocalDate summaryDate = LocalDate.now().minusDays(1); // Previous day's data
            LocalDate jobRunDate = LocalDate.now();

            logger.info("Generating daily summary for date: {}", summaryDate);

            // Generate and save daily summary
            medicineDailySummaryService.generateAndSaveDailySummary(summaryDate, jobRunDate);

            logger.info("Medicine daily summary job completed successfully at {}", LocalDateTime.now());

        } catch (Exception e) {
            logger.error("Error occurred while generating medicine daily summary: {}", e.getMessage(), e);
            throw e;
        }
    }
}
