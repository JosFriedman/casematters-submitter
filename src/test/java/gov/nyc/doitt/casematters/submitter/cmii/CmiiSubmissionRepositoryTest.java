package gov.nyc.doitt.casematters.submitter.cmii;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import gov.nyc.doitt.casematters.submitter.TestBase;
import gov.nyc.doitt.casematters.submitter.cmii.model.CmiiSubmissionMockerUpper;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CmiiSubmissionRepositoryTest extends TestBase {

	@Autowired
	private CmiiSubmissionRepository cmiiSubmissionRepository;

	@Autowired
	private CmiiSubmissionMockerUpper cmiiSubmissionMockerUpper;

	@Value("${submitter.cmii.maxBatchSize}")
	private int maxBatchSize;

	@Value("${submitter.cmii.maxRetriesForError}")
	private int maxRetriesForError;

	/*
	 * @Test
	 * 
	 * @Transactional("cmiiTransactionManager") public void testFindByCmiiSubmitterStatusInAndSubmitterErrorCountLessThan_NEW()
	 * throws Exception {
	 * 
	 * maxBatchSize = 1; CmiiSubmission cmiiSubmission = cmiiSubmissionMockerUpper.create();
	 * cmiiSubmission.setCmiiSubmitterStatus(CmiiSubmitterStatus.NEW); cmiiSubmissionRepository.save(cmiiSubmission);
	 * 
	 * PageRequest pageRequest = PageRequest.of(0, maxBatchSize, Sort.by(Sort.Direction.ASC, "submitted")); List<CmiiSubmission>
	 * cmiiSubmissions = cmiiSubmissionRepository.findByCmiiSubmitterStatusInAndSubmitterErrorCountLessThan( Arrays.asList(new
	 * CmiiSubmitterStatus[]{CmiiSubmitterStatus.NEW}), maxRetriesForError, pageRequest); assertNotNull(cmiiSubmissions);
	 * assertEquals(1, cmiiSubmissions.size()); assertTrue(cmiiSubmissions.contains(cmiiSubmission)); }
	 * 
	 * @Test
	 * 
	 * @Transactional("cmiiTransactionManager") public void
	 * testFindByCmiiSubmitterStatusInAndSubmitterErrorCountLessThan_LimitedByBatchSize() throws Exception {
	 * 
	 * int numberOfCmiiSubmissions = maxBatchSize + 5; // create more that are returned in batch List<CmiiSubmission>
	 * cmiiSubmissions = cmiiSubmissionMockerUpper.createList(numberOfCmiiSubmissions);
	 * 
	 * for (int i = 0; i < cmiiSubmissions.size(); i++) { CmiiSubmission cmiiSubmission = cmiiSubmissions.get(i);
	 * cmiiSubmission.setCmiiSubmitterStatus(i == 0 ? CmiiSubmitterStatus.NEW : CmiiSubmitterStatus.ERROR);
	 * cmiiSubmissionRepository.save(cmiiSubmission); }
	 * 
	 * PageRequest pageRequest = PageRequest.of(0, maxBatchSize, Sort.by(Sort.Direction.ASC, "submitted")); List<CmiiSubmission>
	 * batchOfCmiiSubmissions = cmiiSubmissionRepository .findByCmiiSubmitterStatusInAndSubmitterErrorCountLessThan(
	 * Arrays.asList(new CmiiSubmitterStatus[]{CmiiSubmitterStatus.NEW, CmiiSubmitterStatus.ERROR}), maxRetriesForError,
	 * pageRequest); assertNotNull(batchOfCmiiSubmissions); assertEquals(maxBatchSize, batchOfCmiiSubmissions.size());
	 * assertTrue(cmiiSubmissions.containsAll(batchOfCmiiSubmissions)); }
	 * 
	 * @Test
	 * 
	 * @Transactional("cmiiTransactionManager") public void
	 * testFindByCmiiSubmitterStatusInAndSubmitterErrorCountLessThan_NEW_and_ERROR_only() throws Exception {
	 * 
	 * int numberOfCmiiSubmissions = maxBatchSize + 20; // create more that are returned in batch List<CmiiSubmission>
	 * cmiiSubmissions = cmiiSubmissionMockerUpper.createList(numberOfCmiiSubmissions);
	 * 
	 * List<CmiiSubmission> couldBeInBatchCmiiSubmissions = new ArrayList<>(); for (int i = 0; i < cmiiSubmissions.size(); i++) {
	 * CmiiSubmission cmiiSubmission = cmiiSubmissions.get(i); if (i % 11 == 0) { cmiiSubmission.setCmiiSubmitterStatus(null); }
	 * else if (i % 9 == 0) { cmiiSubmission.setCmiiSubmitterStatus(CmiiSubmitterStatus.PROCESSING); } else if (i % 7 == 0) {
	 * cmiiSubmission.setCmiiSubmitterStatus(CmiiSubmitterStatus.ERROR); cmiiSubmission.setSubmitterErrorCount(maxRetriesForError +
	 * 1); } else if (i % 3 == 0) { cmiiSubmission.setCmiiSubmitterStatus(CmiiSubmitterStatus.ERROR);
	 * couldBeInBatchCmiiSubmissions.add(cmiiSubmission); } else if (i % 2 == 0) {
	 * cmiiSubmission.setCmiiSubmitterStatus(CmiiSubmitterStatus.NEW); couldBeInBatchCmiiSubmissions.add(cmiiSubmission); }
	 * cmiiSubmissionRepository.save(cmiiSubmission); } assertTrue(couldBeInBatchCmiiSubmissions.size() >= maxBatchSize); // make
	 * sure our test data is valid
	 * 
	 * PageRequest pageRequest = PageRequest.of(0, maxBatchSize, Sort.by(Sort.Direction.ASC, "submitted")); List<CmiiSubmission>
	 * batchOfCmiiSubmissions = cmiiSubmissionRepository .findByCmiiSubmitterStatusInAndSubmitterErrorCountLessThan(
	 * Arrays.asList(new CmiiSubmitterStatus[]{CmiiSubmitterStatus.NEW, CmiiSubmitterStatus.ERROR}), maxRetriesForError,
	 * pageRequest); assertNotNull(batchOfCmiiSubmissions); assertEquals(maxBatchSize, batchOfCmiiSubmissions.size());
	 * assertTrue(couldBeInBatchCmiiSubmissions.containsAll(batchOfCmiiSubmissions)); }
	 */
}
