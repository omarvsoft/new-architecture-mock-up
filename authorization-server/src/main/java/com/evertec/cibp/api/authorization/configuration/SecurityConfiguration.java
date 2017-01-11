package com.evertec.cibp.api.authorization.configuration;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;

public class SecurityConfiguration {
	
	/** The data source. */
	@Autowired
	private DataSource dataSource;
	
       
    /**
     * Inits the.
     *
     * @param auth the auth
     * @throws Exception the exception
     */
    @Autowired
	public void init(AuthenticationManagerBuilder auth) throws Exception {
			auth.jdbcAuthentication().dataSource(dataSource).withUser("dave")
					.password("secret").roles("USER","TRUSTED");
	}

}
