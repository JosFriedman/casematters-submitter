package gov.nyc.doitt.casematters.submitter.cmii;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import gov.nyc.doitt.casematters.submitter.cmii.model.CmiiSubmission;

@Component
public class CmiiSubmissionService {

	private Logger logger = LoggerFactory.getLogger(CmiiSubmissionService.class);

	@Autowired
	private CmiiSubmissionRepository cmiiSubmissionRepository;

	/**
	 * Return next batch of submissions
	 * 
	 * @return
	 */
	@Transactional(transactionManager = "cmiiTransactionManager")
	public List<CmiiSubmission> getNextBatch(List<Long> ids) {

		List<CmiiSubmission> cmiiSubmissions = cmiiSubmissionRepository.getByIdIn(ids);
		return cmiiSubmissions;
	}

}
