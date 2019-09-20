package gov.nyc.doitt.casematters.submitter.domain.cmii.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "SUBMISSIONDATA")
public class CmiiSubmissionData {

	@Id
	@Column(name = "ID")
	private long id;

	@Column(name = "SUBMISSIONID")
	private long submissionId;
	
	@Column(name = "ENTITY")
	private String entity;
	
	@Column(name = "VALUE")
	private String value;


	public long getId() {
		return id;
	}

	public long getSubmissionId() {
		return submissionId;
	}

	public String getEntity() {
		return entity;
	}

	@Override
	public String toString() {
		return "CmiiSubmissionData [id=" + id + ", submissionId=" + submissionId + ", entity=" + entity + ", value=" + value + "]";
	}

	public String getValue() {
		return value;
	}
	
	
}
