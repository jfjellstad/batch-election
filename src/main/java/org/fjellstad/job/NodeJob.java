package org.fjellstad.job;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.time.LocalDateTime;

public class NodeJob extends AbstractJob {

	@Override
	protected void onExecuteInternal(JobExecutionContext context) throws JobExecutionException {
		logger.info("onExecuteInternal at {}", LocalDateTime.now());
	}
}
