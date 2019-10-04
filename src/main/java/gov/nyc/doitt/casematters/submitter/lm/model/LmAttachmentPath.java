package gov.nyc.doitt.casematters.submitter.lm.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "LmAttachmentPath")
public class LmAttachmentPath {

	@Id
	@Column(name = "AgencyAbbreviation")
	private String agencyAbbreviation;

	@Column(name = "AttachmentPath")
	private String attachmentPath;

	public String getAgencyAbbreviation() {
		return agencyAbbreviation;
	}

	public String getAttachmentPath() {
		return attachmentPath;
	}

	@Override
	public String toString() {
		return "LmAttachmentPath [agencyAbbreviation=" + agencyAbbreviation + ", attachmentPath=" + attachmentPath + "]";
	}

}
