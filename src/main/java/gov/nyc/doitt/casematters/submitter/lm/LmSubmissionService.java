package gov.nyc.doitt.casematters.submitter.lm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import gov.nyc.doitt.casematters.submitter.lm.model.LmSubmission;

@Component
public class LmSubmissionService {

	private Logger logger = LoggerFactory.getLogger(LmSubmissionService.class);

	@Autowired
	private LmSubmissionRepository lmSubmissionRepository;

	@Transactional("lmTransactionManager")
	public void saveSubmission(LmSubmission lmSubmission) {

		logger.debug("saveSubmission: lmSubmission: {}", lmSubmission);
		
		lmSubmissionRepository.save(lmSubmission);

		// do an update to force the update trigger to fire
		lmSubmission.setMessageID(0);
		lmSubmissionRepository.save(lmSubmission);
	}

}
