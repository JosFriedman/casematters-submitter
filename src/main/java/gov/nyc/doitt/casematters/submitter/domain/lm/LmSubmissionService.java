package gov.nyc.doitt.casematters.submitter.domain.lm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import gov.nyc.doitt.casematters.submitter.domain.lm.model.LmSubmission;

@Component
public class LmSubmissionService {

	private Logger logger = LoggerFactory.getLogger(LmSubmissionService.class);

	@Autowired
	private LmSubmissionRepository lmSubmissionRepository;

	@Transactional("lmTransactionManager")
	public boolean saveSubmission(LmSubmission submission) {

		try {
			lmSubmissionRepository.save(submission);
			
			// do an update to force the update trigger to fire
			submission.setMessageID(0);
			lmSubmissionRepository.save(submission);
			
			return true;
		} catch (Exception e) {
			logger.error("Can't save submission to LawManager", e);
			return false;
		}
	}

}
