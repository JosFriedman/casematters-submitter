package gov.nyc.doitt.casematters.submitter;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.apache.commons.io.FilenameUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import gov.nyc.doitt.casematters.submitter.cmii.model.CmiiAgency;
import gov.nyc.doitt.casematters.submitter.cmii.model.CmiiForm;
import gov.nyc.doitt.casematters.submitter.cmii.model.CmiiFormVersion;
import gov.nyc.doitt.casematters.submitter.cmii.model.CmiiSubmission;
import gov.nyc.doitt.casematters.submitter.cmii.model.CmiiSubmissionAttachment;
import gov.nyc.doitt.casematters.submitter.cmii.model.CmiiSubmissionData;
import gov.nyc.doitt.casematters.submitter.cmii.model.CmiiSubmissionMockerUpper;
import gov.nyc.doitt.casematters.submitter.cmii.model.CmiiUser;
import gov.nyc.doitt.casematters.submitter.lm.model.LmSubmission;
import gov.nyc.doitt.casematters.submitter.lm.model.LmSubmissionAttachment;
import gov.nyc.doitt.casematters.submitter.lm.model.LmSubmissionData;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CmiiToLmMapperTest extends TestBase {

	@Autowired
	private CmiiSubmissionMockerUpper cmiiSubmissionMockerUpper;

	@Autowired
	private CmiiToLmMapper cmiiToLmMapper;

	@Test
	public void testFromCmiiList() throws Exception {

		int listSize = 5;
		List<CmiiSubmission> cmiiSubmissions = cmiiSubmissionMockerUpper.createList(listSize);

		List<LmSubmission> lmSubmissions = cmiiToLmMapper.fromCmii(cmiiSubmissions);

		assertEquals(listSize, lmSubmissions.size());

		for (int i = 0; i < listSize; i++) {
			CmiiSubmission cmiiSubmission = cmiiSubmissions.get(i);
			CmiiUser cmmUser = cmiiSubmission.getCmiiUser();
			CmiiFormVersion cmiiFormVersion = cmiiSubmission.getCmiiFormVersion();
			CmiiForm cmiiForm = cmiiFormVersion.getCmiiForm();
			CmiiAgency cmiiAgency = cmiiForm.getCmiiAgency();

			LmSubmission lmSubmission = lmSubmissions.get(i);

			assertEquals(cmiiSubmission.getId(), lmSubmission.getSubmissionID());
			assertEquals(cmiiSubmission.getParentId(), lmSubmission.getSubmissionParentID());
			assertEquals(cmiiSubmission.getSubmitted(), lmSubmission.getSubmissionTimestamp());
			assertEquals(cmiiSubmission.getDescription(), lmSubmission.getSubmissionDescription());
			assertEquals(cmiiAgency.getId(), lmSubmission.getAgencyID());
			assertEquals(cmiiAgency.getName(), lmSubmission.getAgency());
			assertEquals(cmiiAgency.getTag(), lmSubmission.getAgencyAbbreviation());
			assertEquals(cmiiForm.getId(), lmSubmission.getFormID());
			assertEquals(cmiiForm.getName(), lmSubmission.getFormName());
			assertEquals(cmiiForm.getTag(), lmSubmission.getFormAbbreviation());
			assertEquals(cmiiFormVersion.getId(), lmSubmission.getFormVersionID());
			assertEquals(cmiiFormVersion.getVersion(), lmSubmission.getFormVersion());
			assertEquals(cmmUser.getId(), lmSubmission.getUserID());
			assertEquals(cmmUser.getFirstName(), lmSubmission.getUserFirstName());
			assertEquals(null, lmSubmission.getUserMiddleName());
			assertEquals(cmmUser.getLastName(), lmSubmission.getUserLastName());
			assertEquals(cmmUser.getPrimaryPhone(), lmSubmission.getUserPhone());
			assertEquals(null, lmSubmission.getUserFax());
			assertEquals(cmmUser.getEmail(), lmSubmission.getUserEmail());

			List<CmiiSubmissionData> cmiiSubmissionDataList = cmiiSubmission.getCmiiSubmissionDataList();
			List<LmSubmissionData> lmSubmissionDataList = lmSubmission.getLmSubmissionDataList();
			for (int j = 0; j < cmiiSubmissionDataList.size(); j++) {
				CmiiSubmissionData cmiiSubmissionData = cmiiSubmissionDataList.get(j);
				LmSubmissionData lmSubmissionData = lmSubmissionDataList.get(j);

				assertEquals(cmiiSubmissionData.getSubmissionId(), lmSubmissionData.getLmSubmissionDataKey().getSubmissionId());
				assertEquals(cmiiSubmissionData.getEntity(), lmSubmissionData.getLmSubmissionDataKey().getFieldName());
				assertEquals(-1, lmSubmissionData.getMessageId());
				assertEquals(cmiiSubmissionData.getValue(), lmSubmissionData.getFieldValue());
			}

			List<CmiiSubmissionAttachment> cmiiSubmissionAttachments = cmiiSubmission.getCmiiSubmissionAttachments();
			List<LmSubmissionAttachment> lmSubmissionAttachments = lmSubmission.getLmSubmissionAttachments();
			for (int j = 0; j < cmiiSubmissionAttachments.size(); j++) {
				CmiiSubmissionAttachment cmiiSubmissionAttachment = cmiiSubmissionAttachments.get(j);
				LmSubmissionAttachment lmSubmissionAttachment = lmSubmissionAttachments.get(j);

				assertEquals(cmiiSubmissionAttachment.getSubmissionId(),
						lmSubmissionAttachment.getLmSubmissionAttachmentKey().getSubmissionId());
				assertEquals(j+1, lmSubmissionAttachment.getLmSubmissionAttachmentKey().getSequenceNumber());
				assertEquals(FilenameUtils.getBaseName(cmiiSubmissionAttachment.getOriginalFileName()), lmSubmissionAttachment.getTitle());
				assertEquals(cmiiSubmissionAttachment.getOriginalFileName(), lmSubmissionAttachment.getOriginalFileName());
				assertEquals(cmiiSubmissionAttachment.getUniqueFileName(), lmSubmissionAttachment.getActualTargetFileName());
				assertEquals(null, lmSubmissionAttachment.getLawManagerFileName());
				assertEquals(FilenameUtils.getExtension(cmiiSubmissionAttachment.getOriginalFileName()), lmSubmissionAttachment.getExtension());
				assertEquals(cmiiSubmissionAttachment.getContentType(), lmSubmissionAttachment.getContentType());
				assertEquals(null, lmSubmissionAttachment.getHashSHA256());
				assertEquals(cmiiSubmissionAttachment.getFileSize(),lmSubmissionAttachment.getFileSize());

			}

		}
	}

}
