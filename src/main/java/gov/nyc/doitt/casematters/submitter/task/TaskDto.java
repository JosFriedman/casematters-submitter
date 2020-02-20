package gov.nyc.doitt.casematters.submitter.task;



import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class TaskDto {

	private String jobId;
	private String name;
	private Date startDate;
	private Date endDate;
	private String state;
	private String errorReason;
	private long errorCount;

	public TaskDto() {
	}
	
	public TaskDto(String jobId) {
		this.jobId = jobId;
	}

	public String getJobId() {
		return jobId;
	}

	public void setJobId(String jobId) {
		this.jobId = jobId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getErrorReason() {
		return errorReason;
	}

	public void setErrorReason(String errorReason) {
		this.errorReason = errorReason;
	}

	public long getErrorCount() {
		return errorCount;
	}

	public void setErrorCount(long errorCount) {
		this.errorCount = errorCount;
	}

	@Override
	public String toString() {
		return "TaskDto [jobId=" + jobId + ", name=" + name + ", startDate=" + startDate + ", endDate="
				+ endDate + ", state=" + state + ", errorReason=" + errorReason + ", errorCount=" + errorCount + "]";
	}

}
