package gov.nyc.doitt.casematters.submitter.domain.cmii;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import gov.nyc.doitt.casematters.submitter.domain.cmii.model.CmiiSubmission;
import gov.nyc.doitt.casematters.submitter.domain.cmii.model.CmiiSubmissionSubmitterStatus;

@Component
public class CmiiSubmissionService {

	private Logger logger = LoggerFactory.getLogger(CmiiSubmissionService.class);

	@Autowired
	private CmiiSubmissionRepository cmiiSubmissionRepository;

	@Autowired
	private SubmitterSynchronizer submitterSynchronizer;

	@Value(" ${submitter.cmii.maxBatchSize:2}")
	private int maxBatchSize;

	private PageRequest pageRequest;

	@PostConstruct
	private void postConstruct() {
		pageRequest = PageRequest.of(0, maxBatchSize, Sort.by(Sort.Direction.ASC, "submitted"));
	}

	@Transactional(transactionManager = "cmiiTransactionManager")
	public List<CmiiSubmission> getNextBatch() {

		List<CmiiSubmission> cmiiSubmissions = cmiiSubmissionRepository.findByCmiiSubmissionSubmitterStatusIn(Arrays.asList(
				new CmiiSubmissionSubmitterStatus[]{CmiiSubmissionSubmitterStatus.NEW, CmiiSubmissionSubmitterStatus.ERROR}), pageRequest);
		logger.debug("getNextBatch: number of submissions found: {}", cmiiSubmissions.size());

//		List<CmiiSubmission> markedCmiiSubmissions = submitterSynchronizer.markBatch(this, cmiiSubmissions);
//		logger.debug("getNextBatch: number of submissions marked: {}", markedCmiiSubmissions.size());
//
//		return markedCmiiSubmissions;
		
		cmiiSubmissions.forEach(p -> {
			p.setCmiiSubmissionSubmitterStatus(CmiiSubmissionSubmitterStatus.PROCESSING);
			p.setSubmitterStartTimestamp(new Timestamp(System.currentTimeMillis()));
			updateCmiiSubmission(p);
		});

		return cmiiSubmissions;
	}

	@Transactional("cmiiTransactionManager")
	public void updateCmiiSubmission(CmiiSubmission cmiiSubmission) {

		cmiiSubmissionRepository.save(cmiiSubmission);
	}

}

@Component
class SubmitterSynchronizer {

	private Logger logger = LoggerFactory.getLogger(SubmitterSynchronizer.class);

	@Transactional(transactionManager = "cmiiTransactionManager", propagation = Propagation.REQUIRES_NEW)
	public List<CmiiSubmission> markBatch(CmiiSubmissionService cmiiSubmissionService, List<CmiiSubmission> cmiiSubmissions) {

		cmiiSubmissions.forEach(p -> {
			p.setCmiiSubmissionSubmitterStatus(CmiiSubmissionSubmitterStatus.PROCESSING);
			p.setSubmitterStartTimestamp(new Timestamp(System.currentTimeMillis()));
			cmiiSubmissionService.updateCmiiSubmission(p);
		});
		return cmiiSubmissions;
	}

}
