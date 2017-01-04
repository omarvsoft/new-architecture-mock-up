package com.evertec.cibp.api.authorization;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
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


@SpringBootApplication
@RestController
@EnableResourceServer
public class Application {
	
	@Autowired
	private DataSource dataSource;

	
	@RequestMapping("/user")
    public String user() {
        return "Hellow";
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Configuration
    @EnableAuthorizationServer
    protected static class OAuth2Config extends AuthorizationServerConfigurerAdapter {

        @Autowired
        private AuthenticationManager authenticationManager;
        
        @Autowired
		private DataSource dataSource;
        
        @Bean
		protected AuthorizationCodeServices authorizationCodeServices() {
			return new JdbcAuthorizationCodeServices(dataSource);
		}


        @Override
        public void configure(AuthorizationServerEndpointsConfigurer endpointsConfigurer) throws Exception {
            //endpointsConfigurer.authenticationManager(authenticationManager);
        	endpointsConfigurer.authorizationCodeServices(authorizationCodeServices())
			.authenticationManager(authenticationManager).tokenStore(tokenStore())
			.approvalStoreDisabled();
        }

        @Override
        public void configure(ClientDetailsServiceConfigurer clientDetailsServiceConfigurer) throws Exception {
            // Using hardcoded inmemory mechanism because it is just an example
            clientDetailsServiceConfigurer.jdbc(dataSource);
//                    .withClient("client")
//                    .secret("clientsecret")
//                    .authorizedGrantTypes("authorization_code", "refresh_token", "implicit", "password", "client_credentials")
//                    .scopes("apiAccess");
        }
        
        
        @Bean
		public JdbcTokenStore tokenStore() {
			return new JdbcTokenStore(dataSource);
		}
    }
    
    @Configuration
	@EnableResourceServer
	protected static class ResourceServer extends ResourceServerConfigurerAdapter {

		@Autowired
		private TokenStore tokenStore;

		@Override
		public void configure(ResourceServerSecurityConfigurer resources)
				throws Exception {
			resources.tokenStore(tokenStore);
		}

		@Override
		public void configure(HttpSecurity http) throws Exception {
			http.authorizeRequests().anyRequest().authenticated();
		}

	}
    
    @Autowired
	public void init(AuthenticationManagerBuilder auth) throws Exception {
		// @formatter:off
			auth.jdbcAuthentication().dataSource(dataSource).withUser("dave")
					.password("secret").roles("USER");
			// @formatter:on
	}
}