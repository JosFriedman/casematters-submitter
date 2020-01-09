package gov.nyc.doitt.casematters.submitter.lm;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import gov.nyc.doitt.casematters.submitter.lm.model.LmAttachmentPath;
import gov.nyc.doitt.casematters.submitter.lm.model.LmSubmission;

@Component
public class LmAttachmentConfig {

	private Logger logger = LoggerFactory.getLogger(LmAttachmentConfig.class);

	@Autowired
	private LmAttachmentPathRepository lmAttachmentPathRepository;

	private Map<String, String> lmAttachmentPathMap;

	private void initMap() {

		if (lmAttachmentPathMap == null) {
			List<LmAttachmentPath> lmAttachmentPaths = lmAttachmentPathRepository.findAll();
			if (lmAttachmentPaths.isEmpty()) {
				throw new IllegalStateException("Can't initialize lmAttachmentPathMap");
			}
			lmAttachmentPathMap = lmAttachmentPaths.stream()
					.collect(Collectors.toMap(p -> p.getAgencyAbbreviation(), p -> p.getAttachmentPath()));
		}
	}

	public synchronized void setLawManagerCaseDirectory(LmSubmission lmSubmission) {

		initMap();

		if (!lmAttachmentPathMap.containsKey(lmSubmission.getAgencyAbbreviation())) {
			throw new UnsupportedOperationException("Not supported for agency: #" + lmSubmission.getAgencyAbbreviation() + "#");
		}
		String lawManagerCaseDirectory = String.format("%s/%s", lmAttachmentPathMap.get(lmSubmission.getAgencyAbbreviation()),
				lmSubmission.getSubmissionID());
		lmSubmission.setLawManagerCaseDirectory(lawManagerCaseDirectory);
	}
}
