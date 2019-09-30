package gov.nyc.doitt.casematters.submitter.lm;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import gov.nyc.doitt.casematters.submitter.TestBase;
import gov.nyc.doitt.casematters.submitter.lm.model.LmSubmission;
import gov.nyc.doitt.casematters.submitter.lm.model.LmSubmissionMockerUpper;

@RunWith(SpringRunner.class)
@SpringBootTest
public class LmSubmissionRepositoryTest extends TestBase {

	@Autowired
	private LmSubmissionRepository lmSubmissionRepository;

	@Autowired
	private LmSubmissionMockerUpper lmSubmissionMockerUpper;

	@Test
	@Transactional("lmTransactionManager")
	public void testSaveOne() throws Exception {

		LmSubmission lmSubmission = lmSubmissionMockerUpper.create();

		LmSubmission savedLmSubmission = lmSubmissionRepository.save(lmSubmission);
		assertNotNull(savedLmSubmission);
	}

	@Test
	@Transactional("lmTransactionManager")
	public void testSaveAll() throws Exception {

		List<LmSubmission> lmSubmissions = lmSubmissionMockerUpper.createList(5);

		List<LmSubmission> savedLmSubmissions = lmSubmissionRepository.saveAll(lmSubmissions);
		assertNotNull(savedLmSubmissions);
		assertEquals(lmSubmissions.size(), savedLmSubmissions.size());
	}

}
