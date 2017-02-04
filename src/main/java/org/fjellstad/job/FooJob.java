package org.fjellstad.job;

import org.fjellstad.service.BatchService;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import javax.inject.Inject;

public class FooJob extends AbstractJob {

	@Inject
	public FooJob(BatchService batchService) {
		super(batchService);
	}

	@Override
	protected void onExecuteInternal(JobExecutionContext context) throws JobExecutionException {
		logger.info("onExecuteInternal");
	}
}
