package gov.nyc.doitt.casematters.submitter.domain.cmii;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import gov.nyc.doitt.casematters.submitter.domain.cmii.model.CmiiSubmission;
import gov.nyc.doitt.casematters.submitter.domain.cmii.model.CmiiSubmissionSubmitterStatus;

@Component
public class CmiiSubmissionService {

	private Logger logger = LoggerFactory.getLogger(CmiiSubmissionService.class);

	@Autowired
	private CmiiSubmissionRepository cmiiSubmissionRepository;

	@Value(" ${submitter.cmii.maxBatchSize:2}")
	private int maxBatchSize;

	private PageRequest pageRequest;

	@PostConstruct
	private void postConstruct() {
		pageRequest = PageRequest.of(0, maxBatchSize, Sort.by(Sort.Direction.ASC, "submitted"));
	}

	@Transactional(transactionManager = "cmiiTransactionManager")
	public List<CmiiSubmission> getNextBatch() {

		try {
			List<CmiiSubmission> cmiiSubmissions = cmiiSubmissionRepository.findByCmiiSubmissionSubmitterStatusIn(Arrays.asList(
					new CmiiSubmissionSubmitterStatus[]{CmiiSubmissionSubmitterStatus.NEW, CmiiSubmissionSubmitterStatus.ERROR}),
					pageRequest);
			logger.debug("getNextBatch: number of submissions found: {}", cmiiSubmissions.size());

			cmiiSubmissions.forEach(p -> {
				p.setCmiiSubmissionSubmitterStatus(CmiiSubmissionSubmitterStatus.PROCESSING);
				p.setSubmitterStartTimestamp(new Timestamp(System.currentTimeMillis()));
				updateCmiiSubmission(p);
			});

			return cmiiSubmissions;

		} catch (ObjectOptimisticLockingFailureException e) {
			logger.warn("Another instance has picked up one or more of the submissions");
			return Collections.emptyList();
		}
	}

	@Transactional("cmiiTransactionManager")
	public void updateCmiiSubmission(CmiiSubmission cmiiSubmission) {

		cmiiSubmissionRepository.save(cmiiSubmission);
	}

}
