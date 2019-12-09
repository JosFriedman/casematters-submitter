package gov.nyc.doitt.casematters.submitter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import gov.nyc.doitt.casematters.submitter.cmii.CmiiSubmissionService;
import gov.nyc.doitt.casematters.submitter.cmii.model.CmiiSubmission;
import gov.nyc.doitt.casematters.submitter.lm.LmSubmissionService;
import gov.nyc.doitt.casematters.submitter.lm.model.LmSubmission;

@Component
public class SubmitterService {

	private Logger logger = LoggerFactory.getLogger(SubmitterService.class);

	@Autowired
	private CmiiSubmissionService cmiiSubmissionService;

	@Autowired
	private JobFlowManagerAccessor JobFlowManagerAccessor;

	@Autowired
	private LmSubmissionService lmSubmissionService;

	@Autowired
	private CmiiToLmMapper cmiiToLmMapper;

	@Scheduled(cron = "${submitter.cron}")
	public void submitBatch() {

		logger.info("submitBatch: entering");
		List<JobFlowDto> jobFlowDtos = new ArrayList<>();

		getNextBatch().forEach(p -> jobFlowDtos.add(submitOne(p)));

		if (!jobFlowDtos.isEmpty()) {
			JobFlowManagerAccessor.updateJobResults(jobFlowDtos);
		}
		logger.info("submitBatch: exiting");
	}

	private List<CmiiSubmission> getNextBatch() {

		List<String> jobIdStrs = JobFlowManagerAccessor.getNextBatchOfJobIds();
		logger.info("getNextBatch: number of jobIdStrs found: {}", jobIdStrs.size());
		List<Long> jobIds = jobIdStrs.stream().map(p -> Long.parseLong(p)).collect(Collectors.toList());
		return cmiiSubmissionService.getNextBatch(jobIds);
	}

	private JobFlowDto submitOne(CmiiSubmission cmiiSubmission) {

		JobFlowDto jobFlowDto = new JobFlowDto(String.valueOf(cmiiSubmission.getId()));
		logger.debug("submitOne: cmiiSubmission: {}", cmiiSubmission);
		try {
			LmSubmission lmSubmission = cmiiToLmMapper.fromCmii(cmiiSubmission);
			lmSubmissionService.processSubmission(lmSubmission);
			jobFlowDto.setStatus(JobStatus.COMPLETED.toString());
		} catch (Exception e) {
			logger.error("Can't save submission to LawManager", e);
			jobFlowDto.setStatus(JobStatus.ERROR.toString());
			jobFlowDto.setErrorReason(e.getMessage());
		}
		return jobFlowDto;
	}

}
