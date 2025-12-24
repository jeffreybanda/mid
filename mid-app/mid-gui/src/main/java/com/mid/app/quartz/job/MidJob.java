package com.mid.app.quartz.job;

import java.io.IOException;
import java.time.LocalDateTime;

import org.apache.http.auth.AuthenticationException;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.mid.app.tasks.MidTask;

public class MidJob implements Job {

	public void execute(final JobExecutionContext context) throws JobExecutionException {
		final LocalDateTime localTime = LocalDateTime.now();
		System.out.println("Run QuartzJob at " + localTime.toString());

		final MidTask midTask = new MidTask();
		try {
			midTask.perform();
		} catch (AuthenticationException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
