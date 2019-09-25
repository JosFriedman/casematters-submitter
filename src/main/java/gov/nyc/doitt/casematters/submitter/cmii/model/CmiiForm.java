package gov.nyc.doitt.casematters.submitter.cmii.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "FORMS")
public class CmiiForm {

	@Id
	@Column(name = "ID")
	private long id;

	@Column(name = "ACTIVE")
	private boolean active;

	@Column(name = "NAME")
	private String name;

	@Column(name = "TAG")
	private String tag;

	@OneToOne(cascade = {CascadeType.ALL})
	@JoinColumn(name = "agencyId", referencedColumnName = "id", updatable = false, insertable = true)
	private CmiiAgency cmiiAgency;

	public long getId() {
		return id;
	}

	public boolean isActive() {
		return active;
	}

	public String getName() {
		return name;
	}

	public String getTag() {
		return tag;
	}

	public CmiiAgency getCmiiAgency() {
		return cmiiAgency;
	}

	@Override
	public String toString() {
		return "CmiiForm [id=" + id + ", active=" + active + ", name=" + name + ", tag=" + tag + ", cmiiAgency=" + cmiiAgency + "]";
	}

}
