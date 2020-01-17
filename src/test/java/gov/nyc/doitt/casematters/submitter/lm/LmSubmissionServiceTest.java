package gov.nyc.doitt.casematters.submitter.lm;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import gov.nyc.doitt.casematters.submitter.TestBase;
import gov.nyc.doitt.casematters.submitter.lm.model.LmSubmission;
import gov.nyc.doitt.casematters.submitter.lm.model.LmSubmissionMockerUpper;

@RunWith(SpringRunner.class)
@SpringBootTest
public class LmSubmissionServiceTest extends TestBase {

	@Autowired
	private LmSubmissionMockerUpper lmSubmissionMockerUpper;

	@Mock
	private LmSubmissionRepository lmSubmissionRepository;
	
	@Mock
	private LmAttachmentUploader lmAttachmentUploader;

	@Spy
	@InjectMocks
	private LmSubmissionService lmSubmissionService = new LmSubmissionService();

	@Test
	@Transactional("lmTransactionManager")
	public void testProcessSubmission() throws Exception {

		LmSubmission lmSubmission = lmSubmissionMockerUpper.create();

		lmSubmissionService.processSubmission(lmSubmission);
		verify(lmAttachmentUploader).upload(eq(lmSubmission));
		verify(lmSubmissionRepository, times(2)).save(eq(lmSubmission));
	}

}
