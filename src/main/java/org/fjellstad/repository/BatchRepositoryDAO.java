package org.fjellstad.repository;

import org.fjellstad.model.BatchJob;
import org.fjellstad.model.JobStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.sql.Types;
import java.time.ZonedDateTime;

@Repository
public class BatchRepositoryDAO implements BatchRepository {

    private final JdbcTemplate jdbcTemplate;

    @Inject
    public BatchRepositoryDAO(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }


    @Override
    public BatchJob getLastRunJob(String jobb) {
        return jdbcTemplate.queryForObject("select * from BATCH where name=? and schedule=(select max(SCHEDULE) from BATCH where NAME = ?)",
                                           new Object[]{jobb, jobb},
                                           new int[]{Types.NVARCHAR, Types.NVARCHAR},
                                           (ResultSet rs, int rowNum) -> new BatchJob(rs.getString("name"), rs.getTimestamp("schedule"), rs.getString("status")));
    }

    @Override
    @Transactional
    public int createJob(String job, ZonedDateTime timestamp) {
	    return jdbcTemplate.update("insert into BATCH (NAME, SCHEDULE, STATUS) values (?, ?, ?)", job, toTimestamp(timestamp), JobStatus.RUNNING.name());
    }

    @Override
    @Transactional
    public int deleteJob(String job) {
        return 0;
    }

    @Override
    @Transactional
    public int updateJob(String job, ZonedDateTime timestamp, JobStatus status) {
	    return jdbcTemplate.update("update BATCH set status=? where name=? and schedule=?", status.name(), job, toTimestamp(timestamp));
    }

    private Timestamp toTimestamp(ZonedDateTime dt) {
    	return Timestamp.from(dt.toInstant());
    }
}
