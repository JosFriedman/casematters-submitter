package gov.nyc.doitt.casematters.submitter.domain.lm;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class DataWriter {

	@Autowired
	private LmSubmissionDataRepository lmSubmissionDataRepository;

	@Transactional("lmTransactionManager")
	public boolean saveSubmissions(List<LmSubmissionData> submissionDataList) {

		lmSubmissionDataRepository.saveAll(submissionDataList);
		return true;
	}

}
