package gov.nyc.doitt.casematters.submitter.cmii.model;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "SUBMISSIONATTACHMENT")
public class CmiiSubmissionAttachment {

	@Id
	@Column(name = "ID")
	private long id;

	@Column(name = "SUBMISSIONID")
	private long submissionId;

	@Column(name = "CONTENTTYPE")
	private String contentType;

	@Column(name = "ORIGINALFILENAME")
	private String originalFileName;

	@Column(name = "FILESIZE")
	private long fileSize;

	@Column(name = "UNIQUEFILENAME")
	private String uniqueFileName;

	@Column(name = "UUID")
	private UUID uuid;

	public long getId() {
		return id;
	}

	public long getSubmissionId() {
		return submissionId;
	}

	public String getContentType() {
		return contentType;
	}

	public String getOriginalFileName() {
		return originalFileName;
	}

	public long getFileSize() {
		return fileSize;
	}

	public String getUniqueFileName() {
		return uniqueFileName;
	}

	public UUID getUuid() {
		return uuid;
	}

	@Override
	public String toString() {
		return "CmiiSubmissionAttachment [id=" + id + ", submissionId=" + submissionId + ", contentType=" + contentType
				+ ", originalFileName=" + originalFileName + ", fileSize=" + fileSize + ", uniqueFileName=" + uniqueFileName
				+ ", uuid=" + uuid + "]";
	}

}
