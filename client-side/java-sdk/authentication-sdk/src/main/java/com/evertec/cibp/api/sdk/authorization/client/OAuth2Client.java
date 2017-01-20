package com.evertec.cibp.api.sdk.authorization.client;

import com.evertec.cibp.api.sdk.common.model.security.Token;

public interface OAuth2Client {
	Token getAccessToken(String grantMethod, String grant);
	Token getAccessToken(String grantMethod, String grant, String scope);
	String getUsername();
	String getPassword();
	String getClientId();
	String getClientSecret();
	String getSite();
}
