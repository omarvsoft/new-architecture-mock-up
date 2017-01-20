package com.evertec.cibp.api.authentication.model.domain;

public class UserNameValidationResponse {
	
	private String returnURL;
	private int passwordSalt;

	private String rsaTransactionId;
	private String rsaSessionId;
	
	private Boolean mbopId = false;
	
	private CustomerProfile customerProfile;
	
	public enum ASK_FOR {
		SSDS_FORCED_LOGIN, DUMMY_QUESTIONS, OOB_CHALLENGE, QUESTION_CHALLENGE, CSR_ACTION, VALIDATE_PASSWORD, RSA_ENROLL, RSA_ENROLL_USERNAME, RSA_ENROLL_FROM_OAO,

		RECOVERY_CODE, CHANGE_USERNAME, CHANGE_PASSWORD, VALIDATE_PASSWORD_PIN, VALIDATE_DOB, VALIDATE_ENROLLACCOUNT, VALIDATE_PIN, SESSION_INIT, EXTERNAL_URL
		
	}
	
	public enum ASK_FOR_OOB {

		CHANGE_OOB, EDIT_PRIMARY_PHONE, OOB_CHALLENGE, EDIT_BACKUP_PHONE, EDIT_CHALLENGE_TYPE, EDIT_CHALLENGE_FREQUENCY, GENERATE_RECOVERY_CODE, DEACTIVATE_2_STEP, 
		
		ALERT_TYPES, EDIT_SMS, ATH_MOVIL, MY_DOCUMENTS, BALANCE, ATH_ALERT_TYPES, CCA_ALERT_TYPES, PERSONAL_INFORMATION_CHANGES, PAYMENTS_TRANSFERS, EDIT_EMAIL  
		
		, PERSONAL_INFORMATION
	}

	public String getReturnURL() {
		return returnURL;
	}

	public void setReturnURL(String returnURL) {
		this.returnURL = returnURL;
	}

	public int getPasswordSalt() {
		return passwordSalt;
	}

	public void setPasswordSalt(int passwordSalt) {
		this.passwordSalt = passwordSalt;
	}

	public String getRsaTransactionId() {
		return rsaTransactionId;
	}

	public void setRsaTransactionId(String rsaTransactionId) {
		this.rsaTransactionId = rsaTransactionId;
	}

	public String getRsaSessionId() {
		return rsaSessionId;
	}

	public void setRsaSessionId(String rsaSessionId) {
		this.rsaSessionId = rsaSessionId;
	}

	public Boolean getMbopId() {
		return mbopId;
	}

	public void setMbopId(Boolean mbopId) {
		this.mbopId = mbopId;
	}

	public CustomerProfile getCustomerProfile() {
		return customerProfile;
	}

	public void setCustomerProfile(CustomerProfile customerProfile) {
		this.customerProfile = customerProfile;
	}
}
