package org.fjellstad.config;

import org.fjellstad.job.NodeJob;
import org.fjellstad.repository.BatchRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;

import javax.inject.Inject;

@Configuration
@Profile("node")
public class NodeConfig extends AbstractBatchConfig {

	private final String cronExpression = "0/10 * * * * ?";

	@Inject
	public NodeConfig(BatchRepository batchRepository) {
		super(batchRepository);
	}

	@Bean
	public JobDetailFactoryBean nodeJob() {
		JobDetailFactoryBean bean = new JobDetailFactoryBean();
		bean.setJobClass(NodeJob.class);
		bean.setJobDataAsMap(getJobData());
		bean.setDurability(true);

		return bean;
	}

	@Bean
	public CronTriggerFactoryBean nodeCronTrigger() {
		CronTriggerFactoryBean bean = new CronTriggerFactoryBean();
		bean.setJobDetail(nodeJob().getObject());
		bean.setCronExpression(cronExpression);

		return bean;
	}
}
