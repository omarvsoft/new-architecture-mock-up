package com.evertec.cibp.api.authentication.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.evertec.cibp.api.authentication.model.SSOToken;
import com.evertec.cibp.api.authentication.repository.SSOTokenRepository;

/**
 * The Class TokenController.
 */
@RestController
@RequestMapping("v1/token/")
public class TokenController {
		
	/** The s SO token repository. */
	@Autowired
	private SSOTokenRepository sSOTokenRepository;
	
	/**
	 * Gets the token.
	 *
	 * @param sso the sso
	 * @return the token
	 */
	@RequestMapping(value = "{sso}", method = RequestMethod.GET)
	@ResponseBody
	public SSOToken getToken(@PathVariable("sso") String sso) {
				
		return sSOTokenRepository.findOne(sso);
	}
	
}