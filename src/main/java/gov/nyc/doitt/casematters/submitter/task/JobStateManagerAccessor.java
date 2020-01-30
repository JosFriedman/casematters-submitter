package gov.nyc.doitt.casematters.submitter.task;

import java.net.URI;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class JobStateManagerAccessor {

	private Logger logger = LoggerFactory.getLogger(JobStateManagerAccessor.class);

	@Value("${submitter.JobStateManagerAccessor.baseUrl}")
	private String baseUrl;

	@Value("${submitter.JobStateManagerAccessor.appName}")
	private String appName;

	@Value("${submitter.JobStateManagerAccessor.taskName}")
	private String taskName;

	@Autowired
	private RestTemplate restTemplate;

	/**
	 * Return next batch of task ids
	 * 
	 * @return
	 */
	public List<TaskDto> startNextBatchOfTasks() {

		String getTasksBatchUrl = String.format("%s/tasks/%s?taskName=%s", baseUrl, appName, taskName);
		try {
			TaskDto[] response = restTemplate.postForObject(new URI(getTasksBatchUrl), null, TaskDto[].class);
			List<TaskDto> taskDtos = Arrays.asList(response);
			return taskDtos;
		} catch (Exception e) {
			throw new JobStateManagerException(e);
		}
	}

	public void updateTaskResults(List<TaskDto> taskDtos) {

		String updateTasksUrl = String.format("%s/tasks/%s?taskName=%s", baseUrl, appName, taskName);
		try {
			restTemplate.put(new URI(updateTasksUrl), taskDtos.toArray());
		} catch (Exception e) {
			throw new JobStateManagerException(e);
		}

	}

}
