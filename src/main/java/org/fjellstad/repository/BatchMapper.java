package org.fjellstad.repository;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.fjellstad.model.JobStatus;

import java.time.ZonedDateTime;

public interface BatchMapper {
    @Insert("insert into BATCH (NAME, SCHEDULE, STATUS) values (#{jobName}, #{timestamp}, #{status})")
    int createJob(@Param("jobName") String jobName,
                  @Param("timestamp")ZonedDateTime timestamp,
                  @Param("status")JobStatus status);

    @Select("select max(SCHEDULE) from BATCH where NAME = #{jobName}")
    ZonedDateTime getLastJob(@Param("jobName") String jobname);

    @Update("update BATCH set status=#{status} where name=#{jobName} and schedule=#{timestamp}")
    int updateJob(@Param("jobName") String jobname,
                  @Param("timestamp") ZonedDateTime timestamp,
                  @Param("status") JobStatus status);
}
