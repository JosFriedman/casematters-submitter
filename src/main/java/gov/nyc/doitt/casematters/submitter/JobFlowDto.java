package gov.nyc.doitt.casematters.submitter;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class JobFlowDto {

	private String jobId;
	private String status;
	private String errorReason;

	public JobFlowDto() {
		super();
	}

	public JobFlowDto(String jobId) {
		super();
		this.jobId = jobId;
	}

	public String getJobId() {
		return jobId;
	}

	public void setJobId(String jobId) {
		this.jobId = jobId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getErrorReason() {
		return errorReason;
	}

	public void setErrorReason(String errorReason) {
		this.errorReason = errorReason;
	}

	@Override
	public String toString() {
		return "JobFlowDto [jobId=" + jobId + ", status=" + status + ", errorReason=" + errorReason + "]";
	}

}
