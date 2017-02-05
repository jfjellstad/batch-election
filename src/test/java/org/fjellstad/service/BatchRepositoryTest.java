package org.fjellstad.service;

import org.apache.ibatis.session.SqlSessionFactory;
import org.fjellstad.config.AppConfig;
import org.fjellstad.model.BatchJob;
import org.fjellstad.model.JobStatus;
import org.fjellstad.repository.BatchMapper;
import org.fjellstad.repository.BatchRepository;
import org.fjellstad.repository.BatchRepositoryMyBatis;
import org.flywaydb.core.Flyway;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.ContextHierarchy;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.sql.DataSource;
import java.time.ZonedDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextHierarchy({
        @ContextConfiguration(
                classes = {
                        AppConfig.class,
                        TestAppConfig.class
                }
        )
})
@Transactional
public class BatchRepositoryTest {
    private final Logger logger = LoggerFactory.getLogger(BatchRepositoryTest.class);

    @Inject
    private SqlSessionFactory sessionFactory;
	@Inject
	private DataSource dataSource;

    private BatchRepository batchRepository;

    @PostConstruct
    public void initSetup() throws Exception {
        Flyway flyway = new Flyway();
        flyway.setDataSource(dataSource);
        flyway.migrate();
    }

    @Before
    public void setUp() throws Exception {
        SqlSessionTemplate sessionTemplate = new SqlSessionTemplate(sessionFactory);
        batchRepository = new BatchRepositoryMyBatis(sessionTemplate.getMapper(BatchMapper.class));
    }

    @Test
    public void insertRowInDB() {
        int result = batchRepository.createJob(BatchRepositoryTest.class.getSimpleName(), ZonedDateTime.now());
        assertThat(result).isEqualTo(1);
    }

    @Test
    public void selectRetrieveLatestJob() {
        batchRepository.createJob(BatchRepositoryTest.class.getSimpleName(), ZonedDateTime.now().minusDays(1));
        batchRepository.createJob(BatchRepositoryTest.class.getSimpleName(), ZonedDateTime.now());
        BatchJob result = batchRepository.getLastRunJob(BatchRepositoryTest.class.getSimpleName());
        logger.info("Timestamp retrieved: {}", result.getSchedule());
        assertThat(result.getSchedule().getDayOfWeek()).isEqualTo(ZonedDateTime.now().getDayOfWeek());
    }

    @Test
    public void updateJobStatus() {
        ZonedDateTime timestamp = ZonedDateTime.now();
        batchRepository.createJob(BatchRepositoryTest.class.getSimpleName(), timestamp);
        int resultat = batchRepository.updateJob(BatchRepositoryTest.class.getSimpleName(), timestamp, JobStatus.FINISHED);

        assertThat(resultat).isEqualTo(1);
    }

}