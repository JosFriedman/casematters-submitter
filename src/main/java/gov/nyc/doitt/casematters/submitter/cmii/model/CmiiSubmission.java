package gov.nyc.doitt.casematters.submitter.cmii.model;

import java.sql.Timestamp;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

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

	@OneToOne(cascade = { CascadeType.ALL })
	@JoinColumn(name = "userId", referencedColumnName = "id", updatable = false, insertable = true)
	private CmiiUser cmiiUser;

	@OneToOne(cascade = { CascadeType.ALL })
	@JoinColumn(name = "formVersionId", referencedColumnName = "id", updatable = false, insertable = true)
	private CmiiFormVersion cmiiFormVersion;

	@OneToMany(fetch = FetchType.EAGER, cascade = { CascadeType.ALL })
	@Fetch(value = FetchMode.SUBSELECT)
	@JoinColumn(name = "submissionId", updatable = false, insertable = true)
	private List<CmiiSubmissionData> cmiiSubmissionDataList;

	@OneToMany(fetch = FetchType.EAGER, cascade = { CascadeType.ALL })
	@Fetch(value = FetchMode.SUBSELECT)
	@JoinColumn(name = "submissionId", updatable = false, insertable = true)
	private List<CmiiSubmissionAttachment> cmiiSubmissionAttachments;

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

	public List<CmiiSubmissionAttachment> getCmiiSubmissionAttachments() {
		return cmiiSubmissionAttachments;
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
		return "CmiiSubmission [id=" + id + ", parentId=" + parentId + ", submitted=" + submitted + ", description=" + description
				+ ", cmiiUser=" + cmiiUser + ", cmiiFormVersion=" + cmiiFormVersion + ", cmiiSubmissionDataList="
				+ cmiiSubmissionDataList + ", cmiiSubmissionAttachments=" + cmiiSubmissionAttachments + "]";
	}

}
