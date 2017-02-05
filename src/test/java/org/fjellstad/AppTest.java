package org.fjellstad;


import org.fjellstad.config.AppConfig;
import org.fjellstad.service.TestAppConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.ContextHierarchy;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextHierarchy({
        @ContextConfiguration(
                classes = {
                		AppConfig.class, TestAppConfig.class
                }
        )
})
public class AppTest {

    @Test
    public void testApp()
    {
        assertThat( true ).isTrue();
    }
}
