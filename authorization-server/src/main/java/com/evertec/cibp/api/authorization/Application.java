package com.evertec.cibp.api.authorization;

import java.security.Principal;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.code.JdbcAuthorizationCodeServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * The Class Application.
 */
@SpringBootApplication
@RestController
@EnableResourceServer
public class Application {
	
	/** The data source. */
	@Autowired
	private DataSource dataSource;

	
	/**
	 * User.
	 *
	 * @param principal the principal
	 * @return the principal
	 */
	@RequestMapping({ "/user", "/me" })
	public Principal user(Principal principal) {
	  return principal;
	}

    /**
     * The main method.
     *
     * @param args the arguments
     */
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    /**
     * The Class OAuth2Config.
     */
    @Configuration
    @EnableAuthorizationServer
    protected static class OAuth2Config extends AuthorizationServerConfigurerAdapter {

        /** The authentication manager. */
        @Autowired
        private AuthenticationManager authenticationManager;
        
        /** The data source. */
        @Autowired
		private DataSource dataSource;
        
		/** The password encoder. */
		private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        
        /**
         * Authorization code services.
         *
         * @return the authorization code services
         */
        @Bean
		protected AuthorizationCodeServices authorizationCodeServices() {
			return new JdbcAuthorizationCodeServices(dataSource);
		}

        /* (non-Javadoc)
         * @see org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter#configure(org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer)
         */
        @Override
		public void configure(AuthorizationServerSecurityConfigurer security)
				throws Exception {
			security.passwordEncoder(passwordEncoder);
		}
        
        /* (non-Javadoc)
         * @see org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter#configure(org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer)
         */
        @Override
        public void configure(AuthorizationServerEndpointsConfigurer endpointsConfigurer) throws Exception {
        	endpointsConfigurer.authorizationCodeServices(authorizationCodeServices())
			.authenticationManager(authenticationManager).tokenStore(tokenStore())
			.approvalStoreDisabled();
        }
        
        /* (non-Javadoc)
         * @see org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter#configure(org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer)
         */
        @Override
        public void configure(ClientDetailsServiceConfigurer clientDetailsServiceConfigurer) throws Exception {
            clientDetailsServiceConfigurer.jdbc(dataSource)
            		.passwordEncoder(passwordEncoder)
                    .withClient("mi-banco-web")
                    .secret("web123")
                    .authorizedGrantTypes("client_credentials")
                    .scopes("api_access","read:customer_profile","write:customer_profile").and()
                    .withClient("mi-banco-android")
                    .secret("android123")
                    .authorizedGrantTypes("client_credentials")
                    .scopes("api_access","read:customer_profile","write:customer_profile").and()
                    .withClient("mi-banco-ios")
                    .secret("iOS123")
                    .authorizedGrantTypes("client_credentials")
                    .scopes("api_access","read:customer_profile","write:customer_profile").and()
		            .withClient("authentication-services")
		            .secret("auth123")
		            .authorizedGrantTypes("authorization_code", "refresh_token", "implicit", "password", "client_credentials")
		            .scopes("api_access","read:customer_profile","write:customer_profile");
        }
        
        
        /**
         * Token store.
         *
         * @return the jdbc token store
         */
        @Bean
		public JdbcTokenStore tokenStore() {
			return new JdbcTokenStore(dataSource);
		}
    }
    
    /**
     * The Class ResourceServer.
     */
    @Configuration
	@EnableResourceServer
	protected static class ResourceServer extends ResourceServerConfigurerAdapter {

		/** The token store. */
		@Autowired
		private TokenStore tokenStore;

		/* (non-Javadoc)
		 * @see org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter#configure(org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer)
		 */
		@Override
		public void configure(ResourceServerSecurityConfigurer resources)
				throws Exception {
			resources.tokenStore(tokenStore);
		}

		/* (non-Javadoc)
		 * @see org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter#configure(org.springframework.security.config.annotation.web.builders.HttpSecurity)
		 */
		@Override
		public void configure(HttpSecurity http) throws Exception {
			http.authorizeRequests().anyRequest().authenticated();
		}

	}
    
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