package gov.nyc.doitt.casematters.submitter.lm.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class LmSubmissionAttachmentKey implements Serializable {

	private static final long serialVersionUID = 1L;

	@Column(name = "submissionID")
	private int submissionId;

	@Column(name = "sequenceNumber")
	private int sequenceNumber;

	public LmSubmissionAttachmentKey() {
	}

	public LmSubmissionAttachmentKey(int submissionId, int sequenceNumber) {
		this.submissionId = submissionId;
		this.sequenceNumber = sequenceNumber;
	}

	public int getSubmissionId() {
		return submissionId;
	}

	public int getSequenceNumber() {
		return sequenceNumber;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + sequenceNumber;
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
		LmSubmissionAttachmentKey other = (LmSubmissionAttachmentKey) obj;
		if (sequenceNumber != other.sequenceNumber)
			return false;
		if (submissionId != other.submissionId)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "LmSubmissionAttachmentKey [submissionId=" + submissionId + ", sequenceNumber=" + sequenceNumber + "]";
	}

}
