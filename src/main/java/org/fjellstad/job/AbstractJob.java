package org.fjellstad.job;

import org.fjellstad.service.BatchService;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.Objects;

public abstract class AbstractJob extends QuartzJobBean {
	private final String jobName = getClass().getSimpleName();
	protected final Logger logger = LoggerFactory.getLogger(getClass());

	private BatchService batchService;

	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		if (!jobLock(context)) {
			onLostElection(context);
			return;
		}
		try {
			onExecuteInternal(context);
		} catch (Throwable ex) {
			logger.error("Job failed at {}", ZonedDateTime.ofInstant(context.getScheduledFireTime().toInstant(),
			                                                         ZoneOffset.systemDefault()));
		}
	}

	private boolean jobLock(JobExecutionContext context) {
		try {
			Date scheduled = context.getScheduledFireTime();
			batchService.createJob(jobName, scheduled.toInstant().atZone(ZoneOffset.systemDefault()));
			return true;
		} catch (DuplicateKeyException ignore) {
			return false;
		}
	}

	protected abstract void onExecuteInternal(JobExecutionContext context) throws JobExecutionException;

	@SuppressWarnings("WeakerAccess")
	protected void onLostElection(JobExecutionContext context) {
	}

	/**
	 * Used by Quartz scheduler to set service
	 */
	@SuppressWarnings("unused")
	public void setBatchService(BatchService batchService) {
		Objects.requireNonNull(batchService, "batchService cannot be null");
		this.batchService = batchService;
	}
}
