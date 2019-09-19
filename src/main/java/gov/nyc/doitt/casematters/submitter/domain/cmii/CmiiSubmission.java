package gov.nyc.doitt.casematters.submitter.domain.cmii;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
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

	@Embedded
	private CmiiSubmissionControl cmiiSubmissionControl;

	@OneToMany(fetch = FetchType.EAGER)
	@JoinColumn(name = "submissionId", updatable = false, insertable = false)
	private List<CmiiSubmissionData> submissionDataList;

	public long getId() {
		return id;
	}

	public List<CmiiSubmissionData> getSubmissionDataList() {
		return submissionDataList;
	}

	
	public CmiiSubmissionControl getCmiiSubmissionControl() {
		return cmiiSubmissionControl;
	}

	
	public void setCmiiSubmissionControl(CmiiSubmissionControl cmiiSubmissionControl) {
		this.cmiiSubmissionControl = cmiiSubmissionControl;
	}

	
	public CmiiUser getCmiiUser() {
		return cmiiUser;
	}

}
