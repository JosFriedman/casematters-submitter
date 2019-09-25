package gov.nyc.doitt.casematters.submitter.lm;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import gov.nyc.doitt.casematters.submitter.lm.model.LmSubmission;

@Component
public class LmSubmissionMockerUpper {

	public List<LmSubmission> createList(int listSize) throws Exception {

		List<LmSubmission> lmSubmissions = new ArrayList<>();
		for (int i = 0; i < listSize; i++) {
			lmSubmissions.add(create(i));
		}
		return lmSubmissions;
	}

	public LmSubmission create(int i) throws Exception {

		LmSubmission lmSubmission = new LmSubmission();
		return lmSubmission;
	}
}
