// File: com/mid/app/quartz/QuartzSchedulerApp.java
package com.mid.app.quartz.job;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.util.Properties;

public class QuartzSchedulerApp {
    
    private static final String TRIGGER_NAME = "PolicyImportTrigger";
    private static final String GROUP = "PolicyImportGroup";
    private static final String JOB_NAME = "PolicyImportJob";
    private static Scheduler scheduler;
    
    // Configuration properties
    private static final boolean USE_CRON = false; // Set to true for cron scheduling
    private static final int INTERVAL_MINUTES = 2; // Run every 2 minutes
    private static final String CRON_EXPRESSION = "0 */2 * * * ?"; // Every 2 minutes
    
    public static void main(String[] args) throws Exception {
        System.out.println("[QUARTZ] Starting scheduler...");
        
        // Configure Quartz properties
        Properties props = new Properties();
        props.put("org.quartz.scheduler.instanceName", "PolicyImportScheduler");
        props.put("org.quartz.threadPool.threadCount", "3");
        props.put("org.quartz.jobStore.class", "org.quartz.simpl.RAMJobStore");
        
        StdSchedulerFactory factory = new StdSchedulerFactory(props);
        scheduler = factory.getScheduler();
        scheduler.start();
        
        // Schedule the job
        if (USE_CRON) {
            Trigger trigger = buildCronSchedulerTrigger();
            scheduleJob(trigger);
            System.out.println("[QUARTZ] Job scheduled with cron expression: " + CRON_EXPRESSION);
        } else {
            Trigger trigger = buildSimpleSchedulerTrigger();
            scheduleJob(trigger);
            System.out.println("[QUARTZ] Job scheduled to run every " + INTERVAL_MINUTES + " minutes");
        }
        
        // Keep the scheduler running
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                System.out.println("[QUARTZ] Shutting down scheduler...");
                scheduler.shutdown(true);
                System.out.println("[QUARTZ] Scheduler shutdown complete");
            } catch (SchedulerException e) {
                e.printStackTrace();
            }
        }));
        
        System.out.println("[QUARTZ] Scheduler started successfully");
    }
    
    /**
     * Programmatic way to start the scheduler (can be called from other parts of the application)
     */
    public static void startScheduler() throws Exception {
        main(new String[]{});
    }
    
    /**
     * Programmatic way to stop the scheduler
     */
    public static void stopScheduler() throws Exception {
        if (scheduler != null && scheduler.isStarted()) {
            scheduler.shutdown(true);
            System.out.println("[QUARTZ] Scheduler stopped");
        }
    }
    
    private static void scheduleJob(Trigger trigger) throws Exception {
        JobDetail jobDetail = JobBuilder.newJob(com.mid.app.quartz.job.MidJob.class)
                .withIdentity(JOB_NAME, GROUP)
                .withDescription("Import policies created today")
                .build();
        
        scheduler.scheduleJob(jobDetail, trigger);
    }
    
    private static Trigger buildSimpleSchedulerTrigger() {
        return TriggerBuilder.newTrigger()
                .withIdentity(TRIGGER_NAME, GROUP)
                .withSchedule(SimpleScheduleBuilder.simpleSchedule()
                        .withIntervalInMinutes(INTERVAL_MINUTES)
                        .repeatForever())
                .build();
    }
    
    private static Trigger buildCronSchedulerTrigger() {
        return TriggerBuilder.newTrigger()
                .withIdentity(TRIGGER_NAME, GROUP)
                .withSchedule(CronScheduleBuilder.cronSchedule(CRON_EXPRESSION))
                .build();
    }
    
    /**
     * Check if scheduler is running
     */
    public static boolean isSchedulerRunning() {
        try {
            return scheduler != null && scheduler.isStarted() && !scheduler.isShutdown();
        } catch (SchedulerException e) {
            return false;
        }
    }
    
    /**
     * Pause the scheduler
     */
    public static void pauseScheduler() throws SchedulerException {
        if (scheduler != null && scheduler.isStarted()) {
            scheduler.pauseAll();
            System.out.println("[QUARTZ] Scheduler paused");
        }
    }
    
    /**
     * Resume the scheduler
     */
    public static void resumeScheduler() throws SchedulerException {
        if (scheduler != null && scheduler.isStarted()) {
            scheduler.resumeAll();
            System.out.println("[QUARTZ] Scheduler resumed");
        }
    }
}