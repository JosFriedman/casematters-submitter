package gov.nyc.doitt.casematters.submitter.cmii;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.stream.Collectors;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import gov.nyc.doitt.casematters.submitter.TestBase;
import gov.nyc.doitt.casematters.submitter.cmii.model.CmiiSubmission;
import gov.nyc.doitt.casematters.submitter.cmii.model.CmiiSubmissionMockerUpper;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CmiiSubmissionRepositoryTest extends TestBase {

	@Autowired
	private CmiiSubmissionRepository cmiiSubmissionRepository;

	@Autowired
	private CmiiSubmissionMockerUpper cmiiSubmissionMockerUpper;

	@Test
	@Transactional("cmiiTransactionManager")
	public void testFindByIdIn() throws Exception {

		int numberOfCmiiSubmissions = 5;
		List<CmiiSubmission> cmiiSubmissions = cmiiSubmissionMockerUpper.createList(numberOfCmiiSubmissions);

		cmiiSubmissions.forEach(p -> cmiiSubmissionRepository.save(p));
		List<Long> ids = cmiiSubmissions.stream().map(p -> p.getId()).collect(Collectors.toList());

		List<CmiiSubmission> foundCmiiSubmissions = cmiiSubmissionRepository.findByIdIn(ids);
		assertNotNull(foundCmiiSubmissions);
		assertTrue(cmiiSubmissions.containsAll(foundCmiiSubmissions));
		assertTrue(foundCmiiSubmissions.containsAll(cmiiSubmissions));
	}

}
