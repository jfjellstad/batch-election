package org.fjellstad.repository;

import org.fjellstad.model.BatchJob;
import org.fjellstad.model.JobStatus;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.time.ZonedDateTime;

@Repository
public class BatchRepositoryMyBatis implements BatchRepository {
    private BatchMapper mapper;

    @Inject
    public BatchRepositoryMyBatis(BatchMapper mapper) {
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
