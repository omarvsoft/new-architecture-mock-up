package com.evertec.cibp.api.sdk.common.model.domain;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class UserNameValidationResponse {
	
	@SerializedName("returnURL")
	@Expose
	private String returnURL;
	@SerializedName("passwordSalt")
	@Expose
	private int passwordSalt;
	@SerializedName("rsaTransactionId")
	@Expose
	private String rsaTransactionId;
	@SerializedName("rsaSessionId")
	@Expose
	private String rsaSessionId;
	@SerializedName("mbopId")
	@Expose
	private Boolean mbopId = false;
	@SerializedName("customerProfile")
	@Expose
	private CustomerProfile customerProfile;
	
	private String errorMessage;
	private Boolean successful;
	
	public enum ASK_FOR {
		SSDS_FORCED_LOGIN, DUMMY_QUESTIONS, OOB_CHALLENGE, QUESTION_CHALLENGE, CSR_ACTION, VALIDATE_PASSWORD, RSA_ENROLL, RSA_ENROLL_USERNAME, RSA_ENROLL_FROM_OAO,

		RECOVERY_CODE, CHANGE_USERNAME, CHANGE_PASSWORD, VALIDATE_PASSWORD_PIN, VALIDATE_DOB, VALIDATE_ENROLLACCOUNT, VALIDATE_PIN, SESSION_INIT, EXTERNAL_URL
		
	}
	
	public enum ASK_FOR_OOB {

		CHANGE_OOB, EDIT_PRIMARY_PHONE, OOB_CHALLENGE, EDIT_BACKUP_PHONE, EDIT_CHALLENGE_TYPE, EDIT_CHALLENGE_FREQUENCY, GENERATE_RECOVERY_CODE, DEACTIVATE_2_STEP, 
		
		ALERT_TYPES, EDIT_SMS, ATH_MOVIL, MY_DOCUMENTS, BALANCE, ATH_ALERT_TYPES, CCA_ALERT_TYPES, PERSONAL_INFORMATION_CHANGES, PAYMENTS_TRANSFERS, EDIT_EMAIL  
		
		, PERSONAL_INFORMATION
	}
	
	public UserNameValidationResponse (Boolean successful, String errorMessage) {
		this.errorMessage = errorMessage;
		this.successful = successful;
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
	
	public String getErrorMessage() {
		return errorMessage;
	}

	public Boolean isSuccessful() {		
		if (successful == null) {
			successful = true;
		}
		return successful;
	}


	@Override
	public String toString() {
		return "UserNameValidationResponse [returnURL=" + returnURL + ", passwordSalt=" + passwordSalt
				+ ", rsaTransactionId=" + rsaTransactionId + ", rsaSessionId=" + rsaSessionId + ", mbopId=" + mbopId
				+ ", customerProfile=" + customerProfile + ", errorMessage=" + errorMessage + ", successful="
				+ successful + "]";
	}
	
	
}
