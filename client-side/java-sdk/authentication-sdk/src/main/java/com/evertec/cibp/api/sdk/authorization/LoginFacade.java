package com.evertec.cibp.api.sdk.authorization;

import com.evertec.cibp.api.sdk.authorization.client.OAuth2Client;
import com.evertec.cibp.api.sdk.authorization.client.OAuth2ClientImpl;
import com.evertec.cibp.api.sdk.authorization.oauth2.OAuthUtils;
import com.evertec.cibp.api.sdk.authorization.util.JsonConverter;
import com.evertec.cibp.api.sdk.common.model.JsonResponse;
import com.evertec.cibp.api.sdk.common.model.domain.UserNameValidationResponse;
import com.evertec.cibp.api.sdk.common.model.security.Token;

import static com.evertec.cibp.api.sdk.authorization.oauth2.OAuthConstants.CLIENT_GRANT_METHOD;
import static com.evertec.cibp.api.sdk.authorization.oauth2.OAuthConstants.CLIENT_GRANT;

public class LoginFacade {
	
	private Token token;
	private OAuth2Client client;
	
	public LoginFacade(){}
	
	public LoginFacade(OAuth2Client client) {
		this.client = client;
	}
	
	public Boolean signClient(String clientId, String clientPassword, String site) {
		
		Boolean result = true;
		
		if (null == client) {
			client = new OAuth2ClientImpl(null, null, clientId, clientPassword, site);
		}
		
		this.token = client.getAccessToken(CLIENT_GRANT_METHOD, CLIENT_GRANT);
		
		if (null == token) {
			result = false;
		}
		
		return result;
		
	}
	
	public UserNameValidationResponse validateUser(String userName) {
		
		UserNameValidationResponse userNameValidation;
		
		String ahutorizationApiUrl = System.getProperty("ci-bp.services.ahutorization-api.url");
		
		if (null == token) {
			throw new IllegalStateException("Invalid Clien Token");
		} if (null == ahutorizationApiUrl) {
			throw new IllegalArgumentException("ci-bp.services.ahutorization-api.url environment variable is missing");
		}
		
		JsonResponse jsonResponse = OAuthUtils.getProtectedResource(client, token,
				ahutorizationApiUrl.concat("user/").concat(userName));
		
		
		if (jsonResponse.isValid()) {
			userNameValidation = 
					JsonConverter.jsonToObject(jsonResponse.getJson(), UserNameValidationResponse.class);

		} else {
			userNameValidation = new UserNameValidationResponse(false, jsonResponse.getErrorMessage());
		}
		
		
		return userNameValidation;
	}
	

}
