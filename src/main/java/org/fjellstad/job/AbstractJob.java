package org.fjellstad.job;

import org.fjellstad.model.BatchJob;
import org.fjellstad.model.JobStatus;
import org.fjellstad.repository.BatchRepository;
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

	private BatchRepository batchRepository;

	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		if (!jobLock(context)) {
			onLostElection(context);
			return;
		}
		ZonedDateTime scheduledTime = ZonedDateTime.ofInstant(context.getScheduledFireTime().toInstant(),
		                                                      ZoneOffset.systemDefault());
		try {
			onExecuteInternal(context);
			batchRepository.updateJob(jobName, scheduledTime, JobStatus.FINISHED);
		} catch (Exception ex) {
			logger.error("Job failed at {}", scheduledTime);
			batchRepository.updateJob(jobName, scheduledTime, JobStatus.FAILED);
		}
	}

	private boolean jobLock(JobExecutionContext context) {
		try {
			Date scheduled = context.getScheduledFireTime();
			batchRepository.createJob(jobName, scheduled.toInstant().atZone(ZoneOffset.systemDefault()).withNano(0));
			return true;
		} catch (DuplicateKeyException ignore) {
			return checkJobLock(context);
		}
	}

	private boolean checkJobLock(JobExecutionContext context) {
		ZonedDateTime scheduled = ZonedDateTime.ofInstant(context.getScheduledFireTime().toInstant(),
		                                                  ZoneOffset.systemDefault()).withNano(0);
		BatchJob lastJob = batchRepository.getLastRunJob(jobName);
		if (lastJob.getStatus() == JobStatus.FAILED && scheduled.isEqual(lastJob.getSchedule())) {
			logger.info("Restarting failed job {}", jobName);
			batchRepository.updateJob(jobName, scheduled, JobStatus.RUNNING);
			return true;
		}
		return false;
	}

	protected abstract void onExecuteInternal(JobExecutionContext context) throws JobExecutionException;

	/**
	 *  Overloaded by subclasses if they need it
	 */
	@SuppressWarnings("WeakerAccess")
	protected void onLostElection(JobExecutionContext context) {
	}

	/**
	 * Used by Quartz scheduler to set service
	 */
	@SuppressWarnings("unused")
	public void setBatchRepository(BatchRepository batchRepository) {
		Objects.requireNonNull(batchRepository, "batchRepository cannot be null");
		this.batchRepository = batchRepository;
	}
}
