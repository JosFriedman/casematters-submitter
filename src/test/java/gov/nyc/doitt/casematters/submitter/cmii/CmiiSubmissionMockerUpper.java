package gov.nyc.doitt.casematters.submitter.cmii;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

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

		int id = new Random().nextInt(100) * -1;
		
		List<CmiiSubmission> cmiiSubmissions = new ArrayList<>();
		for (int i = 0; i < listSize; i++) {
			cmiiSubmissions.add(create(id - i));
		}
		return cmiiSubmissions;
	}

	public CmiiSubmission create() throws Exception {

		int id = new Random().nextInt(100) * -1;
		return create(id);
	}
	
	private CmiiSubmission create(int id) throws Exception {

		CmiiSubmission cmiiSubmission = new CmiiSubmission();

		FieldUtils.writeField(cmiiSubmission, "id", id, true);
		FieldUtils.writeField(cmiiSubmission, "parentId", null, true);
		FieldUtils.writeField(cmiiSubmission, "submitted", new Timestamp(System.currentTimeMillis() - 900000000000L), true); // make very old so it is found first
		FieldUtils.writeField(cmiiSubmission, "description", "description" + id, true);
		FieldUtils.writeField(cmiiSubmission, "cmiiUser", cmiiUserMockerUpper.create(id), true);
		FieldUtils.writeField(cmiiSubmission, "cmiiFormVersion", cmiiFormVersionMockerUpper.create(id), true);
		FieldUtils.writeField(cmiiSubmission, "cmiiSubmissionDataList", cmiiSubmissionDataMockerUpper.createList(id), true);

		return cmiiSubmission;
	}
	
}

@Component
class CmiiSubmissionDataMockerUpper {

	public List<CmiiSubmissionData> createList(int submissionId) throws Exception {

		List<CmiiSubmissionData> cmiiSubmissionDataList = new ArrayList<>();
		for (int i = 0; i < 10; i++) {
			cmiiSubmissionDataList.add(create(submissionId, submissionId - i));
		}
		return cmiiSubmissionDataList;
	}

	public CmiiSubmissionData create(int submissionId, int id) throws Exception {

		CmiiSubmissionData cmiiSubmissionData = new CmiiSubmissionData();

		FieldUtils.writeField(cmiiSubmissionData, "id", id, true);
		FieldUtils.writeField(cmiiSubmissionData, "submissionId", submissionId, true);
		FieldUtils.writeField(cmiiSubmissionData, "entity", "entity" + id, true);
		FieldUtils.writeField(cmiiSubmissionData, "value", "value" + id, true);

		return cmiiSubmissionData;
	}
}

@Component
class CmiiUserMockerUpper {

	public CmiiUser create(int id) throws Exception {

		CmiiUser cmiiUser = new CmiiUser();

		FieldUtils.writeField(cmiiUser, "id", id, true);
		FieldUtils.writeField(cmiiUser, "samlId", "deadbeef" + id, true);
		FieldUtils.writeField(cmiiUser, "email", "abc@gmail.com" + id, true);
		FieldUtils.writeField(cmiiUser, "firstName", "John" + id, true);
		FieldUtils.writeField(cmiiUser, "lastName", "Doe" + id, true);
		FieldUtils.writeField(cmiiUser, "username", "John Doe" + id, true);
		FieldUtils.writeField(cmiiUser, "primaryPhone", "555-888-123" + id, true);
		FieldUtils.writeField(cmiiUser, "alternatePhone", "123-456-789" + id, true);
		FieldUtils.writeField(cmiiUser, "lastProfileUpdate", new Timestamp(System.currentTimeMillis()), true);

		return cmiiUser;
	}
}

@Component
class CmiiFormVersionMockerUpper {

	@Autowired
	private CmiiFormMockerUpper cmiiFormMockerUpper;

	public CmiiFormVersion create(int id) throws Exception {

		CmiiFormVersion cmiiFormVersion = new CmiiFormVersion();

		FieldUtils.writeField(cmiiFormVersion, "id", id, true);
		FieldUtils.writeField(cmiiFormVersion, "active", true, true);
		FieldUtils.writeField(cmiiFormVersion, "version", 100 + id, true);
		FieldUtils.writeField(cmiiFormVersion, "cmiiForm", cmiiFormMockerUpper.create(id), true);

		return cmiiFormVersion;
	}
}

@Component
class CmiiFormMockerUpper {

	@Autowired
	private CmiiAgencyMockerUpper cmiiAgencyMockerUpper;

	public CmiiForm create(int id) throws Exception {

		CmiiForm cmiiForm = new CmiiForm();

		FieldUtils.writeField(cmiiForm, "id", id, true);
		FieldUtils.writeField(cmiiForm, "active", true, true);
		FieldUtils.writeField(cmiiForm, "name", "name" + id, true);
		FieldUtils.writeField(cmiiForm, "tag", "tag" + id, true);
		FieldUtils.writeField(cmiiForm, "cmiiAgency", cmiiAgencyMockerUpper.create(id), true);

		return cmiiForm;
	}
}

@Component
class CmiiAgencyMockerUpper {

	public CmiiAgency create(int id) throws Exception {

		CmiiAgency cmiiAgency = new CmiiAgency();

		FieldUtils.writeField(cmiiAgency, "id", id, true);
		FieldUtils.writeField(cmiiAgency, "active", true, true);
		FieldUtils.writeField(cmiiAgency, "name", "name" + id, true);
		FieldUtils.writeField(cmiiAgency, "tag", "tag" + id, true);

		return cmiiAgency;
	}
}
