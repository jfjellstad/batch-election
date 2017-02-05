package org.fjellstad.model;

import java.time.ZonedDateTime;

public class BatchJob {
	private String name;
	private ZonedDateTime schedule;
	private JobStatus status;

	public void setName(String name) {
		this.name = name;
	}

	public void setSchedule(ZonedDateTime schedule) {
		this.schedule = schedule;
	}

	public void setStatus(JobStatus status) {
		this.status = status;
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
