package gov.nyc.doitt.casematters.submitter.task;

import java.net.URI;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Component
public class JobStateManagerAccessor {

	private Logger logger = LoggerFactory.getLogger(JobStateManagerAccessor.class);

	@Value("${submitter.JobStateManagerAccessor.baseUrl}")
	private String baseUrl;

	@Value("${submitter.JobStateManagerAccessor.jobName}")
	private String jobName;

	@Value("${submitter.JobStateManagerAccessor.taskName}")
	private String taskName;

	@Value("${submitter.JobStateManagerAccessor.authToken}")
	private String authToken;

	@Autowired
	private RestTemplate restTemplate;

	/**
	 * Return next batch of task ids
	 * 
	 * @return
	 */
	public List<TaskDto> startNextBatchOfTasks() {

		String getTasksBatchUrl = String.format("%s/tasks?jobName=%s&taskName=%s", baseUrl, jobName, taskName);
		try {
			HttpHeaders headers = createHttpHeaders();
			HttpEntity<?> entity = new HttpEntity<Object>(headers);

			return Arrays.asList(restTemplate.postForObject(new URI(getTasksBatchUrl), entity, TaskDto[].class));
		} catch (HttpClientErrorException e) {
			throw new JobStateManagerException(e.getResponseBodyAsString(), e);
		} catch (Exception e) {
			throw new JobStateManagerException(e);
		}
	}

	public void updateTaskResults(List<TaskDto> taskDtos) {

		String updateTasksUrl = String.format("%s/tasks?jobName=%s&taskName=%s", baseUrl, jobName, taskName);
		try {
			HttpHeaders headers = createHttpHeaders();
			HttpEntity<?> entity = new HttpEntity<Object>(taskDtos.toArray(), headers);

			restTemplate.put(new URI(updateTasksUrl), entity);
		} catch (HttpClientErrorException e) {
			throw new JobStateManagerException(e.getResponseBodyAsString(), e);
		} catch (Exception e) {
			throw new JobStateManagerException(e);
		}

	}

	private HttpHeaders createHttpHeaders() {

		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", "Bearer" + " " + authToken);
		return headers;

	}

}
