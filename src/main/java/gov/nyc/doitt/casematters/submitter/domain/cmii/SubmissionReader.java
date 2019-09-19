package gov.nyc.doitt.casematters.submitter.domain.cmii;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class SubmissionReader {

	@Autowired
	private CmiiSubmissionRepository cmiiSubmissionRepository;

	@Transactional("cmiiTransactionManager")
	public List<CmiiSubmission> getNextBatch() {

		List<CmiiSubmission> submissions = cmiiSubmissionRepository
				.findBySubmissionStateIn(Arrays.asList(new SubmissionState[]{SubmissionState.NEW, SubmissionState.ERROR}));
		return submissions;

	}

}
