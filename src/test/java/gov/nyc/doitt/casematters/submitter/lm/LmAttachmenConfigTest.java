package gov.nyc.doitt.casematters.submitter.lm;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.util.List;

import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import gov.nyc.doitt.casematters.submitter.TestBase;
import gov.nyc.doitt.casematters.submitter.lm.model.LmAttachmentPath;
import gov.nyc.doitt.casematters.submitter.lm.model.LmSubmission;
import gov.nyc.doitt.casematters.submitter.lm.model.LmSubmissionMockerUpper;

@RunWith(SpringRunner.class)
@SpringBootTest
public class LmAttachmenConfigTest extends TestBase {

	@Mock
	private LmAttachmentPathRepository lmAttachmentPathRepository;

	@InjectMocks
	private LmAttachmentConfig lmAttachmentConfig = new LmAttachmentConfig();

	@Autowired
	private LmSubmissionMockerUpper lmSubmissionMockerUpper;

	@Autowired
	private LmAttachmentPathMockerUpper LmAttachmentPathMockerUpper;

	@Before
	public void setup() throws Exception {
	}

	@After
	public void teardown() throws Exception {
	}

	@Test
	public void testSetLawManagerCaseDirectory() throws Exception {

		LmSubmission lmSubmission = lmSubmissionMockerUpper.create();

		List<LmAttachmentPath> lmAttachmentPaths = LmAttachmentPathMockerUpper.createList();
		FieldUtils.writeField(lmAttachmentPaths.get(0), "agencyAbbreviation", lmSubmission.getAgencyAbbreviation(), true);

		when(lmAttachmentPathRepository.findAll()).thenReturn(lmAttachmentPaths);
		try {
			lmAttachmentConfig.setLawManagerCaseDirectory(lmSubmission);
			assertTrue(true);
		} catch (UnsupportedOperationException e) {
			assertTrue(false);
		}
	}

	@Test
	public void testSetLawManagerCaseDirectory_Fail() throws Exception {

		LmSubmission lmSubmission = lmSubmissionMockerUpper.create();

		List<LmAttachmentPath> lmAttachmentPaths = LmAttachmentPathMockerUpper.createList();
		FieldUtils.writeField(lmAttachmentPaths.get(0), "agencyAbbreviation", "unknown agency", true);

		when(lmAttachmentPathRepository.findAll()).thenReturn(lmAttachmentPaths);
		try {
			lmAttachmentConfig.setLawManagerCaseDirectory(lmSubmission);
			assertTrue(false);
		} catch (UnsupportedOperationException e) {
			assertTrue(true);
		}
	}

}
