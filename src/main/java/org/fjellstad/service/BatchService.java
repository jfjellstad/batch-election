package org.fjellstad.service;

import java.time.ZonedDateTime;

public interface BatchService {
    ZonedDateTime getLastRunJob(String jobb);

    int createJob(String job, ZonedDateTime tidspunkt);

    int deleteJob(String job);

    int updateJob(String job, ZonedDateTime tidspunkt);
}
