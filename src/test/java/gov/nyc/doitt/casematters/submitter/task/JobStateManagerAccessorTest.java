package gov.nyc.doitt.casematters.submitter.task;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.net.URI;
import java.util.List;

import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import gov.nyc.doitt.casematters.submitter.TaskDtoMockerUpper;
import gov.nyc.doitt.casematters.submitter.TestBase;

@RunWith(SpringRunner.class)
@SpringBootTest
public class JobStateManagerAccessorTest extends TestBase {

	@Autowired
	private TaskDtoMockerUpper taskDtoMockerUpper;

	@Value("${submitter.JobStateManagerAccessor.baseUrl}")
	private String baseUrl;

	@Value("${submitter.JobStateManagerAccessor.jobName}")
	private String jobName;

	@Value("${submitter.JobStateManagerAccessor.taskName}")
	private String taskName;

	@Value("${submitter.JobStateManagerAccessor.authToken}")
	private String authToken;

	@Mock
	private RestTemplate restTemplate;

	@InjectMocks
	private JobStateManagerAccessor jobStateManagerAccessor = new JobStateManagerAccessor();

	@Before
	public void init() throws Exception {

		FieldUtils.writeField(jobStateManagerAccessor, "baseUrl", baseUrl, true);
		FieldUtils.writeField(jobStateManagerAccessor, "jobName", jobName, true);
		FieldUtils.writeField(jobStateManagerAccessor, "taskName", taskName, true);
		FieldUtils.writeField(jobStateManagerAccessor, "authToken", authToken, true);
	}

	@Test
	public void testStartNextBatchOfTasks() throws Exception {

		int listSize = 5;
		List<TaskDto> taskDtos = taskDtoMockerUpper.createList(listSize);

		TaskDto[] taskDtoArray = taskDtos.toArray(new TaskDto[0]);

		when(restTemplate.postForObject(any(URI.class), any(HttpEntity.class), any(Class.class))).thenReturn(taskDtoArray);

		List<TaskDto> responseTaskDtos = jobStateManagerAccessor.startNextBatchOfTasks();
		assertEquals(taskDtos.size(), responseTaskDtos.size());
		verify(restTemplate).postForObject(any(URI.class), any(HttpEntity.class), any(Class.class));
	}

	@Test
	public void testUpdateTaskResults() throws Exception {

		int listSize = 5;
		List<TaskDto> taskDtos = taskDtoMockerUpper.createList(listSize);

		try {
			jobStateManagerAccessor.updateTaskResults(taskDtos);
			assertTrue(true);
		} catch (JobStateManagerException e) {
			assertTrue(false);
		}
	}

	@Test
	public void testUpdateTaskResults_Fail() throws Exception {

		int listSize = 5;
		List<TaskDto> taskDtos = taskDtoMockerUpper.createList(listSize);

		doThrow(new HttpClientErrorException(HttpStatus.UNAUTHORIZED)).when(restTemplate).put(any(URI.class), any(HttpEntity.class));
		try {
			jobStateManagerAccessor.updateTaskResults(taskDtos);
			assertTrue(false);
		} catch (JobStateManagerException e) {
			assertEquals(HttpClientErrorException.class, e.getCause().getClass());
		}
	}

}
