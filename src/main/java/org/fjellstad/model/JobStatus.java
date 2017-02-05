package org.fjellstad.model;

public enum JobStatus {
	RUNNING,
	FINISHED,
	FAILED,
	UNKNOWN;

	public static JobStatus toJobStatus(String status) {
		switch (status) {
			case "RUNNING":
				return RUNNING;
			case "FINISHED":
				return FINISHED;
			case "FAILED":
				return FAILED;
			case "UNKNOWN":
				return UNKNOWN;
			default:
				throw new RuntimeException("Invalid jobstatus " + status);
		}
	}
}
