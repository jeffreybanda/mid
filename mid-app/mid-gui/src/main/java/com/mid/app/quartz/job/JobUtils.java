package com.mid.app.quartz.job;

import java.util.List;

import org.quartz.JobExecutionContext;
import org.quartz.SchedulerException;

public class JobUtils {

	public static boolean isJobRunning(final JobExecutionContext ctx, final String jobName, final String groupName)
			throws SchedulerException {
		final List<JobExecutionContext> currentJobs = ctx.getScheduler().getCurrentlyExecutingJobs();

		for (final JobExecutionContext jobCtx : currentJobs) {
			final String thisJobName = jobCtx.getJobDetail().getKey().getName();
			final String thisGroupName = jobCtx.getJobDetail().getKey().getGroup();
			if (jobName.equalsIgnoreCase(thisJobName) && groupName.equalsIgnoreCase(thisGroupName)
					&& !jobCtx.getFireTime().equals(ctx.getFireTime())) {
				return true;
			}
		}
		return false;
	}

}
