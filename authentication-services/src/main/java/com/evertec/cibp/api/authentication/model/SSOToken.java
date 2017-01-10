package com.evertec.cibp.api.authentication.model;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * The Class SSOToken.
 */
@Entity
@Table(name = "SSO_TOKEN")
public class SSOToken implements Serializable {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/**
	 * The Enum Status.
	 */
	public enum Status {
        
        /** The exists. */
        EXISTS, 
 /** The does not exists. */
 DOES_NOT_EXISTS
    };
    
    /** The status. */
    @Transient
    private Status status;
    
    /** The return url. */
    @Column(name="RETURN_URL")
    private String returnUrl;
    
    /** The reference data. */
    @Column(name="REFERENCE_DATA")
    private String referenceData;
    
    /** The perm id. */
    @Column(name="PERM_ID")
    private BigInteger permId;
    
    /** The username. */
    @Column(name="USERNAME")
    private String username;
    
    /** The token. */
    @Id
    private String token;
    
    /** The creation date. */
    @Column(name="CREATION_DATE")
    private Date creationDate;
    
    /** The authorized. */
    @Column(name="AUTHORIZED")
    private boolean authorized;
    
    /**
     * Instantiates a new SSO token.
     */
    public SSOToken() {}
    
    /**
     * Instantiates a new SSO token.
     *
     * @param token the token
     * @param referenceData the reference data
     * @param permId the perm id
     * @param userName the user name
     * @param creationDate the creation date
     * @param returnUrl the return url
     * @param authorized the authorized
     */
    public SSOToken(String token, String referenceData, BigInteger permId, String userName, Date creationDate, 
    		String returnUrl, boolean authorized) {
    	
    	this.token = token;
    	this.referenceData = referenceData;
    	this.permId = permId;
    	this.username = userName;
    	this.creationDate = creationDate;
    	this.returnUrl = returnUrl;
    	this.authorized = authorized;
    	
    }
    
    
    /**
     * Gets the status.
     *
     * @return the status
     */
    public Status getStatus() {
        return status;
    }

    /**
     * Sets the status.
     *
     * @param status the new status
     */
    public void setStatus(Status status) {
        this.status = status;
    }

    /**
     * Gets the token.
     *
     * @return the token
     */
    public String getToken() {
        return token;
    }

    /**
     * Sets the token.
     *
     * @param token the new token
     */
    public void setToken(String token) {
        this.token = token;
    }

    /**
     * Gets the return url.
     *
     * @return the return url
     */
    public String getReturnUrl() {
        return returnUrl;
    }

    /**
     * Sets the return url.
     *
     * @param returnUrl the new return url
     */
    public void setReturnUrl(String returnUrl) {
        this.returnUrl = returnUrl;
    }

    /**
     * Gets the reference data.
     *
     * @return the reference data
     */
    public String getReferenceData() {
        return referenceData;
    }

    /**
     * Sets the reference data.
     *
     * @param referenceData the new reference data
     */
    public void setReferenceData(String referenceData) {
        this.referenceData = referenceData;
    }

    /**
     * Gets the perm id.
     *
     * @return the perm id
     */
    public BigInteger getPermId() {
        return permId;
    }

    /**
     * Sets the perm id.
     *
     * @param permId the new perm id
     */
    public void setPermId(BigInteger permId) {
        this.permId = permId;
    }

    /**
     * Gets the creation date.
     *
     * @return the creation date
     */
    public Date getCreationDate() {
        return creationDate;
    }

    /**
     * Sets the creation date.
     *
     * @param creationDate the new creation date
     */
    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    /**
     * Gets the username.
     *
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the username.
     *
     * @param username the new username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((token == null) ? 0 : token.hashCode());
        return result;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final SSOToken other = (SSOToken) obj;
        if (token == null) {
            if (other.token != null)
                return false;
        } else if (!token.equals(other.token))
            return false;
        return true;
    }

    /**
     * Checks if is authorized.
     *
     * @return true, if is authorized
     */
    public boolean isAuthorized() {
        return authorized;
    }

    /**
     * Sets the authorized.
     *
     * @param authorized the new authorized
     */
    public void setAuthorized(boolean authorized) {
        this.authorized = authorized;
    }

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "SSOToken [status=" + status + ", returnUrl=" + returnUrl + ", referenceData=" + referenceData
				+ ", permId=" + permId + ", username=" + username + ", token=" + token + ", creationDate="
				+ creationDate + ", authorized=" + authorized + "]";
	}
    
    

}
