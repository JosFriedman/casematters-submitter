package gov.nyc.doitt.casematters.submitter.lm.model;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "SubmissionData")
public class LmSubmissionData {

	@EmbeddedId
	private LmSubmissionDataKey lmSubmissionDataKey;

	@Column(name = "fieldValue")
	private String fieldValue;

	@Column(name = "messageID")
	private int messageId;

	public LmSubmissionData() {
	}

	public LmSubmissionData(int submissionId, String fieldName) {
		this.lmSubmissionDataKey = new LmSubmissionDataKey(submissionId, fieldName);
	}

	public LmSubmissionDataKey getLmSubmissionDataKey() {
		return lmSubmissionDataKey;
	}

	public String getFieldValue() {
		return fieldValue;
	}

	public void setFieldValue(String fieldValue) {
		this.fieldValue = fieldValue;
	}

	public int getMessageId() {
		return messageId;
	}

	public void setMessageId(int messageId) {
		this.messageId = messageId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((lmSubmissionDataKey == null) ? 0 : lmSubmissionDataKey.hashCode());
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
		LmSubmissionData other = (LmSubmissionData) obj;
		if (lmSubmissionDataKey == null) {
			if (other.lmSubmissionDataKey != null)
				return false;
		} else if (!lmSubmissionDataKey.equals(other.lmSubmissionDataKey))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "LmSubmissionData [lmSubmissionDataKey=" + lmSubmissionDataKey + ", fieldValue=" + fieldValue + ", messageId="
				+ messageId + "]";
	}

}
