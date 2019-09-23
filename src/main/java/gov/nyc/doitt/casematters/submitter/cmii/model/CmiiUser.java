package gov.nyc.doitt.casematters.submitter.cmii.model;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "CMUSERS")
public class CmiiUser {

	@Id
	@Column(name = "ID")
	private long id;

	@Column(name = "SAMLID")
	private String samlId;

	@Column(name = "EMAIL")
	private String email;

	@Column(name = "FIRSTNAME")
	private String firstName;

	@Column(name = "LASTNAME")
	private String lastName;

	@Column(name = "USERNAME")
	private String username;

	@Column(name = "PRIMARYPHONE")
	private String primaryPhone;

	@Column(name = "ALTERNATEPHONE")
	private String alternatePhone;

	@Column(name = "LASTPROFILEUPDATE")
	private Timestamp lastProfileUpdate;

	public long getId() {
		return id;
	}

	public String getSamlId() {
		return samlId;
	}

	public String getEmail() {
		return email;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public String getUsername() {
		return username;
	}

	public String getPrimaryPhone() {
		return primaryPhone;
	}

	public String getAlternatePhone() {
		return alternatePhone;
	}

	public Timestamp getLastProfileUpdate() {
		return lastProfileUpdate;
	}

	@Override
	public String toString() {
		return "CmiiUser [id=" + id + ", samlId=" + samlId + ", email=" + email + ", firstName=" + firstName + ", lastName="
				+ lastName + ", username=" + username + ", primaryPhone=" + primaryPhone + ", alternatePhone=" + alternatePhone
				+ ", lastProfileUpdate=" + lastProfileUpdate + "]";
	}

}
