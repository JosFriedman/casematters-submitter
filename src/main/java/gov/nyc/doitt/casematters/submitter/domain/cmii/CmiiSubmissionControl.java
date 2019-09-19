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

	@Column(name = "SUBMITTER_ERROR_COUNT")
	private int submitterErrorCount;

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

	public int getSubmitterErrorCount() {
		return submitterErrorCount;
	}

	public void setSubmitterErrorCount(int submitterErrorCount) {
		this.submitterErrorCount = submitterErrorCount;
	}
	
	public void incrementSubmitterErrorCount() {
		submitterErrorCount++;
	}

	@Override
	public String toString() {
		return "CmiiSubmitterControl [cmiiSubmissionSubmitterStatus=" + cmiiSubmissionSubmitterStatus + ", submitterStartTimestamp=" + submitterStartTimestamp
				+ ", submitterEndTimestamp=" + submitterEndTimestamp + ", submitterErrorCount=" + submitterErrorCount + "]";
	}

}
