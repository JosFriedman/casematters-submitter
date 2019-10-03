package gov.nyc.doitt.casematters.submitter.lm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import gov.nyc.doitt.casematters.submitter.lm.model.LmSubmission;

@Component
public class LmAttachmentConfig {

	private Logger logger = LoggerFactory.getLogger(LmAttachmentConfig.class);

	@Autowired
	private SmbConfig smbConfig;

	public void setLawManagerCaseDirectory(LmSubmission lmSubmission) {

		String lawManagerCaseDirectory = null;
		if (lmSubmission.getAgencyAbbreviation().equals("OATH")) {
			lawManagerCaseDirectory = getLmCaseDirectory_OATH(lmSubmission);
		} else if (lmSubmission.getAgencyAbbreviation().equals("OCB")) {
			lawManagerCaseDirectory = getLmCaseDirectory_OCB(lmSubmission);
		} else {
			throw new UnsupportedOperationException("Not supported for agency: #" + lmSubmission.getAgency() + "#");
		}

		lmSubmission.setLawManagerCaseDirectory(lawManagerCaseDirectory);
	}

	private String getLmCaseDirectory_OATH(LmSubmission lmSubmission) {
		String dir = "cm_dev_oath_fs";
		String subDir = "" + lmSubmission.getSubmissionID();
		return String.format("//%s/%s/%s/", smbConfig.getSmbServer(), dir, subDir);
	}

	private String getLmCaseDirectory_OCB(LmSubmission lmSubmission) {
		String dir = "cm_dev_ocb_fs";
		String subDir = "" + lmSubmission.getSubmissionID();
		return String.format("//%s/%s/%s/", smbConfig.getSmbServer(), dir, subDir);
	}
}
