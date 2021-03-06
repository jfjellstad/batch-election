package org.fjellstad.config;

import org.apache.commons.dbcp2.BasicDataSource;
import org.h2.tools.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.inject.Inject;
import java.sql.SQLException;

@Configuration
@ComponentScan(basePackages = "org.fjellstad")
@PropertySource("classpath:db.properties")
@EnableTransactionManagement
public class AppConfig {
	private final Logger logger = LoggerFactory.getLogger(AppConfig.class);

	private final String dbUrl;
	private final String dbUsername;
	private final String dbPassword;

	@Inject
	public AppConfig(@Value("${db.url}") String dbUrl,
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
	public PlatformTransactionManager transactionManager() {
		return new DataSourceTransactionManager(dataSource());
	}

	@Bean(destroyMethod = "stop")
	@Profile("server")
	public Server server() throws SQLException {
        return Server.createTcpServer("-tcpAllowOthers", "-tcpPort", "7001", "-baseDir", "./target");
	}
}
