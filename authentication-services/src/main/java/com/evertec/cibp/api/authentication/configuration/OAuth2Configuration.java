package com.evertec.cibp.api.authentication.configuration;

import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;

@EnableResourceServer
@EnableGlobalMethodSecurity(prePostEnabled = true)
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
			http.authorizeRequests().anyRequest().authenticated();
		}

	}
	

}
