package org.fjellstad.config;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.h2.tools.Server;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.inject.Inject;
import java.sql.SQLException;

@Configuration
@EnableScheduling
@ComponentScan(basePackages = "org.fjellstad")
@MapperScan("org.fjellstad.repository")
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

    @Bean
    public SqlSessionFactory sqlSessionFactory() throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dataSource());
        return bean.getObject();
    }

    @Bean(destroyMethod = "stop")
    public Server server() throws SQLException {
        return Server.createTcpServer("-web", "-tcpAllowOthers", "-tcpPort", "7001", "-baseDir", "../foo").start();
    }
}
