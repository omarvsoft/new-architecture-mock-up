package com.evertec.cibp.api.customer.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.provider.expression.OAuth2MethodSecurityExpressionHandler;

@EnableResourceServer
@Configuration
public class OAuth2Configuration {
	
	protected static class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.springframework.security.oauth2.config.annotation.web.
		 * configuration.ResourceServerConfigurerAdapter#configure(org.
		 * springframework.security.config.annotation.web.builders.HttpSecurity)
		 */
		@Override
		public void configure(HttpSecurity http) throws Exception {
			http.authorizeRequests().anyRequest().fullyAuthenticated();
		}
		

	}
	

	@EnableGlobalMethodSecurity(prePostEnabled=true)
	public static class MethodSecurityConfiguration extends GlobalMethodSecurityConfiguration {

	  @Override
	  protected MethodSecurityExpressionHandler createExpressionHandler() {
	    return new OAuth2MethodSecurityExpressionHandler();
	  }
	}

}
