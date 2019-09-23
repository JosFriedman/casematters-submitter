package gov.nyc.doitt.casematters.submitter;

import java.sql.Timestamp;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import gov.nyc.doitt.casematters.submitter.cmii.CmiiSubmissionService;
import gov.nyc.doitt.casematters.submitter.cmii.model.CmiiSubmission;
import gov.nyc.doitt.casematters.submitter.cmii.model.CmiiSubmitterStatus;
import gov.nyc.doitt.casematters.submitter.lm.LmSubmissionService;
import gov.nyc.doitt.casematters.submitter.lm.model.LmSubmission;

@Component
public class SubmitterService {

	private Logger logger = LoggerFactory.getLogger(SubmitterService.class);

	@Autowired
	private CmiiSubmissionService cmiiSubmissionService;

	@Autowired
	private LmSubmissionService lmSubmissionService;

	@Autowired
	private CmiiToLmMapper cmiiToLmMapper;

	@Scheduled(cron = "${submitter.polling.cron}")
	public void submitBatch() {

		logger.debug("submitBatch: entering");
		try {
			cmiiSubmissionService.getNextBatch().forEach(p -> submitOne(p));
		} catch (ObjectOptimisticLockingFailureException e) {
			logger.warn("Another instance has picked up one or more of the submissions; no processing to do.");
		} finally {
			logger.debug("submitBatch: exiting");
		}
	}

	private void submitOne(CmiiSubmission cmiiSubmission) {

		logger.debug("submitOne: cmiiSubmission: {}", cmiiSubmission);

		LmSubmission lmSubmission = cmiiToLmMapper.fromCmii(cmiiSubmission);
		boolean saved = lmSubmissionService.saveSubmission(lmSubmission);
		setSubmissionResult(cmiiSubmission, saved);
		cmiiSubmissionService.updateCmiiSubmission(cmiiSubmission);
	}

	private void setSubmissionResult(CmiiSubmission cmiiSubmission, boolean saved) {

		cmiiSubmission.setSubmitterEndTimestamp(new Timestamp(System.currentTimeMillis()));
		if (saved) {
			cmiiSubmission.setCmiiSubmitterStatus(CmiiSubmitterStatus.COMPLETED);
		} else {
			cmiiSubmission.setCmiiSubmitterStatus(CmiiSubmitterStatus.ERROR);
			cmiiSubmission.incrementSubmitterErrorCount();
		}
		if (logger.isDebugEnabled()) {
			logger.debug("setSubmissionResult: {}", cmiiSubmission.toSubmissionResult());
		}

	}
}
