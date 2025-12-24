package com.mid.app.quartz.job;

import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;

public class QuartzSchedulerApp {

	private static final String TRIGGER_NAME = "MyTriggerName";
	private static final String GROUP = "simple_Group";
	private static final String JOB_NAME = "someJob";
	private static Scheduler scheduler;

	public static void main(final String[] args) throws Exception {
		System.out.println(" QuartzSchedulerApp main thread: " + Thread.currentThread().getName());

		scheduler = new StdSchedulerFactory().getScheduler();
		scheduler.start();

		final Trigger trigger = buildSimpleSchedulerTrigger();
		// buildCronSchedulerTrigger();// for cron job trigger
		scheduleJob(trigger);

	}

	public static void runJob() throws Exception {
		System.out.println(" QuartzSchedulerApp main thread: " + Thread.currentThread().getName());

		scheduler = new StdSchedulerFactory().getScheduler();
		scheduler.start();

		final Trigger trigger = buildSimpleSchedulerTrigger();
		// buildCronSchedulerTrigger();// for cron job trigger
		scheduleJob(trigger);
	}

	private static void scheduleJob(final Trigger trigger) throws Exception {

		final JobDetail someJobDetail = JobBuilder.newJob(MidJob.class).withIdentity(JOB_NAME, GROUP).build();

		scheduler.scheduleJob(someJobDetail, trigger);

	}

	private static Trigger buildSimpleSchedulerTrigger() {

		final int INTERVAL_SECONDS = 120;

		final Trigger trigger = TriggerBuilder.newTrigger().withIdentity(TRIGGER_NAME, GROUP)
				.withSchedule(
						SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(INTERVAL_SECONDS).repeatForever())
				.build();
		return trigger;
	}

	private static Trigger buildCronSchedulerTrigger() {
		final String CRON_EXPRESSION = "0 * * * * ?";
		final Trigger trigger = TriggerBuilder.newTrigger().withIdentity(TRIGGER_NAME, GROUP)
				.withSchedule(CronScheduleBuilder.cronSchedule(CRON_EXPRESSION)).build();

		return trigger;
	}
}
