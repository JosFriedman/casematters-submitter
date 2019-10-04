package gov.nyc.doitt.casematters.submitter.lm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import gov.nyc.doitt.casematters.submitter.lm.model.LmSubmission;

@Component
public class LmSubmissionService {

	private Logger logger = LoggerFactory.getLogger(LmSubmissionService.class);

	@Autowired
	private LmSubmissionRepository lmSubmissionRepository;

	@Autowired
	private LmAttachmentUploader lmAttachmentUploader;

	@Transactional("lmTransactionManager")
	public void processSubmission(LmSubmission lmSubmission) {

		logger.debug("processSubmission: lmSubmission: {}", lmSubmission);

		if (lmSubmission.hasLmSubmissionAttachments()) {
			lmAttachmentUploader.upload(lmSubmission);
		}
		lmSubmissionRepository.save(lmSubmission);

		// do 'no-harm' update to force the update triggers to fire
		lmSubmission.setMessageID(0);

		if (lmSubmission.hasLmSubmissionAttachments()) {
			// fileMoved==true needed for attachments to be seen by update trigger's stored proc
			lmSubmission.getLmSubmissionAttachments().forEach(p -> p.setFileMoved(true));
		}
		lmSubmissionRepository.save(lmSubmission);
	}
}
