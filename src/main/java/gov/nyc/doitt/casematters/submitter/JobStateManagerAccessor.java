package gov.nyc.doitt.casematters.submitter;

import java.net.URI;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class JobStateManagerAccessor {

	private Logger logger = LoggerFactory.getLogger(JobStateManagerAccessor.class);

	@Value("${submitter.JobStateManagerAccessor.baseUrl}")
	private String baseUrl;

	@Value("${submitter.JobStateManagerAccessor.appId}")
	private String appId;

	@Autowired
	private RestTemplate restTemplate;

	/**
	 * Return next batch of job ids
	 * 
	 * @return
	 */
	public List<JobDto> getNextBatchOfJobs() {

		String getJobsBatchUrl = String.format("%s/jobs/%s?nextBatch=true", baseUrl, appId);
		try {
			ResponseEntity<JobDto[]> response = restTemplate.getForEntity(new URI(getJobsBatchUrl), JobDto[].class);
			List<JobDto> jobDtos = Arrays.asList(response.getBody());
			return jobDtos;
		} catch (Exception e) {
			throw new JobStateManagerException(e);
		}
	}

	public List<JobDto> updateJobResults(List<JobDto> jobDtos) {

		String updateJobsUrl = String.format("%s/jobs/%s?nextBatch=true", baseUrl, appId);
		try {
			JobDto[] responseJobDtos = restTemplate.patchForObject(new URI(updateJobsUrl), jobDtos.toArray(),
					JobDto[].class);
			return Arrays.asList(responseJobDtos);
		} catch (Exception e) {
			throw new JobStateManagerException(e);
		}

	}

}
