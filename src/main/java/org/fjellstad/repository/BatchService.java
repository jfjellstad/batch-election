package org.fjellstad.repository;

import org.fjellstad.model.BatchJob;
import org.fjellstad.model.JobStatus;

import java.time.ZonedDateTime;

public interface BatchService {
    BatchJob getLastRunJob(String jobb);

    int createJob(String job, ZonedDateTime timestamp);

    int deleteJob(String job);

    int updateJob(String job, ZonedDateTime timestamp, JobStatus status);
}
