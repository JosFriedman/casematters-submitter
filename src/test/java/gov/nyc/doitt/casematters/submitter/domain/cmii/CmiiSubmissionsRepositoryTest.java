package gov.nyc.doitt.casematters.submitter.domain.cmii;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import gov.nyc.doitt.casematters.submitter.domain.cmii.CmiiSubmission;
import gov.nyc.doitt.casematters.submitter.domain.cmii.CmiiSubmissionRepository;
import gov.nyc.doitt.casematters.submitter.domain.cmii.CmiiSubmissionState;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CmiiSubmissionsRepositoryTest {

	@Autowired
	private CmiiSubmissionRepository cmiiSubmissionRepository;

	@Test
	@Transactional("cmiiTransactionManager")
	public void testFindNew() {

		List<CmiiSubmission> submissions = cmiiSubmissionRepository.findBySubmissionState(CmiiSubmissionState.NEW);
		assertTrue(submissions.size() != 0);
		assertTrue(submissions.get(0).getSubmissionDataList().size() != 0);
	}

	@Test
	@Transactional("cmiiTransactionManager")
	public void testFindNewAndError() {

		List<CmiiSubmission> submissions = cmiiSubmissionRepository
				.findBySubmissionStateIn(Arrays.asList(new CmiiSubmissionState[]{CmiiSubmissionState.NEW, CmiiSubmissionState.ERROR}));
		assertTrue(submissions.size() != 0);
		assertTrue(submissions.get(0).getSubmissionDataList().size() != 0);
	}

}
