package com.evertec.cibp.api.authorization.controller;

import java.security.Principal;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * The Class SecurityController.
 */
@RestController
public class SecurityController {
	
	/**
	 * User.
	 *
	 * @param principal the principal
	 * @return the principal
	 */
	@RequestMapping(value = { "/user", "/me" },  method = RequestMethod.GET, 
			produces =  MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(value = HttpStatus.OK)
	public Principal user(Principal principal) {
	  return principal;
	}
}
