package com.evertec.cibp.api.authorization;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class ApplicationIT {
	
	@Autowired
	private TestRestTemplate restTemplate;
	
	@Test
	public void requestTokenUnauthorized() {
		ResponseEntity<String> response 
			= this.restTemplate.postForEntity("/authserver/oauth/token", "", String.class);
		
		assertEquals( response.getStatusCode() , HttpStatus.UNAUTHORIZED);
	}
	
	@Test
	public void requestUserInfoUnauthorized() {
		ResponseEntity<String> response 
			= this.restTemplate.postForEntity("/authserver/me", "", String.class);
		
		assertEquals( response.getStatusCode() , HttpStatus.UNAUTHORIZED);
	}
	

}
