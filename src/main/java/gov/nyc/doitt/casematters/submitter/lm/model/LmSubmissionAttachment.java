package gov.nyc.doitt.casematters.submitter.lm.model;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.apache.commons.io.FilenameUtils;

@Entity
@Table(name = "SubmissionAttachments")
public class LmSubmissionAttachment {

	@EmbeddedId
	private LmSubmissionAttachmentKey lmSubmissionAttachmentKey;

	@Column(name = "title")
	private String title;

	@Column(name = "originalFileName")
	private String originalFileName;

	@Column(name = "standardizedFileName")
	private String standardizedFileName;

	@Column(name = "lawManagerFileName")
	private String lawManagerFileName;

	@Column(name = "extension")
	private String extension;

	@Column(name = "contentType")
	private String contentType;

	@Column(name = "hashSHA256")
	private String hashSHA256;

	@Column(name = "fileSize")
	private long fileSize;

	@Column(name = "fileMoved")
	private boolean fileMoved;

	@Column(name = "document_key")
	private Integer documentkey;

	@Column(name = "error_flag")
	private Boolean errorFlag;

	@Column(name = "messageID")
	private long messageId;

	@Column(name = "ftpCopyDurationInMillis")
	private long ftpCopyDurationInMillis;

	@Column(name = "smbCopyDurationInMillis")
	private long smbCopyDurationInMillis;

	@Column(name = "decryptDurationInMillis")
	private long decryptDurationInMillis;

	public LmSubmissionAttachment() {
	}

	public LmSubmissionAttachment(int submissionId, int sequenceNumber) {
		this.lmSubmissionAttachmentKey = new LmSubmissionAttachmentKey(submissionId, sequenceNumber);
	}

	public LmSubmissionAttachmentKey getLmSubmissionAttachmentKey() {
		return lmSubmissionAttachmentKey;
	}

	public void setLmSubmissionAttachmentKey(LmSubmissionAttachmentKey lmSubmissionAttachmentKey) {
		this.lmSubmissionAttachmentKey = lmSubmissionAttachmentKey;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getOriginalFileName() {
		return originalFileName;
	}

	public void setOriginalFileName(String originalFileName) {
		this.originalFileName = originalFileName;
	}

	public String getStandardizedFileName() {
		return standardizedFileName;
	}

	public void setStandardizedFileName(String standardizedFileName) {
		this.standardizedFileName = standardizedFileName;
	}

	public String getLawManagerFileName() {
		return lawManagerFileName;
	}

	public void setLawManagerFileName(String lawManagerFileName) {
		this.lawManagerFileName = lawManagerFileName;
	}

	public String getExtension() {
		return extension;
	}

	public void setExtension(String extension) {
		this.extension = extension;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public String getHashSHA256() {
		return hashSHA256;
	}

	public void setHashSHA256(String hashSHA256) {
		this.hashSHA256 = hashSHA256;
	}

	public long getFileSize() {
		return fileSize;
	}

	public void setFileSize(long fileSize) {
		this.fileSize = fileSize;
	}

	public Boolean isFileMoved() {
		return fileMoved;
	}

	public void setFileMoved(Boolean fileMoved) {
		this.fileMoved = fileMoved;
	}

	public Integer getDocumentkey() {
		return documentkey;
	}

	public void setDocumentkey(Integer documentkey) {
		this.documentkey = documentkey;
	}

	public Boolean isErrorFlag() {
		return errorFlag;
	}

	public void setErrorFlag(Boolean errorFlag) {
		this.errorFlag = errorFlag;
	}

	public long getMessageId() {
		return messageId;
	}

	public void setMessageId(long messageId) {
		this.messageId = messageId;
	}

	public long getFtpCopyDurationInMillis() {
		return ftpCopyDurationInMillis;
	}

	public void setFtpCopyDurationInMillis(long ftpCopyDurationInMillis) {
		this.ftpCopyDurationInMillis = ftpCopyDurationInMillis;
	}

	public long getSmbCopyDurationInMillis() {
		return smbCopyDurationInMillis;
	}

	public void setSmbCopyDurationInMillis(long smbCopyDurationInMillis) {
		this.smbCopyDurationInMillis = smbCopyDurationInMillis;
	}

	public long getDecryptDurationInMillis() {
		return decryptDurationInMillis;
	}

	public void setDecryptDurationInMillis(long decryptDurationInMillis) {
		this.decryptDurationInMillis = decryptDurationInMillis;
	}

	public String getTargetFileName() {
		return FilenameUtils.getBaseName(standardizedFileName) + "." + extension;
	}

	@Override
	public String toString() {
		return "LmSubmissionAttachment [lmSubmissionAttachmentKey=" + lmSubmissionAttachmentKey + ", title=" + title
				+ ", originalFileName=" + originalFileName + ", standardizedFileName=" + standardizedFileName
				+ ", lawManagerFileName=" + lawManagerFileName + ", extension=" + extension + ", contentType=" + contentType
				+ ", hashSHA256=" + hashSHA256 + ", fileSize=" + fileSize + ", fileMoved=" + fileMoved + ", documentkey="
				+ documentkey + ", errorFlag=" + errorFlag + ", messageId=" + messageId + ", ftpCopyDurationInMillis="
				+ ftpCopyDurationInMillis + ", smbCopyDurationInMillis=" + smbCopyDurationInMillis + ", decryptDurationInMillis="
				+ decryptDurationInMillis + "]";
	}

}
