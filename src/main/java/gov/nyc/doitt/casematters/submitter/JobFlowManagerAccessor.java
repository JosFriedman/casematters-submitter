package gov.nyc.doitt.casematters.submitter;

import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
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
	public List<String> getNextBatch() {

		String getJobFlowsBatchUrl = String.format("%s/jobFlows/batches/%s", baseUrl, appId);
		try {
			HttpHeaders headers = new HttpHeaders();
			HttpEntity<String> entity = new HttpEntity<>(headers);
			ResponseEntity<JobFlowDto[]> response = restTemplate.getForEntity(new URI(getJobFlowsBatchUrl), JobFlowDto[].class);
			List<JobFlowDto> jobFlowDtos = Arrays.asList(response.getBody());
			return jobFlowDtos.stream().map(p -> p.getJobId()).collect(Collectors.toList());
		} catch (Exception e) {
			throw new CmiiSubmitterException(e);
		}

	}

}
