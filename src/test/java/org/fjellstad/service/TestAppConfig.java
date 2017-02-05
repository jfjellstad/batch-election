package org.fjellstad.service;

import org.apache.commons.dbcp2.BasicDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.DefaultApplicationArguments;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;

import javax.inject.Inject;

@Configuration
@Primary
@PropertySource("classpath:db-test.properties")
public class TestAppConfig {
	private final Logger logger = LoggerFactory.getLogger(TestAppConfig.class);
	private final String dbUrl;
	private final String dbUsername;
	private final String dbPassword;

	@Inject
	public TestAppConfig(@Value("${db.url}") String dbUrl,
	                 @Value("${db.user}") String dbUsername,
	                 @Value("${db.password}") String dbPassword) {
		this.dbUrl = dbUrl;
		this.dbUsername = dbUsername;
		this.dbPassword = dbPassword;
	}

	@Bean(destroyMethod = "close")
	public BasicDataSource dataSource() {
		logger.info("URL: {}, Username: {}", dbUrl, dbUsername);
		BasicDataSource dataSource = new BasicDataSource();
		dataSource.setDriverClassName("org.h2.Driver");
		dataSource.setUrl(dbUrl);
		dataSource.setUsername(dbUsername);
		dataSource.setPassword(dbPassword);
		return dataSource;
	}


	@Bean
	public ApplicationArguments applicationArguments() {
		return new DefaultApplicationArguments(new String[0]);
	}
}
