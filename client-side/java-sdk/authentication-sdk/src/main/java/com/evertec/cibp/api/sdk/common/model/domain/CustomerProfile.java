package com.evertec.cibp.api.sdk.common.model.domain;

public class CustomerProfile {
	
	private String profileId;

	private Integer permId;

	private String userName;

	private String passwordHash;

	private String passwordSalt;

	private String email;

	private String dateOfBirthday;

	private String status;

	private String userInterface;

	private String gender;
	
	public CustomerProfile(){}
	
	public CustomerProfile(String profileId, Integer permId, String userName, String passwordHash, String passwordSalt,
			String email, String dateOfBirthday, String status, String userInterface, String gender) {
		super();
		this.profileId = profileId;
		this.permId = permId;
		this.userName = userName;
		this.passwordHash = passwordHash;
		this.passwordSalt = passwordSalt;
		this.email = email;
		this.dateOfBirthday = dateOfBirthday;
		this.status = status;
		this.userInterface = userInterface;
		this.gender = gender;
	}

	public String getProfileId() {
		return profileId;
	}
	public void setProfileId(String profileId) {
		this.profileId = profileId;
	}
	public Integer getPermId() {
		return permId;
	}
	public void setPermId(Integer permId) {
		this.permId = permId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPasswordHash() {
		return passwordHash;
	}
	public void setPasswordHash(String passwordHash) {
		this.passwordHash = passwordHash;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getDateOfBirthday() {
		return dateOfBirthday;
	}
	public void setDateOfBirthday(String dateOfBirthday) {
		this.dateOfBirthday = dateOfBirthday;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getUserInterface() {
		return userInterface;
	}
	public void setUserInterface(String userInterface) {
		this.userInterface = userInterface;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getPasswordSalt() {
		return passwordSalt;
	}

	public void setPasswordSalt(String passwordSalt) {
		this.passwordSalt = passwordSalt;
	}

	@Override
	public String toString() {
		return "CustomerProfile [profileId=" + profileId + ", permId=" + permId + ", userName=" + userName
				+ ", passwordHash=" + passwordHash + ", passwordSalt=" + passwordSalt + ", email=" + email
				+ ", dateOfBirthday=" + dateOfBirthday + ", status=" + status + ", userInterface=" + userInterface
				+ ", gender=" + gender + "]";
	}
	
	
	
}
