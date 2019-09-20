package gov.nyc.doitt.casematters.submitter.domain.cmii;

import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import gov.nyc.doitt.casematters.submitter.domain.cmii.model.CmiiSubmission;
import gov.nyc.doitt.casematters.submitter.domain.cmii.model.CmiiSubmissionSubmitterStatus;

@Component
public class CmiiSubmissionService {

	private Logger logger = LoggerFactory.getLogger(CmiiSubmissionService.class);

	@Autowired
	private CmiiSubmissionRepository cmiiSubmissionRepository;

	@Transactional("cmiiTransactionManager")
	public List<CmiiSubmission> getNextBatch() {

		List<CmiiSubmission> submissions = cmiiSubmissionRepository.findByCmiiSubmissionSubmitterStatusIn(Arrays.asList(
				new CmiiSubmissionSubmitterStatus[]{CmiiSubmissionSubmitterStatus.NEW, CmiiSubmissionSubmitterStatus.ERROR}));

		logger.debug("getNextBatch: number of submissions found: {}", submissions.size());

		return submissions;
	}

	@Transactional("cmiiTransactionManager")
	public void updateCmiiSubmission(CmiiSubmission cmiiSubmission) {

		cmiiSubmissionRepository.save(cmiiSubmission);
	}

}
