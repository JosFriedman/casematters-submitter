package gov.nyc.doitt.casematters.submitter;

import org.springframework.stereotype.Component;

@Component
public class JobDtoMockerUpper {

//	public String appId = "myApp1";
//
//	public List<JobDto> createList(int listSize) throws Exception {
//
//		List<JobDto> jobs = new ArrayList<>();
//		for (int i = 0; i < listSize; i++) {
//			jobs.add(create(i));
//		}
//		return jobs;
//	}
//
//	public JobDto create() throws Exception {
//
//		int i = new Random().nextInt(100) * -1;
//		return create(i);
//	}
//
//	private JobDto create(int idx) throws Exception {
//
//		JobDto jobDto = new JobDto();
//
//		FieldUtils.writeField(jobDto, "appId", appId, true);
//		FieldUtils.writeField(jobDto, "jobId", String.valueOf(idx), true);
//		FieldUtils.writeField(jobDto, "description", "description" + idx, true);
//
//		// make very old so it is found first
//		FieldUtils.writeField(jobDto, "createdTimestamp", new Timestamp(System.currentTimeMillis() - 9000000000000L), true);
//
//		FieldUtils.writeField(jobDto, "description", "description" + idx, true);
//
//		return jobDto;
//	}
}
