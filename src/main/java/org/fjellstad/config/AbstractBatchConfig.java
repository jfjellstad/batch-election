package org.fjellstad.config;

import org.fjellstad.repository.BatchRepository;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractBatchConfig {
	private final BatchRepository batchRepository;

	public AbstractBatchConfig(BatchRepository batchRepository) {
		this.batchRepository = batchRepository;
	}

	protected Map<String, Object> getJobData() {
		Map<String, Object> jobData = new HashMap<>();
		jobData.put("batchRepository", batchRepository);
		return jobData;
	}
}
