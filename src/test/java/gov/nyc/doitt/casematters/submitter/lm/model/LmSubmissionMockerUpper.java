package gov.nyc.doitt.casematters.submitter.lm.model;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import org.apache.commons.lang3.reflect.FieldUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class LmSubmissionMockerUpper {

	@Autowired
	private LmSubmissionDataMockerUpper lmSubmissionDataMockerUpper;

	@Autowired
	private LmSubmissionAttachmentMockerUpper lmSubmissionAttachmentMockerUpper;

	public List<LmSubmission> createList(int listSize) throws Exception {

		int id = new Random().nextInt(100) * -1;

		List<LmSubmission> lmSubmissions = new ArrayList<>();
		for (int i = 0; i < listSize; i++) {
			lmSubmissions.add(create(id - i));
		}
		return lmSubmissions;
	}

	public LmSubmission create() throws Exception {

		int id = new Random().nextInt(100) * -1;
		return create(id);
	}

	private LmSubmission create(int id) throws Exception {

		LmSubmission lmSubmission = new LmSubmission();

		lmSubmission.setMessageID(-1);
		lmSubmission.setSubmissionID(id);
		lmSubmission.setSubmissionParentID(null);
		lmSubmission.setSubmissionTimestamp(new Timestamp(System.currentTimeMillis()));
		lmSubmission.setSubmissionDescription("description" + id);
		lmSubmission.setAgencyID(id - 100);
		lmSubmission.setAgency("agency" + id);
		lmSubmission.setAgencyAbbreviation("agencyAbbreviation" + id);
		lmSubmission.setFormID(id - 200);
		lmSubmission.setFormName("formName" + id);
		lmSubmission.setFormAbbreviation("formAbbreviation" + id);
		lmSubmission.setFormVersionID(id - 300);
		lmSubmission.setFormVersion(id - 400);
		lmSubmission.setUserID(id - 500);
		lmSubmission.setUserFirstName("userFirstName" + id);
		lmSubmission.setUserMiddleName(null);
		lmSubmission.setUserLastName("userLastName" + id);
		lmSubmission.setUserPhone("userPhone" + id);
		lmSubmission.setUserFax(null);
		lmSubmission.setUserEmail("userEmail@abc.com" + id);

		lmSubmission.setLmSubmissionDataList(lmSubmissionDataMockerUpper.createList(id));
		lmSubmission.setLmSubmissionAttachments(lmSubmissionAttachmentMockerUpper.createList(id));

		return lmSubmission;
	}

}

@Component
class LmSubmissionDataMockerUpper {

	public List<LmSubmissionData> createList(int submissionId) throws Exception {

		List<LmSubmissionData> lmSubmissionDataList = new ArrayList<>();
		for (int i = 0; i < 10; i++) {
			lmSubmissionDataList.add(create(submissionId, submissionId - i));
		}
		return lmSubmissionDataList;
	}

	public LmSubmissionData create(int submissionId, int id) throws Exception {

		LmSubmissionData lmSubmissionData = new LmSubmissionData();

		LmSubmissionDataKey lmSubmissionDataKey = new LmSubmissionDataKey(submissionId, "fieldName" + id);
		lmSubmissionData.setLmSubmissionDataKey(lmSubmissionDataKey);
		lmSubmissionData.setFieldValue("fieldValue" + 1);
		lmSubmissionData.setMessageId(-1);
	
		return lmSubmissionData;
	}
}

@Component
class LmSubmissionAttachmentMockerUpper {

	public List<LmSubmissionAttachment> createList(int submissionId) throws Exception {

		List<LmSubmissionAttachment> lmSubmissionAttachments = new ArrayList<>();
		for (int i = 0; i < 5; i++) {
			lmSubmissionAttachments.add(create(submissionId, submissionId - i));
		}
		return lmSubmissionAttachments;
	}

	public LmSubmissionAttachment create(int submissionId, int id) throws Exception {

		LmSubmissionAttachment lmSubmissionAttachment = new LmSubmissionAttachment();

		FieldUtils.writeField(lmSubmissionAttachment, "id", id, true);
		FieldUtils.writeField(lmSubmissionAttachment, "submissionId", submissionId, true);
		FieldUtils.writeField(lmSubmissionAttachment, "contentType", "application/pdf", true);
		FieldUtils.writeField(lmSubmissionAttachment, "originalFileName", "originalFileName" + id, true);
		FieldUtils.writeField(lmSubmissionAttachment, "fileSize", 100000, true);
		UUID uuid = UUID.randomUUID();
		FieldUtils.writeField(lmSubmissionAttachment, "uniqueFileName", "lmAttachment-" + uuid + ".dat", true);
		FieldUtils.writeField(lmSubmissionAttachment, "uuid", uuid, true);

		return lmSubmissionAttachment;
	}
}
