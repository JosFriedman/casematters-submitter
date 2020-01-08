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
import gov.nyc.doitt.casematters.submitter.job.JobDto;
import gov.nyc.doitt.casematters.submitter.job.JobState;
import gov.nyc.doitt.casematters.submitter.job.JobStateManagerAccessor;
import gov.nyc.doitt.casematters.submitter.lm.LmSubmissionService;
import gov.nyc.doitt.casematters.submitter.lm.model.LmSubmission;

@Component
public class SubmitterService {

	private Logger logger = LoggerFactory.getLogger(SubmitterService.class);

	@Autowired
	private CmiiSubmissionService cmiiSubmissionService;

	@Autowired
	private JobStateManagerAccessor jobStateManagerAccessor;

	@Autowired
	private LmSubmissionService lmSubmissionService;

	@Autowired
	private CmiiToLmMapper cmiiToLmMapper;

	@Scheduled(cron = "${submitter.cron}")
	public void submitBatch() {

		logger.info("submitBatch: entering");
		List<JobDto> jobDtos = new ArrayList<>();

		getNextBatch().forEach(p -> jobDtos.add(submitOne(p)));

		if (!jobDtos.isEmpty()) {
			jobStateManagerAccessor.updateJobResults(jobDtos);
		}
		logger.info("submitBatch: exiting");
	}

	private List<CmiiSubmission> getNextBatch() {

		List<JobDto> jobDtos = jobStateManagerAccessor.getNextBatchOfJobs();

		logger.info("getNextBatch: number of jobs found: {}", jobDtos.size());

		if (jobDtos.size() == 0) {
			return new ArrayList<CmiiSubmission>();
		}
		List<Long> jobIds = jobDtos.stream().map(p -> Long.parseLong(p.getJobId())).collect(Collectors.toList());
		return cmiiSubmissionService.getSubmissions(jobIds);
	}

	private JobDto submitOne(CmiiSubmission cmiiSubmission) {

		JobDto jobDto = new JobDto(String.valueOf(cmiiSubmission.getId()));
		logger.debug("submitOne: cmiiSubmission: {}", cmiiSubmission);
		try {
			LmSubmission lmSubmission = cmiiToLmMapper.fromCmii(cmiiSubmission);
			lmSubmissionService.processSubmission(lmSubmission);
			jobDto.setState(JobState.COMPLETED.toString());
		} catch (Exception e) {
			logger.error("Can't save submission to LawManager", e);
			jobDto.setState(JobState.ERROR.toString());
			jobDto.setErrorReason(e.getMessage());
		}
		return jobDto;
	}

}
