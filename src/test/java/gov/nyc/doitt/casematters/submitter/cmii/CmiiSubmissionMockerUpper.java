package gov.nyc.doitt.casematters.submitter.cmii;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.reflect.FieldUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import gov.nyc.doitt.casematters.submitter.cmii.model.CmiiSubmission;
import gov.nyc.doitt.casematters.submitter.cmii.model.CmiiUser;

@Component
public class CmiiSubmissionMockerUpper {

	@Autowired
	private CmiiUserMockerUpper cmiiUserMockerUpper;

	public List<CmiiSubmission> createList(int listSize) throws Exception {

		List<CmiiSubmission> cmiiSubmissions = new ArrayList<>();
		for (int i = 0; i < listSize; i++) {
			cmiiSubmissions.add(create());
		}
		return cmiiSubmissions;
	}

	public CmiiSubmission create() throws Exception {

		CmiiSubmission cmiiSubmission = new CmiiSubmission();

		FieldUtils.writeField(cmiiSubmission, "id", 1234, true);
		FieldUtils.writeField(cmiiSubmission, "parentId", null, true);
		FieldUtils.writeField(cmiiSubmission, "submitted", new Timestamp(System.currentTimeMillis()), true);
		FieldUtils.writeField(cmiiSubmission, "description", "this submission blah blah ", true);
		FieldUtils.writeField(cmiiSubmission, "cmiiUser", cmiiUserMockerUpper.create(), true);

		return cmiiSubmission;
	}
}

@Component
class CmiiUserMockerUpper {

	public CmiiUser create() throws Exception {

		CmiiUser cmiiUser = new CmiiUser();

		FieldUtils.writeField(cmiiUser, "samlId", "deadbeef", true);
		FieldUtils.writeField(cmiiUser, "email", "abc@gmail.com", true);
		FieldUtils.writeField(cmiiUser, "firstName", "John", true);
		FieldUtils.writeField(cmiiUser, "lastName", "Doe", true);
		FieldUtils.writeField(cmiiUser, "username", "John Doe", true);
		FieldUtils.writeField(cmiiUser, "primaryPhone", "555-888-1234", true);
		FieldUtils.writeField(cmiiUser, "alternatePhone", "123-456-7890", true);
		FieldUtils.writeField(cmiiUser, "lastProfileUpdate", new Timestamp(System.currentTimeMillis()), true);

		return cmiiUser;
	}
}
