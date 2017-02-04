package org.fjellstad.service;

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
    public ZonedDateTime getLastRunJob(String jobb) {
        return mapper.getLastJob(jobb);
    }

    @Override
    @Transactional
    public int createJob(String job, ZonedDateTime tidspunkt) {
        return mapper.createJob(job, tidspunkt);
    }

    @Override
    public int deleteJob(String job) {
        return 0;
    }

    @Override
    public int updateJob(String job, ZonedDateTime tidspunkt) {
        return 0;
    }
}
