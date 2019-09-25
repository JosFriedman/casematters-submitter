package gov.nyc.doitt.casematters.submitter.cmii;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
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

	// @Test
	// @Transactional("cmiiTransactionManager")
	// public void testFindNewAndErrorPage() {
	//
	// PageRequest pageRequest = PageRequest.of(0, 2, Sort.by(Sort.Direction.ASC, "submitted"));
	// List<CmiiSubmission> cmiiSubmissions =
	// cmiiSubmissionRepository.findByCmiiSubmitterStatusInAndSubmitterErrorCountLessThan(Arrays.asList(
	// new CmiiSubmitterStatus[]{CmiiSubmitterStatus.NEW, CmiiSubmitterStatus.ERROR}), 3, pageRequest);
	// assertNotNull(cmiiSubmissions);
	// }
	//
	@Test
	@Transactional("cmiiTransactionManager")
	public void testFindByCmiiSubmitterStatusInAndSubmitterErrorCountLessThan() throws Exception {

		CmiiSubmission cmiiSubmission = cmiiSubmissionMockerUpper.create(1);
		cmiiSubmission.setCmiiSubmitterStatus(CmiiSubmitterStatus.NEW);
		cmiiSubmissionRepository.save(cmiiSubmission);

		PageRequest pageRequest = PageRequest.of(0, 2, Sort.by(Sort.Direction.ASC, "submitted"));
		List<CmiiSubmission> cmiiSubmissions = cmiiSubmissionRepository.findByCmiiSubmitterStatusInAndSubmitterErrorCountLessThan(
				Arrays.asList(new CmiiSubmitterStatus[]{CmiiSubmitterStatus.NEW, CmiiSubmitterStatus.ERROR}), 3, pageRequest);
		assertNotNull(cmiiSubmissions);
		assertTrue(cmiiSubmissions.size() >= 1);
		assertTrue(cmiiSubmissions.contains(cmiiSubmission));
	}

}
