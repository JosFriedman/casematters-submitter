package gov.nyc.doitt.casematters.submitter;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class JobDto {

	private String jobId;
	private String state;
	private String errorReason;

	public JobDto() {
		super();
	}

	public JobDto(String jobId) {
		super();
		this.jobId = jobId;
	}

	public String getJobId() {
		return jobId;
	}

	public void setJobId(String jobId) {
		this.jobId = jobId;
	}

	public String getState() {
		return state;
	}

	public void setState(String status) {
		this.state = status;
	}

	public String getErrorReason() {
		return errorReason;
	}

	public void setErrorReason(String errorReason) {
		this.errorReason = errorReason;
	}

	@Override
	public String toString() {
		return "JobDto [jobId=" + jobId + ", state=" + state + ", errorReason=" + errorReason + "]";
	}

}
