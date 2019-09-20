package gov.nyc.doitt.casematters.submitter.domain.cmii;

import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CmiiSubmissionsRepositoryTest {

	@Autowired
	private CmiiSubmissionRepository cmiiSubmissionRepository;

	@Test
	@Transactional("cmiiTransactionManager")
	public void testFindNew() {

		List<CmiiSubmission> submissions = cmiiSubmissionRepository
				.findByCmiiSubmissionSubmitterStatus(CmiiSubmissionSubmitterStatus.NEW);
		assertTrue(submissions.size() != 0);
		assertTrue(submissions.get(0).getSubmissionDataList().size() != 0);
	}

	@Test
	@Transactional("cmiiTransactionManager")
	public void testFindNewAndError() {

		List<CmiiSubmission> submissions = cmiiSubmissionRepository
				.findByCmiiSubmissionSubmitterStatusIn(Arrays.asList(new CmiiSubmissionSubmitterStatus[]{
						CmiiSubmissionSubmitterStatus.NEW, CmiiSubmissionSubmitterStatus.ERROR}));
		assertTrue(submissions.size() != 0);
		assertTrue(submissions.get(0).getSubmissionDataList().size() != 0);
	}

}
