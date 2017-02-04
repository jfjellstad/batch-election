package org.fjellstad.job;

import org.fjellstad.service.BatchService;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.time.ZoneOffset;
import java.util.Date;

public abstract class AbstractJob extends QuartzJobBean {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final String jobName = getClass().getSimpleName();

    private BatchService batchService;

    public AbstractJob(BatchService batchService) {
        this.batchService = batchService;
    }

    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        if (!jobLock(context)) {
            onLostElection(context);
            return;
        }
        onExecuteInternal(context);
    }

    private boolean jobLock(JobExecutionContext context) {
        try {
            Date scheduled = context.getScheduledFireTime();
            batchService.createJob(jobName, scheduled.toInstant().atZone(ZoneOffset.systemDefault()));
            return true;
        } catch(DuplicateKeyException ignore) {
            return false;
        }
    }

    protected abstract void onExecuteInternal(JobExecutionContext context) throws JobExecutionException;

    protected void onLostElection(JobExecutionContext context) {
    }
}
