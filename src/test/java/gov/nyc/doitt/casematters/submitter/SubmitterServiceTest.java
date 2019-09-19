package gov.nyc.doitt.casematters.submitter;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SubmitterServiceTest {

	@Autowired
	private gov.nyc.doitt.casematters.submitter.domain.SubmitterService submitterService;

	@Test
	public void testSubmitterService() {

		submitterService.submitBatch();
	}

}
