package gov.nyc.doitt.casematters.submitter.lm.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import gov.nyc.doitt.casematters.submitter.TestBase;

@RunWith(SpringRunner.class)
@SpringBootTest
public class LmSummissionDataTest extends TestBase {

	@Autowired
	private LmSubmissionMockerUpper lmSubmissionMockerUpper;

	@Before
	public void setup() throws Exception {
	}

	@After
	public void teardown() throws Exception {
	}

	@Test
	public void testEquals() throws Exception {

		LmSubmission lmSubmission = lmSubmissionMockerUpper.create();

		LmSubmissionData lmSubmissionData0 = lmSubmission.getLmSubmissionDataList().get(0);
		LmSubmissionData lmSubmissionData1 = lmSubmission.getLmSubmissionDataList().get(1);

		assertEquals(lmSubmissionData0, lmSubmissionData0);

		assertNotEquals(lmSubmissionData0, this);

		assertNotEquals(lmSubmissionData0, lmSubmissionData1);
		assertNotEquals(lmSubmissionData0, null);

		FieldUtils.writeField(lmSubmissionData0, "lmSubmissionDataKey", null, true);
		assertNotEquals(lmSubmissionData0, lmSubmissionData1);

		FieldUtils.writeField(lmSubmissionData0, "lmSubmissionDataKey", lmSubmissionData1.getLmSubmissionDataKey(), true);
		assertEquals(lmSubmissionData0, lmSubmissionData1);

		FieldUtils.writeField(lmSubmissionData1, "lmSubmissionDataKey", null, true);
		assertNotEquals(lmSubmissionData0, lmSubmissionData1);

		FieldUtils.writeField(lmSubmissionData0, "lmSubmissionDataKey", null, true);
		assertEquals(lmSubmissionData0, lmSubmissionData1);

	}

	@Test
	public void testHashCode() throws Exception {

		LmSubmission lmSubmission = lmSubmissionMockerUpper.create();

		LmSubmissionData lmSubmissionData0 = lmSubmission.getLmSubmissionDataList().get(0);
		LmSubmissionData lmSubmissionData1 = lmSubmission.getLmSubmissionDataList().get(1);

		assertNotEquals(lmSubmissionData0.hashCode(), lmSubmissionData1.hashCode());

		FieldUtils.writeField(lmSubmissionData0, "lmSubmissionDataKey", lmSubmissionData1.getLmSubmissionDataKey(), true);
		assertEquals(lmSubmissionData0.hashCode(), lmSubmissionData1.hashCode());

		FieldUtils.writeField(lmSubmissionData0, "lmSubmissionDataKey", null, true);
		assertNotEquals(lmSubmissionData0.hashCode(), lmSubmissionData1.hashCode());

		FieldUtils.writeField(lmSubmissionData1, "lmSubmissionDataKey", null, true);
		assertEquals(lmSubmissionData0.hashCode(), lmSubmissionData1.hashCode());

	}

}
