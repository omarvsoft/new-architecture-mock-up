package com.evertec.cibp.api.authentication.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.evertec.cibp.api.authentication.model.domain.SSOToken;
import com.evertec.cibp.api.authentication.service.TokenService;

/**
 * The Class TokenController.
 */
@EnableCircuitBreaker
@RestController
@RequestMapping("v1/token/")
public class TokenController {
		
	/** The s SO token repository. */
	@Autowired
	private TokenService tokenService;
	
	/**
	 * Gets the token.
	 *
	 * @param sso the sso
	 * @return the token
	 */
	@RequestMapping(value = "{sso}", method = RequestMethod.GET, produces =
            MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(value = HttpStatus.OK)
	public SSOToken getToken(@PathVariable("sso") String sso) {
				
		return tokenService.getSsoToken(sso);
	}
	
}
