package org.fjellstad.config;

import org.quartz.Trigger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import javax.inject.Inject;
import java.util.Collections;
import java.util.List;

@Configuration
@EnableScheduling
@Profile("node")
public class BatchConfig {
	private List<Trigger> triggers = Collections.emptyList();

	@Bean
	public SchedulerFactoryBean schedulerFactory() {
		SchedulerFactoryBean schedulerFactoryBean = new SchedulerFactoryBean();
		schedulerFactoryBean.setTriggers(triggers.toArray(new Trigger[triggers.size()]));

		return schedulerFactoryBean;
	}

	@Inject
	public void setTriggers(List<Trigger> triggers) {
		this.triggers = triggers;
	}
}
