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

	@Value("${submitter.JobFlowManagerAccessor.baseUrl}")
	private String baseUrl;

	@Value("${submitter.JobFlowManagerAccessor.appId}")
	private String appId;

	@Autowired
	private RestTemplate restTemplate;

	/**
	 * Return next batch of job ids
	 * 
	 * @return
	 */
	public List<JobStateDto> getNextBatchOfJobFlows() {

		String getJobFlowsBatchUrl = String.format("%s/jobStates/%s?nextBatch=true", baseUrl, appId);
		try {
			ResponseEntity<JobStateDto[]> response = restTemplate.getForEntity(new URI(getJobFlowsBatchUrl), JobStateDto[].class);
			List<JobStateDto> jobStateDtos = Arrays.asList(response.getBody());
			return jobStateDtos;
		} catch (Exception e) {
			throw new JobStateManagerException(e);
		}
	}

	public List<JobStateDto> updateJobResults(List<JobStateDto> jobStateDtos) {

		String updateJobFlowsUrl = String.format("%s/jobStates/%s?nextBatch=true", baseUrl, appId);
		try {
			JobStateDto[] responseJobFlowDtos = restTemplate.patchForObject(new URI(updateJobFlowsUrl), jobStateDtos.toArray(),
					JobStateDto[].class);
			return Arrays.asList(responseJobFlowDtos);
		} catch (Exception e) {
			throw new JobStateManagerException(e);
		}

	}

}
