package gov.nyc.doitt.casematters.submitter;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import gov.nyc.doitt.casematters.submitter.cmii.model.CmiiAgency;
import gov.nyc.doitt.casematters.submitter.cmii.model.CmiiForm;
import gov.nyc.doitt.casematters.submitter.cmii.model.CmiiFormVersion;
import gov.nyc.doitt.casematters.submitter.cmii.model.CmiiSubmission;
import gov.nyc.doitt.casematters.submitter.cmii.model.CmiiSubmissionAttachment;
import gov.nyc.doitt.casematters.submitter.cmii.model.CmiiSubmissionData;
import gov.nyc.doitt.casematters.submitter.cmii.model.CmiiUser;
import gov.nyc.doitt.casematters.submitter.lm.model.LmSubmission;
import gov.nyc.doitt.casematters.submitter.lm.model.LmSubmissionAttachment;
import gov.nyc.doitt.casematters.submitter.lm.model.LmSubmissionData;

/**
 * Map CmiiSubmission to LmSubmission
 * 
 */
@Component
public class CmiiToLmMapper {

	private Logger logger = LoggerFactory.getLogger(CmiiToLmMapper.class);

	public List<LmSubmission> fromCmii(List<CmiiSubmission> cmiiSubmissions) {

		return cmiiSubmissions.stream().map(p -> fromCmii(p)).collect(Collectors.toList());
	}

	public LmSubmission fromCmii(CmiiSubmission cmiiSubmission) {

		logger.debug("cmiiSubmission={}", cmiiSubmission);

		LmSubmission lmSubmission = new LmSubmission();

		CmiiUser cmmUser = cmiiSubmission.getCmiiUser();
		CmiiFormVersion cmiiFormVersion = cmiiSubmission.getCmiiFormVersion();
		CmiiForm cmiiForm = cmiiFormVersion.getCmiiForm();
		CmiiAgency cmiiAgency = cmiiForm.getCmiiAgency();

		lmSubmission.setMessageID(-1);
		lmSubmission.setSubmissionID((int) cmiiSubmission.getId());
		lmSubmission.setSubmissionParentID(cmiiSubmission.getParentId() != null ? cmiiSubmission.getParentId().intValue() : null);
		lmSubmission.setSubmissionTimestamp(cmiiSubmission.getSubmitted());
		lmSubmission.setSubmissionDescription(cmiiSubmission.getDescription());
		lmSubmission.setAgencyID((int) cmiiAgency.getId());
		lmSubmission.setAgency(cmiiAgency.getName());
		lmSubmission.setAgencyAbbreviation(cmiiAgency.getTag());
		lmSubmission.setFormID((int) cmiiForm.getId());
		lmSubmission.setFormName(cmiiForm.getName());
		lmSubmission.setFormAbbreviation(cmiiForm.getTag());
		lmSubmission.setFormVersionID((int) cmiiFormVersion.getId());
		lmSubmission.setFormVersion((int) cmiiFormVersion.getVersion());
		lmSubmission.setUserID((int) cmmUser.getId());
		lmSubmission.setUserFirstName(cmmUser.getFirstName());
		lmSubmission.setUserMiddleName(null);
		lmSubmission.setUserLastName(cmmUser.getLastName());
		lmSubmission.setUserPhone(cmmUser.getPrimaryPhone());
		lmSubmission.setUserFax(null);
		lmSubmission.setUserEmail(cmmUser.getEmail());

		lmSubmission.setLmSubmissionDataList(fromCmiiSubmissionDataList(cmiiSubmission.getCmiiSubmissionDataList()));
		lmSubmission.setLmSubmissionAttachments(fromCmiiSubmissionAttachments(cmiiSubmission.getCmiiSubmissionAttachments()));

		logger.debug("lmSubmission={}", lmSubmission);

		return lmSubmission;
	}

	private List<LmSubmissionData> fromCmiiSubmissionDataList(List<CmiiSubmissionData> cmiiSubmissionDataList) {

		return cmiiSubmissionDataList.stream().map(p -> fromCmii(p)).collect(Collectors.toList());
	}

	public LmSubmissionData fromCmii(CmiiSubmissionData cmiiSubmissionData) {

		logger.debug("cmiiSubmissionData={}", cmiiSubmissionData);

		LmSubmissionData lmSubmissionData = new LmSubmissionData((int) cmiiSubmissionData.getSubmissionId(),
				cmiiSubmissionData.getEntity());
		lmSubmissionData.setMessageId(-1);
		lmSubmissionData.setFieldValue(cmiiSubmissionData.getValue());

		logger.debug("lmSubmissionData={}", lmSubmissionData);

		return lmSubmissionData;
	}

	private List<LmSubmissionAttachment> fromCmiiSubmissionAttachments(
			List<CmiiSubmissionAttachment> cmiiSubmissionAttachmentList) {

		final AtomicInteger atomicInteger = new AtomicInteger(1);
		return cmiiSubmissionAttachmentList.stream().map(p -> fromCmii(p, atomicInteger.getAndIncrement()))
				.collect(Collectors.toList());
	}

	public LmSubmissionAttachment fromCmii(CmiiSubmissionAttachment cmiiSubmissionAttachment, int i) {

		logger.debug("cmiiSubmissionAttachment={}", cmiiSubmissionAttachment);

		LmSubmissionAttachment lmSubmissionAttachment = new LmSubmissionAttachment((int) cmiiSubmissionAttachment.getSubmissionId(),
				i);
		lmSubmissionAttachment.setTitle(FilenameUtils.getBaseName(cmiiSubmissionAttachment.getOriginalFileName()));
		lmSubmissionAttachment.setOriginalFileName(cmiiSubmissionAttachment.getOriginalFileName());
		lmSubmissionAttachment.setStandardizedFileName(cmiiSubmissionAttachment.getUniqueFileName());
		lmSubmissionAttachment.setLawManagerFileName(null);
		lmSubmissionAttachment.setExtension(FilenameUtils.getExtension(cmiiSubmissionAttachment.getOriginalFileName()));
		lmSubmissionAttachment.setContentType(cmiiSubmissionAttachment.getContentType());
		lmSubmissionAttachment.setHashSHA256(null);
		lmSubmissionAttachment.setFileSize(cmiiSubmissionAttachment.getFileSize());

		logger.debug("lmSubmissionAttachment={}", lmSubmissionAttachment);

		return lmSubmissionAttachment;
	}

}
