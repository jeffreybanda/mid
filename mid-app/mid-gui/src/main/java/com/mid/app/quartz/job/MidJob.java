// File: com/mid/app/quartz/job/MidJob.java (simplified and correct)
package com.mid.app.quartz.job;

import com.mid.app.service.ScheduledPolicyService;
import com.mid.app.swing.model.ImportResult;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.time.LocalDateTime;

public class MidJob implements Job {

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        LocalDateTime startTime = LocalDateTime.now();
        System.out.println("[QUARTZ JOB] Started at " + startTime);
        
        try {
            ScheduledPolicyService service = new ScheduledPolicyService();
            ImportResult result = service.processCurrentDatePolicies();
            
            LocalDateTime endTime = LocalDateTime.now();
            long durationSeconds = java.time.Duration.between(startTime, endTime).getSeconds();
            
            System.out.println("[QUARTZ JOB] " + result.getMessage());
            System.out.println("[QUARTZ JOB] Duration: " + durationSeconds + " seconds");
            
            // Note: Cleanup is now handled separately by TrackingCleanupJob
            // OR it's done automatically in ScheduledPolicyService constructor
            
            if (!result.isSuccess()) {
                throw new JobExecutionException("Scheduled job failed: " + result.getMessage());
            }
            
        } catch (Exception e) {
            System.err.println("[QUARTZ JOB ERROR] " + e.getMessage());
            e.printStackTrace();
            throw new JobExecutionException(e);
        }
        
        System.out.println("[QUARTZ JOB] Completed at " + LocalDateTime.now());
    }
}