package gov.nyc.doitt.casematters.submitter.lm.model;

import java.sql.Timestamp;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "Submissions")
public class LmSubmission {

	@Id
	private int submissionID;

	private int messageID;
	private Integer submissionParentID;
	private Timestamp submissionTimestamp;
	private String submissionDescription;
	private int agencyID;
	private String agency;
	private String agencyAbbreviation;
	private int formID;
	private String formName;
	private String formAbbreviation;
	private int formVersionID;
	private int formVersion;
	private int userID;
	private String userFirstName;
	private String userMiddleName;
	private String userLastName;
	private String userPhone;
	private String userFax;
	private String userEmail;

	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "submissionId", referencedColumnName = "submissionID")
	private List<LmSubmissionData> lmSubmissionDataList;

	public int getMessageID() {
		return messageID;
	}

	public void setMessageID(int messageID) {
		this.messageID = messageID;
	}

	public int getSubmissionID() {
		return submissionID;
	}

	public void setSubmissionID(int submissionID) {
		this.submissionID = submissionID;
	}

	public Integer getSubmissionParentID() {
		return submissionParentID;
	}

	public void setSubmissionParentID(Integer submissionParentID) {
		this.submissionParentID = submissionParentID;
	}

	public Timestamp getSubmissionTimestamp() {
		return submissionTimestamp;
	}

	public void setSubmissionTimestamp(Timestamp submissionTimestamp) {
		this.submissionTimestamp = submissionTimestamp;
	}

	public String getSubmissionDescription() {
		return submissionDescription;
	}

	public void setSubmissionDescription(String submissionDescription) {
		this.submissionDescription = submissionDescription;
	}

	public int getAgencyID() {
		return agencyID;
	}

	public void setAgencyID(int agencyID) {
		this.agencyID = agencyID;
	}

	public String getAgency() {
		return agency;
	}

	public void setAgency(String agency) {
		this.agency = agency;
	}

	public String getAgencyAbbreviation() {
		return agencyAbbreviation;
	}

	public void setAgencyAbbreviation(String agencyAbbreviation) {
		this.agencyAbbreviation = agencyAbbreviation;
	}

	public int getFormID() {
		return formID;
	}

	public void setFormID(int formID) {
		this.formID = formID;
	}

	public String getFormName() {
		return formName;
	}

	public void setFormName(String formName) {
		this.formName = formName;
	}

	public String getFormAbbreviation() {
		return formAbbreviation;
	}

	public void setFormAbbreviation(String formAbbreviation) {
		this.formAbbreviation = formAbbreviation;
	}

	public int getFormVersionID() {
		return formVersionID;
	}

	public void setFormVersionID(int formVersionID) {
		this.formVersionID = formVersionID;
	}

	public int getFormVersion() {
		return formVersion;
	}

	public void setFormVersion(int formVersion) {
		this.formVersion = formVersion;
	}

	public int getUserID() {
		return userID;
	}

	public void setUserID(int userID) {
		this.userID = userID;
	}

	public String getUserFirstName() {
		return userFirstName;
	}

	public void setUserFirstName(String userFirstName) {
		this.userFirstName = userFirstName;
	}

	public String getUserMiddleName() {
		return userMiddleName;
	}

	public void setUserMiddleName(String userMiddleName) {
		this.userMiddleName = userMiddleName;
	}

	public String getUserLastName() {
		return userLastName;
	}

	public void setUserLastName(String userLastName) {
		this.userLastName = userLastName;
	}

	public String getUserPhone() {
		return userPhone;
	}

	public void setUserPhone(String userPhone) {
		this.userPhone = userPhone;
	}

	public String getUserFax() {
		return userFax;
	}

	public void setUserFax(String userFax) {
		this.userFax = userFax;
	}

	public String getUserEmail() {
		return userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	public List<LmSubmissionData> getLmSubmissionDataList() {
		return lmSubmissionDataList;
	}

	public void setLmSubmissionDataList(List<LmSubmissionData> lmSubmissionDataList) {
		this.lmSubmissionDataList = lmSubmissionDataList;
	}

	@Override
	public String toString() {
		return "LmSubmission [messageID=" + messageID + ", submissionID=" + submissionID + ", submissionParentID="
				+ submissionParentID + ", submissionTimestamp=" + submissionTimestamp + ", submissionDescription="
				+ submissionDescription + ", agencyID=" + agencyID + ", agency=" + agency + ", agencyAbbreviation="
				+ agencyAbbreviation + ", formID=" + formID + ", formName=" + formName + ", formAbbreviation=" + formAbbreviation
				+ ", formVersionID=" + formVersionID + ", formVersion=" + formVersion + ", userID=" + userID + ", userFirstName="
				+ userFirstName + ", userMiddleName=" + userMiddleName + ", userLastName=" + userLastName + ", userPhone="
				+ userPhone + ", userFax=" + userFax + ", userEmail=" + userEmail + ", lmSubmissionDataList=" + lmSubmissionDataList
				+ "]";
	}

}
