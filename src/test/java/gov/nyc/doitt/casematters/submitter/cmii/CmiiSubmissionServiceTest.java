package gov.nyc.doitt.casematters.submitter.cmii;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import gov.nyc.doitt.casematters.submitter.TestBase;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CmiiSubmissionServiceTest extends TestBase {

	@Mock
	private CmiiSubmissionRepository cmiiSubmissionRepository;

	@Spy
	@InjectMocks
	private CmiiSubmissionService cmiiSubmissionService = new CmiiSubmissionService();

	@Before
	public void init() throws Exception {

	}

	@Test
	public void testGetSubmissions() {

		List<Long> ids = Collections.emptyList();
		cmiiSubmissionService.getSubmissions(ids);
		verify(cmiiSubmissionRepository).findByIdIn(eq(ids));
	}
}
