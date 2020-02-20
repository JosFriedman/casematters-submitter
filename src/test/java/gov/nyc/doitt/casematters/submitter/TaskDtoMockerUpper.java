package gov.nyc.doitt.casematters.submitter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.springframework.stereotype.Component;

import gov.nyc.doitt.casematters.submitter.task.TaskDto;
import gov.nyc.doitt.casematters.submitter.task.TaskState;

@Component
public class TaskDtoMockerUpper {

	public String jobId = "1";

	public List<TaskDto> createList(int listSize) throws Exception {

		List<TaskDto> tasks = new ArrayList<>();
		for (int i = 0; i < listSize; i++) {
			tasks.add(create(i));
		}
		return tasks;
	}

	public TaskDto create() throws Exception {

		int i = new Random().nextInt(100) * -1;
		return create(i);
	}

	private TaskDto create(int idx) throws Exception {

		TaskDto taskDto = new TaskDto();
		taskDto.setName("nextTaskName" + idx);
		taskDto.setJobId(jobId);
		taskDto.setState(TaskState.PROCESSING.toString());
		taskDto.setStartDate(new Date(System.currentTimeMillis()));
		taskDto.setEndDate(new Date(System.currentTimeMillis()));

		return taskDto;
	}
}
