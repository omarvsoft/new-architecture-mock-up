package com.evertec.cibp.api.authorization.configuration;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.stereotype.Component;

/**
 * The Class SecurityConfiguration.
 */

@SuppressWarnings("unused")
@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter{
	
	/** The data source. */
	@Autowired
	private DataSource dataSource;
	
	@Autowired
	@Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.jdbcAuthentication().dataSource(dataSource).withUser("palencia")
		.password("pUma$").roles("LOGIN","USER","TRUSTED");
	}
	
	@Bean 
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
	     return super.authenticationManagerBean();
	}

}
