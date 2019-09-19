package gov.nyc.doitt.casematters.submitter.domain.lm;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "SubmissionData")
public class LmSubmissionData {

	@EmbeddedId
	private LmSubmissionDataKey mSubmissionDataKey;

	@Column(name = "fieldValue")
	private String fieldValue;

	@Column(name = "messageID")
	private int messageId;

	public LmSubmissionData() {
	}

	public LmSubmissionData(int submissionId, String fieldName) {
		this.mSubmissionDataKey = new LmSubmissionDataKey(submissionId, fieldName);
	}

	public LmSubmissionDataKey getmSubmissionDataKey() {
		return mSubmissionDataKey;
	}

	public void setmSubmissionDataKey(LmSubmissionDataKey mSubmissionDataKey) {
		this.mSubmissionDataKey = mSubmissionDataKey;
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
	public String toString() {
		return "LmSubmissionData [mSubmissionDataKey=" + mSubmissionDataKey + ", fieldValue=" + fieldValue + ", messageId="
				+ messageId + "]";
	}

}
