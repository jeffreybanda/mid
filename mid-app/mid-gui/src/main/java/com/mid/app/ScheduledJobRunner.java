// File: com/mid/app/ScheduledJobRunner.java
package com.mid.app;

import com.mid.app.service.ScheduledPolicyService;
import com.mid.app.swing.model.ImportResult;

/**
 * Standalone runner for scheduled job (can be called from command line)
 */
public class ScheduledJobRunner {
    
    public static void main(String[] args) {
        System.out.println("[SCHEDULED JOB] Starting manual run...");
        
        try {
            ScheduledPolicyService service = new ScheduledPolicyService();
            ImportResult result = service.processCurrentDatePolicies();
            
            System.out.println("[SCHEDULED JOB] Result: " + result.getMessage());
            System.out.println("[SCHEDULED JOB] Success: " + result.getSuccessCount());
            System.out.println("[SCHEDULED JOB] Failures: " + result.getFailureCount());
            
            if (!result.isSuccess()) {
                System.exit(1);
            }
            
        } catch (Exception e) {
            System.err.println("[SCHEDULED JOB ERROR] " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
        
        System.out.println("[SCHEDULED JOB] Manual run completed successfully");
    }
}