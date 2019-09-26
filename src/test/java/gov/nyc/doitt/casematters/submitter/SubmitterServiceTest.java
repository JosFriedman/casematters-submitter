package gov.nyc.doitt.casematters.submitter;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import gov.nyc.doitt.casematters.submitter.cmii.CmiiSubmissionMockerUpper;
import gov.nyc.doitt.casematters.submitter.cmii.CmiiSubmissionService;
import gov.nyc.doitt.casematters.submitter.cmii.SubmitterConcurrencyException;
import gov.nyc.doitt.casematters.submitter.cmii.model.CmiiSubmission;
import gov.nyc.doitt.casematters.submitter.lm.LmSubmissionMockerUpper;
import gov.nyc.doitt.casematters.submitter.lm.LmSubmissionService;
import gov.nyc.doitt.casematters.submitter.lm.model.LmSubmission;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SubmitterServiceTest extends TestBase {

	@Autowired
	private CmiiSubmissionMockerUpper cmiiSubmissionMockerUpper;

	@Autowired
	private LmSubmissionMockerUpper lmSubmissionMockerUpper;

	@InjectMocks
	private SubmitterService submitterService = new SubmitterService();

	@Mock
	private CmiiSubmissionService cmiiSubmissionService;

	@Mock
	private LmSubmissionService lmSubmissionService;

	@Mock
	private CmiiToLmMapper cmiiToLmMapper;

	@Test
	public void testSubmitterServiceNoSubmissions() {

		List<CmiiSubmission> cmiiSubmissions = Collections.emptyList();
		when(cmiiSubmissionService.getNextBatch()).thenReturn(cmiiSubmissions);

		submitterService.submitBatch();

		verify(cmiiToLmMapper, times(0)).fromCmii(cmiiSubmissions);
		verify(lmSubmissionService, times(0)).processSubmission(any(LmSubmission.class));
	}

	@Test
	public void testSubmitterServiceWithSubmissions() throws Exception {

		int listSize = 5;
		List<CmiiSubmission> cmiiSubmissions = cmiiSubmissionMockerUpper.createList(listSize);
		when(cmiiSubmissionService.getNextBatch()).thenReturn(cmiiSubmissions);

		List<LmSubmission> lmSubmissions = lmSubmissionMockerUpper.createList(listSize);
		for (int i = 0; i < listSize; i++) {
			when(cmiiToLmMapper.fromCmii(cmiiSubmissions.get(i))).thenReturn(lmSubmissions.get(i));
		}
		submitterService.submitBatch();

		verify(cmiiToLmMapper, times(listSize)).fromCmii(any(CmiiSubmission.class));
		verify(lmSubmissionService, times(listSize)).processSubmission(any(LmSubmission.class));
	}

	@Test
	public void testSubmitterServiceConcurrencyException() {

		List<CmiiSubmission> cmiiSubmissions = Collections.emptyList();
		when(cmiiSubmissionService.getNextBatch()).thenThrow(SubmitterConcurrencyException.class);

		submitterService.submitBatch();

		verify(cmiiToLmMapper, times(0)).fromCmii(cmiiSubmissions);
		verify(lmSubmissionService, times(0)).processSubmission(any(LmSubmission.class));
	}

	
}
