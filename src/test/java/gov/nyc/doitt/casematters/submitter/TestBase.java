package gov.nyc.doitt.casematters.submitter;

import static org.junit.Assert.assertNotNull;

import java.util.Iterator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.ScheduledAnnotationBeanPostProcessor;
import org.springframework.scheduling.config.ScheduledTask;
import org.springframework.test.context.junit4.SpringRunner;

//@RunWith(SpringRunner.class)
//@SpringBootTest
public class TestBase {

//	@Autowired
//	private ScheduledAnnotationBeanPostProcessor postProcessor;
//
//	@Autowired
//	private ApplicationContext appContext;
//
//	@Before
//	public void init() {
//
//		removeScheduledTasks(postProcessor, appContext);
//	}
//
//	@Test
//	public void testApplicationContext() throws Exception {
//		assertNotNull(appContext);
//	}
//
//	public static void removeScheduledTasks(ScheduledAnnotationBeanPostProcessor postProcessor, ApplicationContext appContext) {
//
//		postProcessor.setApplicationContext(appContext);
//
//		Iterator<ScheduledTask> iterator = postProcessor.getScheduledTasks().iterator();
//
//		while (iterator.hasNext()) {
//
//			ScheduledTask taskAtual = iterator.next();
//			taskAtual.cancel();
//
//		}
//	}
}
