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

	public void setLmSubmissionDataKey(LmSubmissionDataKey lmSubmissionDataKey) {
		this.lmSubmissionDataKey = lmSubmissionDataKey;
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
		return "LmSubmissionData [lmSubmissionDataKey=" + lmSubmissionDataKey + ", fieldValue=" + fieldValue + ", messageId="
				+ messageId + "]";
	}

}
