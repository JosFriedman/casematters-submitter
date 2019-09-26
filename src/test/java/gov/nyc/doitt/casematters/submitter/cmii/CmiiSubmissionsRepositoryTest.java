package gov.nyc.doitt.casematters.submitter.cmii;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.lang.Math.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import gov.nyc.doitt.casematters.submitter.cmii.model.CmiiSubmission;
import gov.nyc.doitt.casematters.submitter.cmii.model.CmiiSubmitterStatus;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CmiiSubmissionsRepositoryTest {

	@Autowired
	private CmiiSubmissionRepository cmiiSubmissionRepository;

	@Autowired
	private CmiiSubmissionMockerUpper cmiiSubmissionMockerUpper;

	@Value("${submitter.maxBatchSize}")
	private int maxBatchSize;

	@Value(" ${submitter.maxRetriesForError}")
	private int maxRetriesForError;

	@Test
	@Transactional("cmiiTransactionManager")
	public void testFindByCmiiSubmitterStatusInAndSubmitterErrorCountLessThan_NEW() throws Exception {

		maxBatchSize = 1;
		CmiiSubmission cmiiSubmission = cmiiSubmissionMockerUpper.create();
		cmiiSubmission.setCmiiSubmitterStatus(CmiiSubmitterStatus.NEW);
		cmiiSubmissionRepository.save(cmiiSubmission);

		PageRequest pageRequest = PageRequest.of(0, maxBatchSize, Sort.by(Sort.Direction.ASC, "submitted"));
		List<CmiiSubmission> cmiiSubmissions = cmiiSubmissionRepository.findByCmiiSubmitterStatusInAndSubmitterErrorCountLessThan(
				Arrays.asList(new CmiiSubmitterStatus[]{CmiiSubmitterStatus.NEW}), maxRetriesForError, pageRequest);
		assertNotNull(cmiiSubmissions);
		assertEquals(1, cmiiSubmissions.size());
		assertTrue(cmiiSubmissions.contains(cmiiSubmission));
	}

	@Test
	@Transactional("cmiiTransactionManager")
	public void testFindByCmiiSubmitterStatusInAndSubmitterErrorCountLessThan_LimitedByBatchSize() throws Exception {

		int numberOfCmiiSubmissions = maxBatchSize + 5; // create more that are returned in batch
		List<CmiiSubmission> cmiiSubmissions = cmiiSubmissionMockerUpper.createList(numberOfCmiiSubmissions);

		for (int i = 0; i < cmiiSubmissions.size(); i++) {
			CmiiSubmission cmiiSubmission = cmiiSubmissions.get(i);
			cmiiSubmission.setCmiiSubmitterStatus(i == 0 ? CmiiSubmitterStatus.NEW : CmiiSubmitterStatus.ERROR);
			cmiiSubmissionRepository.save(cmiiSubmission);

		}

		PageRequest pageRequest = PageRequest.of(0, maxBatchSize, Sort.by(Sort.Direction.ASC, "submitted"));
		List<CmiiSubmission> batchOfCmiiSubmissions = cmiiSubmissionRepository
				.findByCmiiSubmitterStatusInAndSubmitterErrorCountLessThan(
						Arrays.asList(new CmiiSubmitterStatus[]{CmiiSubmitterStatus.NEW, CmiiSubmitterStatus.ERROR}),
						maxRetriesForError, pageRequest);
		assertNotNull(batchOfCmiiSubmissions);
		assertEquals(maxBatchSize, batchOfCmiiSubmissions.size());
		assertTrue(cmiiSubmissions.containsAll(batchOfCmiiSubmissions));
	}

	@Test
	@Transactional("cmiiTransactionManager")
	public void testFindByCmiiSubmitterStatusInAndSubmitterErrorCountLessThan_NEW_and_ERROR_only() throws Exception {

		int numberOfCmiiSubmissions = maxBatchSize + 20; // create more that are returned in batch
		List<CmiiSubmission> cmiiSubmissions = cmiiSubmissionMockerUpper.createList(numberOfCmiiSubmissions);

		List<CmiiSubmission> couldInBatchCmiiSubmissions = new ArrayList<>();
		for (int i = 0; i < cmiiSubmissions.size(); i++) {
			CmiiSubmission cmiiSubmission = cmiiSubmissions.get(i);
			if (i % 11 == 0) {
				cmiiSubmission.setCmiiSubmitterStatus(null);
			} else if (i % 9 == 0) {
				cmiiSubmission.setCmiiSubmitterStatus(CmiiSubmitterStatus.PROCESSING);
			} else if (i % 7 == 0) {
				cmiiSubmission.setCmiiSubmitterStatus(CmiiSubmitterStatus.ERROR);
				cmiiSubmission.setSubmitterErrorCount(maxRetriesForError + 1);
			} else if (i % 3 == 0) {
				cmiiSubmission.setCmiiSubmitterStatus(CmiiSubmitterStatus.ERROR);
				couldInBatchCmiiSubmissions.add(cmiiSubmission);
			} else if (i % 2 == 0) {
				cmiiSubmission.setCmiiSubmitterStatus(CmiiSubmitterStatus.NEW);
				couldInBatchCmiiSubmissions.add(cmiiSubmission);
			}
			cmiiSubmissionRepository.save(cmiiSubmission);
		}
		assertTrue(couldInBatchCmiiSubmissions.size() >= maxBatchSize); // make sure our test data is valid

		PageRequest pageRequest = PageRequest.of(0, maxBatchSize, Sort.by(Sort.Direction.ASC, "submitted"));
		List<CmiiSubmission> batchOfCmiiSubmissions = cmiiSubmissionRepository
				.findByCmiiSubmitterStatusInAndSubmitterErrorCountLessThan(
						Arrays.asList(new CmiiSubmitterStatus[]{CmiiSubmitterStatus.NEW, CmiiSubmitterStatus.ERROR}),
						maxRetriesForError, pageRequest);
		assertNotNull(batchOfCmiiSubmissions);
		assertEquals(maxBatchSize, batchOfCmiiSubmissions.size());
		assertTrue(couldInBatchCmiiSubmissions.containsAll(batchOfCmiiSubmissions));
	}

}
