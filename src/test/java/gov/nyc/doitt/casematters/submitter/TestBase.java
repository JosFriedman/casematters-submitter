package gov.nyc.doitt.casematters.submitter;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.ScheduledAnnotationBeanPostProcessor;

public class TestBase {

	@Autowired
	private ScheduledAnnotationBeanPostProcessor postProcessor;

	@Autowired
	private ApplicationContext appContext;

	@Before
	public void init() throws Exception {
		postProcessor.setApplicationContext(appContext);
		removeScheduledTasks();
	}

	private void removeScheduledTasks() {
		postProcessor.getScheduledTasks().forEach(p -> p.cancel());
	}
}
