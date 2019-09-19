package gov.nyc.doitt.casematters.submitter.domain.cmii;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "SUBMISSIONS")
public class CmiiSubmission {

	@Id
	@Column(name = "ID")
	private long id;

	@Enumerated(EnumType.STRING)
	@Column(name = "SUBMISSION_STATE")
	private CmiiSubmissionState submissionState;

	@OneToMany(fetch = FetchType.EAGER)
	@JoinColumn(name = "submissionId", updatable = false, insertable = false)
	private List<CmiiSubmissionData> submissionDataList;

	public long getId() {
		return id;
	}

	public CmiiSubmissionState getSubmissionState() {
		return submissionState;
	}

	public void setSubmissionState(CmiiSubmissionState submissionState) {
		this.submissionState = submissionState;
	}

	public List<CmiiSubmissionData> getSubmissionDataList() {
		return submissionDataList;
	}

}
