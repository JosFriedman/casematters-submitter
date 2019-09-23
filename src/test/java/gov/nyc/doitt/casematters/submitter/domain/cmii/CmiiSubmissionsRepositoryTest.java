package gov.nyc.doitt.casematters.submitter.domain.cmii;

import static org.junit.Assert.assertNotNull;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import gov.nyc.doitt.casematters.submitter.domain.cmii.model.CmiiSubmission;
import gov.nyc.doitt.casematters.submitter.domain.cmii.model.CmiiSubmitterStatus;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CmiiSubmissionsRepositoryTest {

	@Autowired
	private CmiiSubmissionRepository cmiiSubmissionRepository;

	@Test
	@Transactional("cmiiTransactionManager")
	public void testFindNew() {

		List<CmiiSubmission> submissions = cmiiSubmissionRepository
				.findByCmiiSubmitterStatus(CmiiSubmitterStatus.NEW);
		// assertTrue(submissions.size() != 0);
		// assertTrue(submissions.get(0).getCmiiSubmissionDataList().size() != 0);
	}

//	@Test
//	@Transactional("cmiiTransactionManager")
//	public void testFindNewAndError() {
//
//		List<CmiiSubmission> submissions = cmiiSubmissionRepository.findByCmiiSubmitterStatusIn(Arrays.asList(
//				new CmiiSubmitterStatus[]{CmiiSubmitterStatus.NEW, CmiiSubmitterStatus.ERROR}));
//		// assertTrue(submissions.size() != 0);
//		// assertTrue(submissions.get(0).getCmiiSubmissionDataList().size() != 0);
//	}

	@Test
	@Transactional("cmiiTransactionManager")
	public void testFindNewAndErrorPage() {

		PageRequest pageRequest = PageRequest.of(0, 2, Sort.by(Sort.Direction.ASC, "submitted"));
		List<CmiiSubmission> cmiiSubmissions = cmiiSubmissionRepository.findByCmiiSubmitterStatusInAndSubmitterErrorCountLessThan(Arrays.asList(
				new CmiiSubmitterStatus[]{CmiiSubmitterStatus.NEW, CmiiSubmitterStatus.ERROR}), 3, pageRequest);
		assertNotNull(cmiiSubmissions);
	}

}
