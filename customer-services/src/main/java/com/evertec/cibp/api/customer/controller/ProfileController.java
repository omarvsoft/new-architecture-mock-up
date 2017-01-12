package com.evertec.cibp.api.customer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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
	
	@Autowired
	private CustomerRepository customerRepository;
	
	@RequestMapping(value="{customerId}", method = RequestMethod.GET)
	@PreAuthorize("#oauth2.isUser() and hasAuthority('LOGIN')")
	public Customer getCustomer(@PathVariable("customerId") String customerId) {
		return customerRepository.findOne(customerId);
	}

}
