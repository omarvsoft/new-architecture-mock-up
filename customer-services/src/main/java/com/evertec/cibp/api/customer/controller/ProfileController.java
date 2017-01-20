/*
 * 
 */
package com.evertec.cibp.api.customer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.evertec.cibp.api.customer.model.Customer;
import com.evertec.cibp.api.customer.repository.CustomerRepository;

/**
 * The Class TokenController.
 * #oauth2.hasScope('api_access')
 * #oauth2.clientHasRole
 * #oauth2.isUser() and hasAuthority('UT1')
 */
@RestController
@RequestMapping("v1/profile/")
public class ProfileController {
	
	/** The customer repository. */
	@Autowired
	private CustomerRepository customerRepository;
		
	
	/**
	 * Gets the customer.
	 *
	 * @param userName the user name
	 * @return the customer
	 */
	@RequestMapping(value="{userName}", method = RequestMethod.GET, 
			produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(value = HttpStatus.OK)
	@PreAuthorize("#oauth2.isUser() and hasAuthority('ROLE_LOGIN')")
	public Customer getCustomer(@PathVariable("userName") String userName) {
		return customerRepository.findOne(userName);
	}

}
