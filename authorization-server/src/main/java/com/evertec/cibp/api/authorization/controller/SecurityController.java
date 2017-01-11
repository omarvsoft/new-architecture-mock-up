package com.evertec.cibp.api.authorization.controller;

import java.security.Principal;

import org.springframework.web.bind.annotation.RequestMapping;
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
	@RequestMapping({ "/user", "/me" })
	public Principal user(Principal principal) {
	  return principal;
	}
}
