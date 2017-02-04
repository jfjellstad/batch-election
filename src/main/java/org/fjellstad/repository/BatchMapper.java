package org.fjellstad.repository;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.time.ZonedDateTime;

public interface BatchMapper {
    @Insert("insert into BATCH (NAME, SCHEDULE) values (#{jobName}, #{timestamp})")
    int createJob(@Param("jobName") String jobName, @Param("timestamp")ZonedDateTime timestamp);

    @Select("select max(SCHEDULE) from BATCH where NAME = #{jobName}")
    ZonedDateTime getLastJob(@Param("jobName") String jobname);
}
