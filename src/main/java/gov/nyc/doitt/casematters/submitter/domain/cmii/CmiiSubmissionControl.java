package gov.nyc.doitt.casematters.submitter.domain.cmii;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Embeddable
public class CmiiSubmissionControl implements Serializable {

	private static final long serialVersionUID = 1L;

	@Enumerated(EnumType.STRING)
	@Column(name = "SUBMITTER_STATUS")
	private CmiiSubmissionSubmitterStatus cmiiSubmissionSubmitterStatus;

	@Column(name = "SUBMITTER_START_TIMESTAMP")
	private Timestamp submitterStartTimestamp;

	@Column(name = "SUBMITTER_END_TIMESTAMP")
	private Timestamp submitterEndTimestamp;

	@Column(name = "SUBMITTER_RETRY_COUNT")
	private int submitterRetryCount;

	public CmiiSubmissionSubmitterStatus getCmiiSubmissionSubmitterStatus() {
		return cmiiSubmissionSubmitterStatus;
	}

	public void setCmiiSubmissionSubmitterStatus(CmiiSubmissionSubmitterStatus cmiiSubmissionSubmitterStatus) {
		this.cmiiSubmissionSubmitterStatus = cmiiSubmissionSubmitterStatus;
	}

	public Timestamp getSubmitterStartTimestamp() {
		return submitterStartTimestamp;
	}

	public void setSubmitterStartTimestamp(Timestamp submitterStartTimestamp) {
		this.submitterStartTimestamp = submitterStartTimestamp;
	}

	public Timestamp getSubmitterEndTimestamp() {
		return submitterEndTimestamp;
	}

	public void setSubmitterEndTimestamp(Timestamp submitterEndTimestamp) {
		this.submitterEndTimestamp = submitterEndTimestamp;
	}

	public int getSubmitterRetryCount() {
		return submitterRetryCount;
	}

	public void setSubmitterRetryCount(int submitterRetryCount) {
		this.submitterRetryCount = submitterRetryCount;
	}
	
	public void incrementSubmitterRetryCount() {
		submitterRetryCount++;
	}

	@Override
	public String toString() {
		return "CmiiSubmitterControl [cmiiSubmissionSubmitterStatus=" + cmiiSubmissionSubmitterStatus + ", submitterStartTimestamp=" + submitterStartTimestamp
				+ ", submitterEndTimestamp=" + submitterEndTimestamp + ", submitterRetryCount=" + submitterRetryCount + "]";
	}

}
