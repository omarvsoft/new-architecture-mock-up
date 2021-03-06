package com.evertec.cibp.api.customer.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * The Class Customer.
 */
@Entity
@Table(name = "CUSTOMER_PROFILE")
public class Customer {
	
	
	/** The profile id. */
	@Column(name="PROFILE_ID")
	private String profileId;
	
	/** The perm id. */
	@Column(name="PERM_ID")
	private Integer permId;
	
	/** The user name. */
	@Id
	@Column(name="USERNAME")
	private String userName;
	
	/** The password hash. */
	@Transient
	@Column(name="PASSWORD_HASH")
	private String passwordHash;
	
	/** The password salt. */
	@Transient
	@Column(name="PASSWORD_SALT")
	private String passwordSalt;
	
	/** The email. */
	@Column(name="EMAIL")
	private String email;
	
	/** The date of birthday. */
	@Column(name="DATE_OF_BIRTH")
	private String dateOfBirthday;
	
	/** The status. */
	@Column(name="STATUS")
	private String status;
	
	/** The user interface. */
	@Column(name="USER_INTERFACE")
	private String userInterface;
	
	/** The gender. */
	@Column(name="UGENDER")
	private String gender;
	
	/**
	 * Instantiates a new customer.
	 */
	public Customer(){}
	
	/**
	 * Instantiates a new customer.
	 *
	 * @param profileId the profile id
	 * @param permId the perm id
	 * @param userName the user name
	 * @param passwordHash the password hash
	 * @param passwordSalt the password salt
	 * @param email the email
	 * @param dateOfBirthday the date of birthday
	 * @param status the status
	 * @param userInterface the user interface
	 * @param gender the gender
	 */
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

	/**
	 * Gets the profile id.
	 *
	 * @return the profile id
	 */
	public String getProfileId() {
		return profileId;
	}
	
	/**
	 * Sets the profile id.
	 *
	 * @param profileId the new profile id
	 */
	public void setProfileId(String profileId) {
		this.profileId = profileId;
	}
	
	/**
	 * Gets the perm id.
	 *
	 * @return the perm id
	 */
	public Integer getPermId() {
		return permId;
	}
	
	/**
	 * Sets the perm id.
	 *
	 * @param permId the new perm id
	 */
	public void setPermId(Integer permId) {
		this.permId = permId;
	}
	
	/**
	 * Gets the user name.
	 *
	 * @return the user name
	 */
	public String getUserName() {
		return userName;
	}
	
	/**
	 * Sets the user name.
	 *
	 * @param userName the new user name
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	/**
	 * Gets the password hash.
	 *
	 * @return the password hash
	 */
	public String getPasswordHash() {
		return passwordHash;
	}
	
	/**
	 * Sets the password hash.
	 *
	 * @param passwordHash the new password hash
	 */
	public void setPasswordHash(String passwordHash) {
		this.passwordHash = passwordHash;
	}
	
	/**
	 * Gets the email.
	 *
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}
	
	/**
	 * Sets the email.
	 *
	 * @param email the new email
	 */
	public void setEmail(String email) {
		this.email = email;
	}
	
	/**
	 * Gets the date of birthday.
	 *
	 * @return the date of birthday
	 */
	public String getDateOfBirthday() {
		return dateOfBirthday;
	}
	
	/**
	 * Sets the date of birthday.
	 *
	 * @param dateOfBirthday the new date of birthday
	 */
	public void setDateOfBirthday(String dateOfBirthday) {
		this.dateOfBirthday = dateOfBirthday;
	}
	
	/**
	 * Gets the status.
	 *
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}
	
	/**
	 * Sets the status.
	 *
	 * @param status the new status
	 */
	public void setStatus(String status) {
		this.status = status;
	}
	
	/**
	 * Gets the user interface.
	 *
	 * @return the user interface
	 */
	public String getUserInterface() {
		return userInterface;
	}
	
	/**
	 * Sets the user interface.
	 *
	 * @param userInterface the new user interface
	 */
	public void setUserInterface(String userInterface) {
		this.userInterface = userInterface;
	}
	
	/**
	 * Gets the gender.
	 *
	 * @return the gender
	 */
	public String getGender() {
		return gender;
	}
	
	/**
	 * Sets the gender.
	 *
	 * @param gender the new gender
	 */
	public void setGender(String gender) {
		this.gender = gender;
	}
	
	

}
