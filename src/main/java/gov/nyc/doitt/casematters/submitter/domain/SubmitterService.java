package gov.nyc.doitt.casematters.submitter.domain;

import java.sql.Timestamp;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import gov.nyc.doitt.casematters.submitter.domain.cmii.CmiiSubmissionService;
import gov.nyc.doitt.casematters.submitter.domain.cmii.model.CmiiSubmission;
import gov.nyc.doitt.casematters.submitter.domain.cmii.model.CmiiSubmissionSubmitterStatus;
import gov.nyc.doitt.casematters.submitter.domain.lm.LmSubmissionService;
import gov.nyc.doitt.casematters.submitter.domain.lm.model.LmSubmission;

@Component
public class SubmitterService {

	private Logger logger = LoggerFactory.getLogger(SubmitterService.class);

	@Autowired
	private CmiiSubmissionService cmiiSubmissionService;

	@Autowired
	private LmSubmissionService lmSubmissionService;

	@Autowired
	private CmiiToLmMapper cmiiToLmMapper;

	public void submitBatch() {

		logger.debug("submitBatch: entering");

		List<CmiiSubmission> cmiiSubmissionList = cmiiSubmissionService.getNextBatch();
		cmiiSubmissionList.forEach(p -> submitOne(p));

		logger.debug("submitBatch: exiting");
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
			cmiiSubmission.setCmiiSubmissionSubmitterStatus(CmiiSubmissionSubmitterStatus.COMPLETED);
		} else {
			cmiiSubmission.setCmiiSubmissionSubmitterStatus(CmiiSubmissionSubmitterStatus.ERROR);
			cmiiSubmission.incrementSubmitterErrorCount();
		}

		if (logger.isDebugEnabled()) {
			logger.debug("setSubmissionResult: {}", cmiiSubmission.toSubmissionResult());
		}

	}
}
