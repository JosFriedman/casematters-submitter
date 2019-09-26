package gov.nyc.doitt.casematters.submitter.cmii;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit4.SpringRunner;

import gov.nyc.doitt.casematters.submitter.cmii.model.CmiiSubmission;
import gov.nyc.doitt.casematters.submitter.cmii.model.CmiiSubmitterStatus;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CmiiSubmissionServiceTest {

	@Autowired
	private CmiiSubmissionMockerUpper cmiiSubmissionMockerUpper;

	@Mock
	private CmiiSubmissionRepository cmiiSubmissionRepository;

	@Spy
	@InjectMocks
	private CmiiSubmissionService cmiiSubmissionService = new CmiiSubmissionService();

	@Value("${submitter.maxBatchSize}")
	private int maxBatchSize;

	@Value(" ${submitter.maxRetriesForError}")
	private int maxRetriesForError;

	private Pageable pageable;

	@Before
	public void init() throws Exception {

		pageable = PageRequest.of(0, maxBatchSize, Sort.by(Sort.Direction.ASC, "submitted"));
		FieldUtils.writeField(cmiiSubmissionService, "pageRequest", pageable, true);
		FieldUtils.writeField(cmiiSubmissionService, "maxRetriesForError", maxRetriesForError, true);
	}

	@Test
	public void testSubmitterServiceNoSubmissions() {

		List<CmiiSubmission> cmiiSubmissions = Collections.emptyList();
		when(cmiiSubmissionRepository.findByCmiiSubmitterStatusInAndSubmitterErrorCountLessThan(
				ArgumentMatchers.<CmiiSubmitterStatus>anyList(), eq(maxRetriesForError), eq(pageable))).thenReturn(cmiiSubmissions);

		List<CmiiSubmission> batchOfCmiiSubmissions = cmiiSubmissionService.getNextBatch();

		verify(cmiiSubmissionRepository, times(1)).findByCmiiSubmitterStatusInAndSubmitterErrorCountLessThan(
				ArgumentMatchers.<CmiiSubmitterStatus>anyList(), anyInt(), any(Pageable.class));
		assertTrue(batchOfCmiiSubmissions.isEmpty());
	}

	@Test
	public void testSubmitterServiceWithSubmissions() throws Exception {

		int listSize = 5;
		List<CmiiSubmission> cmiiSubmissions = cmiiSubmissionMockerUpper.createList(listSize);
		when(cmiiSubmissionRepository.findByCmiiSubmitterStatusInAndSubmitterErrorCountLessThan(
				ArgumentMatchers.<CmiiSubmitterStatus>anyList(), eq(maxRetriesForError), eq(pageable))).thenReturn(cmiiSubmissions);

		List<CmiiSubmission> batchOfCmiiSubmissions = cmiiSubmissionService.getNextBatch();

		verify(cmiiSubmissionRepository, times(1)).findByCmiiSubmitterStatusInAndSubmitterErrorCountLessThan(
				ArgumentMatchers.<CmiiSubmitterStatus>anyList(), anyInt(), any(Pageable.class));
		assertTrue(batchOfCmiiSubmissions.isEmpty());

		batchOfCmiiSubmissions.forEach(p -> {
			assertEquals(CmiiSubmitterStatus.PROCESSING, p.getCmiiSubmitterStatus());
			assertNotNull(p.getSubmitterStartTimestamp());
			verify(cmiiSubmissionService).updateCmiiSubmission(p);
		});
	}

}
