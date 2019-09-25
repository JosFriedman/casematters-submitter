package gov.nyc.doitt.casematters.submitter;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ManualSubmitter extends TestBase {

	@Autowired
	private SubmitterService submitterService;

	// this test is an integration test and should not be enabled except on developer's desktop
	private static boolean enabled = true;

	@Test
	public void testSubmitterService() {
		if (enabled) {
			submitterService.submitBatch();
		}
	}

}
