package org.fjellstad.service;

import org.fjellstad.model.BatchJob;
import org.fjellstad.model.JobStatus;
import org.fjellstad.repository.BatchMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.time.ZonedDateTime;

@Service
public class BatchServiceImpl implements BatchService {
    private BatchMapper mapper;

    @Inject
    public BatchServiceImpl(BatchMapper mapper) {
        this.mapper = mapper;
    }


    @Override
    public BatchJob getLastRunJob(String jobb) {
        return mapper.getLastJob(jobb);
    }

    @Override
    @Transactional
    public int createJob(String job, ZonedDateTime timestamp) {
        return mapper.createJob(job, timestamp, JobStatus.RUNNING);
    }

    @Override
    @Transactional
    public int deleteJob(String job) {
        return 0;
    }

    @Override
    @Transactional
    public int updateJob(String job, ZonedDateTime timestamp, JobStatus status) {
        return mapper.updateJob(job, timestamp, status);
    }
}
