package gov.nyc.doitt.casematters.submitter.lm;

import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringRunner.class)
@SpringBootTest
public class LmSubmissionsRepositoryTest {

	@Autowired
	private LmSubmissionRepository lmSubmissionRepository;

	@Test
	@Transactional("lmTransactionManager")
	public void test() {
		assertTrue(true);
	}

}
