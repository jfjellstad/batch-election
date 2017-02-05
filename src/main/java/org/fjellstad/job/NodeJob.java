package org.fjellstad.job;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class NodeJob extends AbstractJob {

	@Override
	protected void onExecuteInternal(JobExecutionContext context) throws JobExecutionException {
		logger.info("Won election at {}", context.getScheduledFireTime());
	}

	@Override
	protected void onLostElection(JobExecutionContext context) {
		logger.info("Lost election at {}", context.getScheduledFireTime());
	}
}
