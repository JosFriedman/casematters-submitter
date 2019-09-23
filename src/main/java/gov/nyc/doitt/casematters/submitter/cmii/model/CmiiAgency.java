package gov.nyc.doitt.casematters.submitter.cmii.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "AGENCIES")
public class CmiiAgency {

	@Id
	@Column(name = "ID")
	private long id;

	@Column(name = "ACTIVE")
	private boolean active;

	@Column(name = "NAME")
	private String name;

	@Column(name = "TAG")
	private String tag;

	@Column(name = "FORMLISTTEXT")
	private String formListText;

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

	public String getFormListText() {
		return formListText;
	}

	@Override
	public String toString() {
		return "CmiiAgency [id=" + id + ", active=" + active + ", name=" + name + ", tag=" + tag + ", formListText=" + formListText
				+ "]";
	}

}
