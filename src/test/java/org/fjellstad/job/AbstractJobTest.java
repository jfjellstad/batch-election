package org.fjellstad.job;

import org.fjellstad.model.BatchJob;
import org.fjellstad.model.JobStatus;
import org.fjellstad.service.BatchService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.dao.DuplicateKeyException;

import java.time.ZonedDateTime;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

public class AbstractJobTest {

	@Mock
	private JobExecutionContext jobContext;
	@Mock
	private BatchService batchService;
	private FakeJob fakeJob;

	@Before
	public void setUp() {
		initMocks(this);

		fakeJob = new FakeJob();
		fakeJob.setBatchService(batchService);

		when(jobContext.getScheduledFireTime()).thenReturn(new Date());
	}

	@Test
	public void test() throws Exception {
		fakeJob.withFlag(true).withMessage("should run");

		fakeJob.executeInternal(jobContext);

		verify(batchService, times(1)).createJob(anyString(), any(ZonedDateTime.class));
	}

	@Test
	public void whenAnotherJobRunningThrowDuplicateKeyException() throws Exception {
		fakeJob.withFlag(false).withMessage("should not run");

		when(batchService.createJob(anyString(), any(ZonedDateTime.class))).thenThrow(new DuplicateKeyException("Duplicate Key"));
		BatchJob job = mock(BatchJob.class);
		when(batchService.getLastRunJob(anyString())).thenReturn(job);
		when(job.getStatus()).thenReturn(JobStatus.RUNNING);

		fakeJob.executeInternal(jobContext);

		assertThat(true).isTrue();

		verify(batchService, times(1)).createJob(anyString(), any(ZonedDateTime.class));
		verify(batchService).getLastRunJob(anyString());
		verify(batchService, never()).updateJob(anyString(), any(ZonedDateTime.class), any(JobStatus.class));
	}

	public static class FakeJob extends AbstractJob {
		private boolean flag;
		private String message;

		@Override
		protected void onExecuteInternal(JobExecutionContext context) throws JobExecutionException {
			assertThat(flag).as(message).isTrue();
		}

		public FakeJob withFlag(boolean flag) {
			this.flag = flag;
			return this;
		}

		public FakeJob withMessage(String message) {
			this.message = message;
			return this;
		}
	}
}