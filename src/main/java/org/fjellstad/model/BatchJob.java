package org.fjellstad.model;

import java.sql.Timestamp;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class BatchJob {
	private final String name;
	private final ZonedDateTime schedule;
	private final JobStatus status;

	public BatchJob(String name, ZonedDateTime schedule, JobStatus status) {
		this.name = name;
		this.schedule = schedule;
		this.status = status;
	}

	public BatchJob(String name, Timestamp schedule, String status) {
		this.name = name;
		this.schedule = ZonedDateTime.ofInstant(schedule.toInstant(), ZoneId.systemDefault());
		this.status = JobStatus.toJobStatus(status);
	}

	public String getName() {
		return name;
	}

	public ZonedDateTime getSchedule() {
		return schedule;
	}

	public JobStatus getStatus() {
		return status;
	}

	@Override
	public String toString() {
		return "BatchJob[" +
				       "name='" + name + '\'' +
				       ", schedule=" + schedule +
				       ", status=" + status +
				       ']';
	}
}
