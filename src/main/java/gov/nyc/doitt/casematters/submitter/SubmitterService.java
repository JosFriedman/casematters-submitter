package gov.nyc.doitt.casematters.submitter;

import java.sql.Timestamp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import gov.nyc.doitt.casematters.submitter.cmii.CmiiSubmissionService;
import gov.nyc.doitt.casematters.submitter.cmii.SubmitterConcurrencyException;
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

	@Scheduled(cron = "${submitter.cron}")
	public void submitBatch() {

		logger.info("submitBatch: entering");
		try {
			cmiiSubmissionService.getNextBatch().forEach(p -> submitOne(p));
		} catch (SubmitterConcurrencyException e) {
			logger.warn("Another instance has picked up one or more of the submissions; no processing to do.");
		} finally {
			logger.info("submitBatch: exiting");
		}
	}

	private void submitOne(CmiiSubmission cmiiSubmission) {

		logger.debug("submitOne: cmiiSubmission: {}", cmiiSubmission);

		try {
			LmSubmission lmSubmission = cmiiToLmMapper.fromCmii(cmiiSubmission);
			lmSubmissionService.processSubmission(lmSubmission);
			cmiiSubmission.setCmiiSubmitterStatus(CmiiSubmitterStatus.COMPLETED);
		} catch (Exception e) {
			logger.error("Can't save submission to LawManager", e);
			cmiiSubmission.setCmiiSubmitterStatus(CmiiSubmitterStatus.ERROR);
			cmiiSubmission.incrementSubmitterErrorCount();
		}

		cmiiSubmission.setSubmitterEndTimestamp(new Timestamp(System.currentTimeMillis()));
		cmiiSubmissionService.updateCmiiSubmission(cmiiSubmission);
	}

}
