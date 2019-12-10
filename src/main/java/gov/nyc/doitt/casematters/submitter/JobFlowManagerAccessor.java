package gov.nyc.doitt.casematters.submitter;

import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import gov.nyc.doitt.casematters.submitter.cmii.CmiiSubmitterException;

@Component
public class JobFlowManagerAccessor {

	private Logger logger = LoggerFactory.getLogger(JobFlowManagerAccessor.class);

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
	public List<JobFlowDto> getNextBatchOfJobFlows() {

		String getJobFlowsBatchUrl = String.format("%s/jobFlows/%s?nextBatch=true", baseUrl, appId);
		try {
//			ResponseEntity<String[]> response = restTemplate.getForEntity(new URI(getJobFlowIdsUrl), String[].class);
			ResponseEntity<JobFlowDto[]> response = restTemplate.getForEntity(new URI(getJobFlowsBatchUrl), JobFlowDto[].class);
			List<JobFlowDto> jobFlowDtos = Arrays.asList(response.getBody());
			return jobFlowDtos;
		} catch (Exception e) {
			throw new CmiiSubmitterException(e);
		}
	}

	public List<JobFlowDto> updateJobResults(List<JobFlowDto> jobFlowDtos) {

		String updateJobFlowsUrl = String.format("%s/jobFlows/%s?nextBatch=true", baseUrl, appId);
		try {
			JobFlowDto[] responseJobFlowDtos = restTemplate.patchForObject(new URI(updateJobFlowsUrl), jobFlowDtos.toArray(),
					JobFlowDto[].class);
			return Arrays.asList(responseJobFlowDtos);
		} catch (Exception e) {
			throw new CmiiSubmitterException(e);
		}

	}

}
