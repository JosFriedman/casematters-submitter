package gov.nyc.doitt.casematters.submitter.lm;

import static org.junit.Assert.assertTrue;

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
	public void testCreate() throws Exception {

		LmSubmission lmSubmission = lmSubmissionMockerUpper.create();

		assertTrue(true);
	}

}
