package com.evertec.cibp.api.authentication.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.stereotype.Component;

import com.evertec.cibp.api.authentication.model.Customer;

/**
 * The Class CustomerClient.
 */
@Component
public class CustomerClient {
	
	/** The customer api url. */
	@Value("${ci-bp.services.customer-api.url}")
	private String customerApiUrl;
	
	/** The resource rest template. */
	@Autowired
	private OAuth2RestTemplate resourceRestTemplate;
	
	/**
	 * Gets the customer profile.
	 *
	 * @param userName the user name
	 * @return the customer profile
	 */
	public Customer getCustomerProfile(String userName) {
		return resourceRestTemplate.getForObject(customerApiUrl.concat("/profile/{userName"),
				Customer.class,
				userName);
	}
	
	
}
