package gov.nyc.doitt.casematters.submitter.domain.lm.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class LmSubmissionDataKey implements Serializable {

	private static final long serialVersionUID = 1L;

	@Column(name = "submissionID")
	private int submissionId;

	@Column(name = "fieldName")
	private String fieldName;

	public LmSubmissionDataKey() {
	}

	public LmSubmissionDataKey(int submissionId, String fieldName) {
		this.submissionId = submissionId;
		this.fieldName = fieldName;
	}

	public int getSubmissionId() {
		return submissionId;
	}

	public void setSubmissionId(int submissionId) {
		this.submissionId = submissionId;
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((fieldName == null) ? 0 : fieldName.hashCode());
		result = prime * result + submissionId;
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
		LmSubmissionDataKey other = (LmSubmissionDataKey) obj;
		if (fieldName == null) {
			if (other.fieldName != null)
				return false;
		} else if (!fieldName.equals(other.fieldName))
			return false;
		if (submissionId != other.submissionId)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "LmSubmissionDataKey [submissionId=" + submissionId + ", fieldName=" + fieldName + "]";
	}

}
