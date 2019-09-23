package gov.nyc.doitt.casematters.submitter.cmii.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "FORMVERSIONS")
public class CmiiFormVersion {

	@Id
	@Column(name = "ID")
	private long id;

	@Column(name = "ACTIVE")
	private boolean active;

	@Column(name = "VERSION")
	private long version;

	@OneToOne
	@JoinColumn(name = "formId", referencedColumnName = "id", updatable = false, insertable = false)
	private CmiiForm cmiiForm;

	public long getId() {
		return id;
	}

	public boolean isActive() {
		return active;
	}

	public long getVersion() {
		return version;
	}

	public CmiiForm getCmiiForm() {
		return cmiiForm;
	}

	@Override
	public String toString() {
		return "CmiiFormVersion [id=" + id + ", active=" + active + ", version=" + version + ", cmiiForm=" + cmiiForm + "]";
	}

}
