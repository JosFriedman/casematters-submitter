package gov.nyc.doitt.casematters.submitter.cmii.model;

import java.sql.Timestamp;
import java.util.List;

import javax.persistence.CascadeType;
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
import javax.persistence.Version;

@Entity
@Table(name = "SUBMISSIONS")
public class CmiiSubmission {

	@Id
	@Column(name = "ID")
	private long id;

	@Column(name = "PARENTID")
	private Long parentId;

	@Column(name = "SUBMITTED")
	private Timestamp submitted;

	@Column(name = "DESCRIPTION")
	private String description;

	@OneToOne(cascade = {CascadeType.ALL})
	@JoinColumn(name = "userId", referencedColumnName = "id", updatable = false, insertable = true)
	private CmiiUser cmiiUser;

	@OneToOne(cascade = {CascadeType.ALL})
	@JoinColumn(name = "formVersionId", referencedColumnName = "id", updatable = false, insertable = true)
	private CmiiFormVersion cmiiFormVersion;

	@OneToMany(fetch = FetchType.EAGER, cascade = {CascadeType.ALL})
	@JoinColumn(name = "submissionId", updatable = false, insertable = true)
	private List<CmiiSubmissionData> cmiiSubmissionDataList;

	@Enumerated(EnumType.STRING)
	@Column(name = "SUBMITTER_STATUS")
	private CmiiSubmitterStatus cmiiSubmitterStatus;

	@Column(name = "SUBMITTER_START_TIMESTAMP")
	private Timestamp submitterStartTimestamp;

	@Column(name = "SUBMITTER_END_TIMESTAMP")
	private Timestamp submitterEndTimestamp;

	@Column(name = "SUBMITTER_ERROR_COUNT")
	private int submitterErrorCount;

	@Version
	@Column(name = "SUBMITTER_MULTI_INSTANCE_CTRL")
	private int submitterMultiInstanceCtrl;

	public long getId() {
		return id;
	}

	public Long getParentId() {
		return parentId;
	}

	public Timestamp getSubmitted() {
		return submitted;
	}

	public String getDescription() {
		return description;
	}

	public CmiiUser getCmiiUser() {
		return cmiiUser;
	}

	public CmiiFormVersion getCmiiFormVersion() {
		return cmiiFormVersion;
	}

	public List<CmiiSubmissionData> getCmiiSubmissionDataList() {
		return cmiiSubmissionDataList;
	}

	public CmiiSubmitterStatus getCmiiSubmitterStatus() {
		return cmiiSubmitterStatus;
	}

	public void setCmiiSubmitterStatus(CmiiSubmitterStatus cmiiSubmitterStatus) {
		this.cmiiSubmitterStatus = cmiiSubmitterStatus;
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
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CmiiSubmission other = (CmiiSubmission) obj;
		if (id != other.id)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "CmiiSubmission [id=" + id + ", cmiiUser=" + cmiiUser + ", cmiiFormVersion=" + cmiiFormVersion
				+ ", submissionDataList=" + cmiiSubmissionDataList + ", cmiiSubmitterStatus=" + cmiiSubmitterStatus
				+ ", submitterStartTimestamp=" + submitterStartTimestamp + ", submitterEndTimestamp=" + submitterEndTimestamp
				+ ", submitterErrorCount=" + submitterErrorCount + "]";
	}

	public String toSubmissionResult() {
		return "CmiiSubmission [id=" + id + ", cmiiSubmitterStatus=" + cmiiSubmitterStatus
				+ ", submitterStartTimestamp=" + submitterStartTimestamp + ", submitterEndTimestamp=" + submitterEndTimestamp
				+ ", submitterErrorCount=" + submitterErrorCount + "]";
	}

}
