package com.evertec.cibp.api.authentication.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.evertec.cibp.api.authentication.client.CustomerClient;
import com.evertec.cibp.api.authentication.model.domain.CustomerProfile;
import com.evertec.cibp.api.authentication.model.domain.UserNameValidationResponse;

@RestController
@RequestMapping("v1/user/")
public class UserController {
	
	@Autowired
	private CustomerClient customerClient;
	
	@RequestMapping(value = "{userName}", method = RequestMethod.GET, 
			produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
 	@ResponseStatus(value = HttpStatus.OK)
	@PreAuthorize("#oauth2.isUser() and hasAuthority('ROLE_LOGIN')")
	public  UserNameValidationResponse validateUser(@PathVariable("userName") String customerId) {
		
		UserNameValidationResponse userNameValidation = new UserNameValidationResponse();
		
		CustomerProfile customerProfile = customerClient.getCustomerProfile(customerId);
		
		userNameValidation.setCustomerProfile(customerProfile);
		
		userNameValidation.setMbopId(true);
		
		userNameValidation.setRsaSessionId("10000");
		
		return userNameValidation;
		
	}

}
