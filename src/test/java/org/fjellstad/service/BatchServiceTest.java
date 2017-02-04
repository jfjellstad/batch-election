package org.fjellstad.service;

import org.apache.ibatis.session.SqlSessionFactory;
import org.fjellstad.config.AppConfig;
import org.fjellstad.repository.BatchMapper;
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

import javax.inject.Inject;
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
public class BatchServiceTest {
    private final Logger logger = LoggerFactory.getLogger(BatchServiceTest.class);

    @Inject
    private SqlSessionFactory sessionFactory;

    private BatchService batchService;

    @Before
    public void setUp() throws Exception {
        SqlSessionTemplate sessionTemplate = new SqlSessionTemplate(sessionFactory);
        batchService = new BatchServiceImpl(sessionTemplate.getMapper(BatchMapper.class));
    }

    @Test
    public void insertRowInDB() {
        int result = batchService.createJob(BatchServiceTest.class.getSimpleName(), ZonedDateTime.now());
        assertThat(result).isEqualTo(1);
    }

    @Test
    public void selectRetrieveLatestJob() {
        batchService.createJob(BatchServiceTest.class.getSimpleName(), ZonedDateTime.now().minusDays(1));
        batchService.createJob(BatchServiceTest.class.getSimpleName(), ZonedDateTime.now());
        ZonedDateTime result = batchService.getLastRunJob(BatchServiceTest.class.getSimpleName());
        logger.info("Timestamp retrieved: {}", result);
        assertThat(result.getDayOfWeek()).isEqualTo(ZonedDateTime.now().getDayOfWeek());
    }

}