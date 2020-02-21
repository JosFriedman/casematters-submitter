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
public class LmSummissionAttachmentTest extends TestBase {

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

		LmSubmissionAttachment lmSubmissionAttachment0 = lmSubmission.getLmSubmissionAttachments().get(0);
		LmSubmissionAttachment lmSubmissionAttachment1 = lmSubmission.getLmSubmissionAttachments().get(1);

		assertEquals(lmSubmissionAttachment0, lmSubmissionAttachment0);

		assertNotEquals(lmSubmissionAttachment0, this);

		assertNotEquals(lmSubmissionAttachment0, lmSubmissionAttachment1);
		assertNotEquals(lmSubmissionAttachment0, null);

		FieldUtils.writeField(lmSubmissionAttachment0, "lmSubmissionAttachmentKey", null, true);
		assertNotEquals(lmSubmissionAttachment0, lmSubmissionAttachment1);

		FieldUtils.writeField(lmSubmissionAttachment0, "lmSubmissionAttachmentKey",
				lmSubmissionAttachment1.getLmSubmissionAttachmentKey(), true);
		assertEquals(lmSubmissionAttachment0, lmSubmissionAttachment1);

		FieldUtils.writeField(lmSubmissionAttachment1, "lmSubmissionAttachmentKey", null, true);
		assertNotEquals(lmSubmissionAttachment0, lmSubmissionAttachment1);

		FieldUtils.writeField(lmSubmissionAttachment0, "lmSubmissionAttachmentKey", null, true);
		assertEquals(lmSubmissionAttachment0, lmSubmissionAttachment1);

	}

	@Test
	public void testHashCode() throws Exception {

		LmSubmission lmSubmission = lmSubmissionMockerUpper.create();

		LmSubmissionAttachment lmSubmissionAttachment0 = lmSubmission.getLmSubmissionAttachments().get(0);
		LmSubmissionAttachment lmSubmissionAttachment1 = lmSubmission.getLmSubmissionAttachments().get(1);

		assertNotEquals(lmSubmissionAttachment0.hashCode(), lmSubmissionAttachment1.hashCode());

		FieldUtils.writeField(lmSubmissionAttachment0, "lmSubmissionAttachmentKey",
				lmSubmissionAttachment1.getLmSubmissionAttachmentKey(), true);
		assertEquals(lmSubmissionAttachment0.hashCode(), lmSubmissionAttachment1.hashCode());

		FieldUtils.writeField(lmSubmissionAttachment0, "lmSubmissionAttachmentKey", null, true);
		assertNotEquals(lmSubmissionAttachment0.hashCode(), lmSubmissionAttachment1.hashCode());

		FieldUtils.writeField(lmSubmissionAttachment1, "lmSubmissionAttachmentKey", null, true);
		assertEquals(lmSubmissionAttachment0.hashCode(), lmSubmissionAttachment1.hashCode());

	}

}
