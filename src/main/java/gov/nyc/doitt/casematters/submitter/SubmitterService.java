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
import gov.nyc.doitt.casematters.submitter.task.JobStateManagerAccessor;
import gov.nyc.doitt.casematters.submitter.task.TaskDto;
import gov.nyc.doitt.casematters.submitter.task.TaskState;

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
		List<TaskDto> taskDtos = new ArrayList<>();

		getNextBatch().forEach(p -> taskDtos.add(submitOne(p)));

		if (!taskDtos.isEmpty()) {
			jobStateManagerAccessor.updateTaskResults(taskDtos);
		}
		logger.info("submitBatch: exiting");
	}

	private List<CmiiSubmission> getNextBatch() {

		List<TaskDto> taskDtos = jobStateManagerAccessor.startNextBatchOfTasks();

		logger.info("getNextBatch: number of tasks found: {}", taskDtos.size());
		if (taskDtos.size() == 0) {
			return new ArrayList<CmiiSubmission>();
		}
		if (logger.isDebugEnabled()) {
			taskDtos.forEach(p -> logger.debug("task: {}", p.toString()));
		}
		List<Long> jobIds = taskDtos.stream().map(p -> Long.parseLong(p.getJobId())).collect(Collectors.toList());
		return cmiiSubmissionService.getSubmissions(jobIds);
	}

	private TaskDto submitOne(CmiiSubmission cmiiSubmission) {

		TaskDto taskDto = new TaskDto(String.valueOf(cmiiSubmission.getId()));
		logger.debug("submitOne: cmiiSubmission: {}", cmiiSubmission);
		try {
			LmSubmission lmSubmission = cmiiToLmMapper.fromCmii(cmiiSubmission);
			lmSubmissionService.processSubmission(lmSubmission);
			taskDto.setState(TaskState.COMPLETED.toString());
		} catch (Exception e) {
			logger.error("Can't save submission to LawManager", e);
			taskDto.setState(TaskState.ERROR.toString());
			taskDto.setErrorReason(e.toString());
		}
		return taskDto;
	}

}
