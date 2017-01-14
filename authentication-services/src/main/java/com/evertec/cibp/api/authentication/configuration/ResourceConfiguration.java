package com.evertec.cibp.api.authentication.configuration;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeResourceDetails;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;

/**
 * The Class ResourceConfiguration.
 */
@Configuration
@EnableOAuth2Client
public class ResourceConfiguration {
	
	/** The authorize url. */
	@Value("${security.oauth2.client.userAuthorizationUri}")
	private String authorizeUrl;

	/** The token url. */
	@Value("${security.oauth2.client.accessTokenUri}")
	private String tokenUrl;

	/** The client id. */
	@Value("${security.oauth2.client.client-id}")
	private String clientId;
	
	/** The client secret. */
	@Value("${security.oauth2.client.client-secret}")
	private String clientSecret;
	
	/**
	 * Resource.
	 *
	 * @return the o auth 2 protected resource details
	 */
	@Bean
    public OAuth2ProtectedResourceDetails resource() {
        AuthorizationCodeResourceDetails details = new AuthorizationCodeResourceDetails();
        details.setId("reddit");
        details.setClientId(clientId);
        details.setClientSecret(clientSecret);
        details.setAccessTokenUri(tokenUrl);
        details.setUserAuthorizationUri(authorizeUrl);
        details.setTokenName("oauth_token");
        details.setScope(Arrays.asList("api-access"));
        details.setUseCurrentUri(false);
        return details;
    }
 
    /**
     * Resource rest template.
     *
     * @param clientContext the client context
     * @return the o auth 2 rest template
     */
    @Bean
    public OAuth2RestTemplate resourceRestTemplate(OAuth2ClientContext clientContext) {
        OAuth2RestTemplate template = new OAuth2RestTemplate(resource(), clientContext);
        return template;
    }

}
