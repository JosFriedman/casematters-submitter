package gov.nyc.doitt.casematters.submitter.cmii;

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
import org.springframework.transaction.annotation.Transactional;

import gov.nyc.doitt.casematters.submitter.cmii.model.CmiiSubmission;
import gov.nyc.doitt.casematters.submitter.cmii.model.CmiiSubmitterStatus;

@Component
public class CmiiSubmissionService {

	private Logger logger = LoggerFactory.getLogger(CmiiSubmissionService.class);

	@Autowired
	private CmiiSubmissionRepository cmiiSubmissionRepository;

	@Value(" ${submitter.maxBatchSize}")
	private int maxBatchSize;

	@Value(" ${submitter.maxRetriesForError}")
	private int maxRetriesForError;

	private PageRequest pageRequest;

	@PostConstruct
	private void postConstruct() {
		pageRequest = PageRequest.of(0, maxBatchSize, Sort.by(Sort.Direction.ASC, "submitted"));
	}

	/**
	 * Return next batch of submissions
	 * 
	 * @return
	 */
	@Transactional(transactionManager = "cmiiTransactionManager")
	public List<CmiiSubmission> getNextBatch()  { 

		try {
			List<CmiiSubmission> cmiiSubmissions = cmiiSubmissionRepository
					.findByCmiiSubmitterStatusInAndSubmitterErrorCountLessThan(
							Arrays.asList(new CmiiSubmitterStatus[]{CmiiSubmitterStatus.NEW, CmiiSubmitterStatus.ERROR}),
							maxRetriesForError + 1, pageRequest);
			logger.info("getNextBatch: number of submissions found: {}", cmiiSubmissions.size());

			// mark each submission as picked up for processing
			cmiiSubmissions.forEach(p -> {
				p.setCmiiSubmitterStatus(CmiiSubmitterStatus.PROCESSING);
				p.setSubmitterStartTimestamp(new Timestamp(System.currentTimeMillis()));
				updateCmiiSubmission(p);
			});
			return cmiiSubmissions;

		} catch (Exception e) {
			throw new SubmitterConcurrencyException(e);
		}

	}

	@Transactional("cmiiTransactionManager")
	public void updateCmiiSubmission(CmiiSubmission cmiiSubmission) {

		cmiiSubmissionRepository.save(cmiiSubmission);
	}

}
