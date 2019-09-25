package gov.nyc.doitt.casematters.submitter.cmii;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.reflect.FieldUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import gov.nyc.doitt.casematters.submitter.cmii.model.CmiiAgency;
import gov.nyc.doitt.casematters.submitter.cmii.model.CmiiForm;
import gov.nyc.doitt.casematters.submitter.cmii.model.CmiiFormVersion;
import gov.nyc.doitt.casematters.submitter.cmii.model.CmiiSubmission;
import gov.nyc.doitt.casematters.submitter.cmii.model.CmiiSubmissionData;
import gov.nyc.doitt.casematters.submitter.cmii.model.CmiiUser;

@Component
public class CmiiSubmissionMockerUpper {

	@Autowired
	private CmiiSubmissionDataMockerUpper cmiiSubmissionDataMockerUpper;
	
	@Autowired
	private CmiiUserMockerUpper cmiiUserMockerUpper;

	@Autowired
	private CmiiFormVersionMockerUpper cmiiFormVersionMockerUpper;

	public List<CmiiSubmission> createList(int listSize) throws Exception {

		List<CmiiSubmission> cmiiSubmissions = new ArrayList<>();
		for (int i = 0; i < listSize; i++) {
			cmiiSubmissions.add(create(i));
		}
		return cmiiSubmissions;
	}

	public CmiiSubmission create(int i) throws Exception {

		CmiiSubmission cmiiSubmission = new CmiiSubmission();

//		FieldUtils.writeField(cmiiSubmission, "id", i, true);
		FieldUtils.writeField(cmiiSubmission, "parentId", null, true);
		FieldUtils.writeField(cmiiSubmission, "submitted", new Timestamp(System.currentTimeMillis()), true);
		FieldUtils.writeField(cmiiSubmission, "description", "description" + i, true);
		FieldUtils.writeField(cmiiSubmission, "cmiiUser", cmiiUserMockerUpper.create(i), true);
		FieldUtils.writeField(cmiiSubmission, "cmiiFormVersion", cmiiFormVersionMockerUpper.create(i), true);
		FieldUtils.writeField(cmiiSubmission, "cmiiSubmissionDataList", cmiiSubmissionDataMockerUpper.createList(i), true);

		return cmiiSubmission;
	}
}

@Component
class CmiiSubmissionDataMockerUpper {

	public List<CmiiSubmissionData> createList(int submissionId) throws Exception {

		List<CmiiSubmissionData> cmiiSubmissionDataList = new ArrayList<>();
		for (int i = 0; i < 10; i++) {
			cmiiSubmissionDataList.add(create(submissionId, i));
		}
		return cmiiSubmissionDataList;
	}

	public CmiiSubmissionData create(int submissionId, int i) throws Exception {

		CmiiSubmissionData cmiiSubmissionData = new CmiiSubmissionData();

//		FieldUtils.writeField(cmiiSubmissionData, "id", i, true);
		FieldUtils.writeField(cmiiSubmissionData, "submissionId", submissionId, true);
		FieldUtils.writeField(cmiiSubmissionData, "entity", "entity" + i, true);
		FieldUtils.writeField(cmiiSubmissionData, "value", "value" + i, true);

		return cmiiSubmissionData;
	}
}

@Component
class CmiiUserMockerUpper {

	public CmiiUser create(int i) throws Exception {

		CmiiUser cmiiUser = new CmiiUser();

//		FieldUtils.writeField(cmiiUser, "id", i, true);
		FieldUtils.writeField(cmiiUser, "samlId", "deadbeef" + i, true);
		FieldUtils.writeField(cmiiUser, "email", "abc@gmail.com" + i, true);
		FieldUtils.writeField(cmiiUser, "firstName", "John" + i, true);
		FieldUtils.writeField(cmiiUser, "lastName", "Doe" + i, true);
		FieldUtils.writeField(cmiiUser, "username", "John Doe" + i, true);
		FieldUtils.writeField(cmiiUser, "primaryPhone", "555-888-123" + i, true);
		FieldUtils.writeField(cmiiUser, "alternatePhone", "123-456-789" + i, true);
		FieldUtils.writeField(cmiiUser, "lastProfileUpdate", new Timestamp(System.currentTimeMillis()), true);

		return cmiiUser;
	}
}

@Component
class CmiiFormVersionMockerUpper {

	@Autowired
	private CmiiFormMockerUpper cmiiFormMockerUpper;

	public CmiiFormVersion create(int i) throws Exception {

		CmiiFormVersion cmiiFormVersion = new CmiiFormVersion();

//		FieldUtils.writeField(cmiiFormVersion, "id", i, true);
		FieldUtils.writeField(cmiiFormVersion, "active", true, true);
		FieldUtils.writeField(cmiiFormVersion, "version", 100 + i, true);
		FieldUtils.writeField(cmiiFormVersion, "cmiiForm", cmiiFormMockerUpper.create(i), true);

		return cmiiFormVersion;
	}
}

@Component
class CmiiFormMockerUpper {

	@Autowired
	private CmiiAgencyMockerUpper cmiiAgencyMockerUpper;

	public CmiiForm create(int i) throws Exception {

		CmiiForm cmiiForm = new CmiiForm();

		FieldUtils.writeField(cmiiForm, "id", i, true);
//		FieldUtils.writeField(cmiiForm, "active", true, true);
		FieldUtils.writeField(cmiiForm, "name", "name" + i, true);
		FieldUtils.writeField(cmiiForm, "tag", "tag" + i, true);
		FieldUtils.writeField(cmiiForm, "cmiiAgency", cmiiAgencyMockerUpper.create(i), true);

		return cmiiForm;
	}
}

@Component
class CmiiAgencyMockerUpper {

	public CmiiAgency create(int i) throws Exception {

		CmiiAgency cmiiAgency = new CmiiAgency();

//		FieldUtils.writeField(cmiiAgency, "id", i, true);
		FieldUtils.writeField(cmiiAgency, "active", true, true);
		FieldUtils.writeField(cmiiAgency, "name", "name" + i, true);
		FieldUtils.writeField(cmiiAgency, "tag", "tag" + i, true);

		return cmiiAgency;
	}
}
