package com.evertec.cibp.api.sdk.authorization.client;

import com.evertec.cibp.api.sdk.authorization.oauth2.OAuth2Config;
import com.evertec.cibp.api.sdk.authorization.oauth2.OAuthUtils;
import com.evertec.cibp.api.sdk.common.model.security.Token;

import static com.evertec.cibp.api.sdk.authorization.oauth2.OAuthConstants.*;

public class OAuth2ClientImpl implements OAuth2Client {

	private final String username;
	private final String password;
	private final String clientId;
	private final String clientSecret;
	private final String site;

	public OAuth2ClientImpl(String username, String password, String clientId, String clientSecret, String site) {
		this.username = username;
		this.password = password;
		this.clientId = clientId;
		this.clientSecret = clientSecret;
		this.site = site;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public String getClientId() {
		return clientId;
	}

	public String getClientSecret() {
		return clientSecret;
	}

	public String getSite() {
		return site;
	}

	
	@Override
	public Token getAccessToken(String grantMethod, String grant) {
		OAuth2Config oauthConfig = new OAuth2Config.OAuth2ConfigBuilder(username, password, clientId, clientSecret,
				site).grantType(grant).grantMethod(grantMethod).build();
		return OAuthUtils.getAccessToken(oauthConfig);
	}
	
	@Override
	public Token getAccessToken(String grantMethod, String grant, String scope) {
		OAuth2Config oauthConfig = new OAuth2Config.OAuth2ConfigBuilder(username, password, clientId, clientSecret,
				site).grantType(grant).grantMethod(grantMethod).scope(scope).build();
		return OAuthUtils.getAccessToken(oauthConfig);
	}

}
