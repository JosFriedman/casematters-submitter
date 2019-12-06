package gov.nyc.doitt.casematters.submitter.cmii;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import gov.nyc.doitt.casematters.submitter.JobFlowManagerAccessor;
import gov.nyc.doitt.casematters.submitter.cmii.model.CmiiSubmission;
import gov.nyc.doitt.casematters.submitter.cmii.model.CmiiSubmitterStatus;

@Component
public class CmiiSubmissionService {

	private Logger logger = LoggerFactory.getLogger(CmiiSubmissionService.class);

	@Autowired
	private CmiiSubmissionRepository cmiiSubmissionRepository;

	@Autowired
	private JobFlowManagerAccessor JobFlowManagerAccessor;

	/**
	 * Return next batch of submissions
	 * 
	 * @return
	 */
	@Transactional(transactionManager = "cmiiTransactionManager")
	public List<CmiiSubmission> getNextBatch() {

		List<String> jobIds = JobFlowManagerAccessor.getNextBatch();
		logger.info("getNextBatch: number of submissions found: {}", jobIds.size());

		List<Long> ids = jobIds.stream().map(p -> Long.parseLong(p)).collect(Collectors.toList());
		List<CmiiSubmission> cmiiSubmissions = cmiiSubmissionRepository.getByIdIn(ids);
		return cmiiSubmissions;

	}

	@Transactional("cmiiTransactionManager")
	public void updateCmiiSubmission(CmiiSubmission cmiiSubmission) {

	}

}
