package com.evertec.cibp.api.authentication.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "CUSTOMER_PROFILE")
public class Customer {
	
	@Id
	@Column(name="PROFILE_ID")
	private String profileId;
	@Column(name="PERM_ID")
	private Integer permId;
	@Column(name="USERNAME")
	private String userName;
	@Transient
	@Column(name="PASSWORD_HASH")
	private String passwordHash;
	@Transient
	@Column(name="PASSWORD_SALT")
	private String passwordSalt;
	@Column(name="EMAIL")
	private String email;
	@Column(name="DATE_OF_BIRTH")
	private String dateOfBirthday;
	@Column(name="STATUS")
	private String status;
	@Column(name="USER_INTERFACE")
	private String userInterface;
	@Column(name="UGENDER")
	private String gender;
	
	public Customer(){}
	
	public Customer(String profileId, Integer permId, String userName, String passwordHash, String passwordSalt,
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
	
	

}
