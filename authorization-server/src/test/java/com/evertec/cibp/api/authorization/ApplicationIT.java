package com.evertec.cibp.api.authorization;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.RequestPostProcessor;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class ApplicationIT {
	
	@Autowired
	@Qualifier("defaultAuthorizationServerTokenServices")
    private AuthorizationServerTokenServices tokenservice;
	
	@Autowired
    private MockMvc mvc;
	
//	@Test
//	public void contextLoads() {
//	}
	
	@Test
	public void bearerToken() throws Exception {
		RequestPostProcessor bearerToken = addBearerToken("test", "ROLE_USER");
		ResultActions resultActions = mvc.perform(post("/hello-again").with(bearerToken)).andDo(print());
		 
		  resultActions
		    .andExpect(status().isOk())
		    .andExpect(content().string("hello again"));
	}
	
	public RequestPostProcessor addBearerToken(final String username, String... authorities) {
        return mockRequest -> {
            // Create OAuth2 token
            OAuth2Request oauth2Request = new OAuth2Request(null, "mi-banco-web", null, true, null, null, null, null, null);
            Authentication userauth = new TestingAuthenticationToken(username, null, authorities);
            OAuth2Authentication oauth2auth = new OAuth2Authentication(oauth2Request, userauth);
            OAuth2AccessToken token = tokenservice.createAccessToken(oauth2auth);
 
            // Set Authorization header to use Bearer
            mockRequest.addHeader("Authorization", "Bearer " + token.getValue());
            return mockRequest;
        };
    }

}
