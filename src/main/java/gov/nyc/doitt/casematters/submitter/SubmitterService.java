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
	private JobStateManagerAccessor JobFlowManagerAccessor;

	@Autowired
	private LmSubmissionService lmSubmissionService;

	@Autowired
	private CmiiToLmMapper cmiiToLmMapper;

	@Scheduled(cron = "${submitter.cron}")
	public void submitBatch() {

		logger.info("submitBatch: entering");
		List<JobStateDto> jobStateDtos = new ArrayList<>();

		getNextBatch().forEach(p -> jobStateDtos.add(submitOne(p)));

		if (!jobStateDtos.isEmpty()) {
			JobFlowManagerAccessor.updateJobResults(jobStateDtos);
		}
		logger.info("submitBatch: exiting");
	}

	private List<CmiiSubmission> getNextBatch() {

		List<JobStateDto> jobStateDtos = JobFlowManagerAccessor.getNextBatchOfJobFlows();

		logger.info("getNextBatch: number of jobStates found: {}", jobStateDtos.size());

		if (jobStateDtos.size() == 0) {
			return new ArrayList<CmiiSubmission>();
		}
		List<Long> jobIds = jobStateDtos.stream().map(p -> Long.parseLong(p.getJobId())).collect(Collectors.toList());
		return cmiiSubmissionService.getNextBatch(jobIds);
	}

	private JobStateDto submitOne(CmiiSubmission cmiiSubmission) {

		JobStateDto jobStateDto = new JobStateDto(String.valueOf(cmiiSubmission.getId()));
		logger.debug("submitOne: cmiiSubmission: {}", cmiiSubmission);
		try {
			LmSubmission lmSubmission = cmiiToLmMapper.fromCmii(cmiiSubmission);
			lmSubmissionService.processSubmission(lmSubmission);
			jobStateDto.setStatus(JobStatus.COMPLETED.toString());
		} catch (Exception e) {
			logger.error("Can't save submission to LawManager", e);
			jobStateDto.setStatus(JobStatus.ERROR.toString());
			jobStateDto.setErrorReason(e.getMessage());
		}
		return jobStateDto;
	}

}
