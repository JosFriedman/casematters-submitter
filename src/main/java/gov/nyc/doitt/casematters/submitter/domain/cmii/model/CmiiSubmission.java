package gov.nyc.doitt.casematters.submitter.domain.cmii.model;

import java.sql.Timestamp;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "SUBMISSIONS")
public class CmiiSubmission {

	@Id
	@Column(name = "ID")
	private long id;

	@OneToOne
	@JoinColumn(name = "userId", referencedColumnName = "id", updatable = false, insertable = false)
	private CmiiUser cmiiUser;

	@OneToOne
	@JoinColumn(name = "formVersionId", referencedColumnName = "id", updatable = false, insertable = false)
	private CmiiFormVersion cmiiFormVersion;

	@OneToMany(fetch = FetchType.EAGER)
	@JoinColumn(name = "submissionId", updatable = false, insertable = false)
	private List<CmiiSubmissionData> submissionDataList;

	@Enumerated(EnumType.STRING)
	@Column(name = "SUBMITTER_STATUS")
	private CmiiSubmissionSubmitterStatus cmiiSubmissionSubmitterStatus;

	@Column(name = "SUBMITTER_START_TIMESTAMP")
	private Timestamp submitterStartTimestamp;

	@Column(name = "SUBMITTER_END_TIMESTAMP")
	private Timestamp submitterEndTimestamp;

	@Column(name = "SUBMITTER_ERROR_COUNT")
	private int submitterErrorCount;

	public long getId() {
		return id;
	}

	public CmiiUser getCmiiUser() {
		return cmiiUser;
	}

	public CmiiFormVersion getCmiiFormVersion() {
		return cmiiFormVersion;
	}

	public List<CmiiSubmissionData> getSubmissionDataList() {
		return submissionDataList;
	}

	public void setSubmissionDataList(List<CmiiSubmissionData> submissionDataList) {
		this.submissionDataList = submissionDataList;
	}

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
		return "CmiiSubmission [id=" + id + ", cmiiUser=" + cmiiUser + ", cmiiFormVersion=" + cmiiFormVersion
				+ ", submissionDataList=" + submissionDataList + ", cmiiSubmissionSubmitterStatus=" + cmiiSubmissionSubmitterStatus
				+ ", submitterStartTimestamp=" + submitterStartTimestamp + ", submitterEndTimestamp=" + submitterEndTimestamp
				+ ", submitterErrorCount=" + submitterErrorCount + "]";
	}

	public String toSubmissionResult() {
		return "CmiiSubmission [id=" + id + ", cmiiSubmissionSubmitterStatus=" + cmiiSubmissionSubmitterStatus
				+ ", submitterStartTimestamp=" + submitterStartTimestamp + ", submitterEndTimestamp=" + submitterEndTimestamp
				+ ", submitterErrorCount=" + submitterErrorCount + "]";
	}

}
