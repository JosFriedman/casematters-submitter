package gov.nyc.doitt.casematters.submitter.domain.lm;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class LmSubmissionService {

	private Logger logger = LoggerFactory.getLogger(LmSubmissionService.class);

	@Autowired
	private LmSubmissionDataRepository lmSubmissionDataRepository;

	@Transactional("lmTransactionManager")
	public boolean saveSubmissions(List<LmSubmissionData> submissionDataList) {

		try {
			lmSubmissionDataRepository.saveAll(submissionDataList);
			return true;
		} catch (Exception e) {
			logger.error("Can't save submission to LawManager", e);
			return false;
		}
	}

}
