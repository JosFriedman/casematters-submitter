package gov.nyc.doitt.casematters.submitter.domain;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import gov.nyc.doitt.casematters.submitter.domain.cmii.CmiiSubmission;
import gov.nyc.doitt.casematters.submitter.domain.cmii.CmiiSubmissionService;
import gov.nyc.doitt.casematters.submitter.domain.cmii.CmiiSubmissionState;
import gov.nyc.doitt.casematters.submitter.domain.lm.LmSubmissionService;
import gov.nyc.doitt.casematters.submitter.domain.lm.LmSubmissionData;

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

		List<LmSubmissionData> lmSubmissionDataList = cmiiToLmMapper.fromCmii(cmiiSubmission.getSubmissionDataList());
		boolean saved = lmSubmissionService.saveSubmissions(lmSubmissionDataList);
		cmiiSubmission.setSubmissionState(saved ? CmiiSubmissionState.COMPLETED : CmiiSubmissionState.ERROR);
		cmiiSubmissionService.updateCmiiSubmission(cmiiSubmission);
	}

}
