package gov.nyc.doitt.casematters.submitter;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import gov.nyc.doitt.casematters.submitter.cmii.CmiiSubmissionService;
import gov.nyc.doitt.casematters.submitter.cmii.model.CmiiSubmission;
import gov.nyc.doitt.casematters.submitter.cmii.model.CmiiSubmissionMockerUpper;
import gov.nyc.doitt.casematters.submitter.lm.LmSubmissionService;
import gov.nyc.doitt.casematters.submitter.lm.model.LmSubmission;
import gov.nyc.doitt.casematters.submitter.lm.model.LmSubmissionMockerUpper;
import gov.nyc.doitt.casematters.submitter.task.JobStateManagerAccessor;
import gov.nyc.doitt.casematters.submitter.task.TaskDto;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SubmitterServiceTest extends TestBase {

	@Autowired
	private CmiiSubmissionMockerUpper cmiiSubmissionMockerUpper;

	@Autowired
	private LmSubmissionMockerUpper lmSubmissionMockerUpper;

	@Autowired
	private TaskDtoMockerUpper taskDtoMockerUpper;

	@InjectMocks
	private SubmitterService submitterService = new SubmitterService();

	@Mock
	private CmiiSubmissionService cmiiSubmissionService;

	@Mock
	private LmSubmissionService lmSubmissionService;

	@Mock
	private JobStateManagerAccessor jobStateManagerAccessor;

	@Autowired
	private CmiiToLmMapper cmiiToLmMapper;

	@Before
	public void init() throws Exception {

		FieldUtils.writeField(submitterService, "cmiiToLmMapper", cmiiToLmMapper, true);
	}

	@Test
	public void testSubmitterServiceWithSubmissions() throws Exception {

		int listSize = 5;
		List<CmiiSubmission> cmiiSubmissions = cmiiSubmissionMockerUpper.createList(listSize);
		List<TaskDto> taskDtos = taskDtoMockerUpper.createList(listSize);
		List<Long> jobIds = taskDtos.stream().map(p -> Long.parseLong(p.getJobId())).collect(Collectors.toList());
		when(cmiiSubmissionService.getSubmissions(jobIds)).thenReturn(cmiiSubmissions);

		when(jobStateManagerAccessor.startNextBatchOfTasks()).thenReturn(taskDtos);

		submitterService.submitBatch();

		verify(lmSubmissionService, times(listSize)).processSubmission(any(LmSubmission.class));
		verify(jobStateManagerAccessor).updateTaskResults((ArgumentMatchers.<TaskDto>anyList()));
	}

	@Test
	public void testSubmitterServiceWithNoSubmissions() throws Exception {

		int listSize = 0;
		List<CmiiSubmission> cmiiSubmissions = cmiiSubmissionMockerUpper.createList(listSize);
		List<TaskDto> taskDtos = taskDtoMockerUpper.createList(listSize);
		List<Long> jobIds = taskDtos.stream().map(p -> Long.parseLong(p.getJobId())).collect(Collectors.toList());
		when(cmiiSubmissionService.getSubmissions(jobIds)).thenReturn(cmiiSubmissions);

		when(jobStateManagerAccessor.startNextBatchOfTasks()).thenReturn(taskDtos);

		submitterService.submitBatch();

		verify(lmSubmissionService, times(listSize)).processSubmission(any(LmSubmission.class));
		verify(jobStateManagerAccessor, times(0)).updateTaskResults((ArgumentMatchers.<TaskDto>anyList()));
	}

}
